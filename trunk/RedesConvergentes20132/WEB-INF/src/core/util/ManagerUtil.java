package core.util;

import gui.model.SNMPModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import core.pojo.Interface;
import core.pojo.MIB;
import core.pojo.Manager;
import core.pojo.Route;

public class ManagerUtil {
	private Manager manager;

	/**
	 * Construtor padrão
	 * @param ipAddress - endereço de ip do agente
	 * @param community - nome da community
	 */
	public ManagerUtil(String ipAddress, String community) {
		this.manager = new Manager();
		manager.setIpAddress(ipAddress);
		manager.setCommunity(community);
	}

	/**
	 * Inicia a escuta pela porta do protocolo snmp
	 * 
	 * @throws IOException
	 */
	public void listenPort() throws IOException {
		TransportMapping<?> transport = new DefaultUdpTransportMapping();
		manager.setSnmp(new Snmp(transport));
		transport.listen();
	}

	/**
	 * Recebe a resposta do agente e retorna o valor de uma MIB
	 * 
	 * @param oid
	 * @return
	 * @throws IOException
	 */
	public String getInformation(OID oid) throws IOException, RuntimeException {
		try {
			ResponseEvent event = get(new OID[] { oid });
			return event.getResponse().get(0).getVariable().toString();
		} catch (Exception e) {
			throw new RuntimeException("Sem resposta do servidor.");
		}
	}

	/**
	 * Retorno o evento com os valores das mibs que foram passadas
	 * 
	 * @param oids
	 * @return
	 * @throws IOException
	 */
	public ResponseEvent get(OID oids[]) throws IOException, RuntimeException {
		PDU mibPdu = new PDU();
		mibPdu.setType(PDU.GET);
		for (OID oid : oids) {
			mibPdu.add(new VariableBinding(oid));
		}
		ResponseEvent event = manager.getSnmp().send(mibPdu, getCommunityTarget(), null);
		if (event != null) {
			return event;
		}
		throw new RuntimeException("timed out");
	}

	/**
	 * Retorna o destino com as configurações do community
	 * 
	 * @return
	 */
	private Target getCommunityTarget() {
		Address ipDestino = GenericAddress.parse(manager.getIpAddress());
		CommunityTarget communityTarget = new CommunityTarget();
		communityTarget.setCommunity(new OctetString(manager.getCommunity()));
		communityTarget.setAddress(ipDestino);
		communityTarget.setRetries(2);
		communityTarget.setTimeout(1500);
		communityTarget.setVersion(SnmpConstants.version2c);
		return communityTarget;
	}

	/**
	 * Metodo que retorna um conjunto de dados a partir de um ou mais OIDs.
	 * @param oids
	 * @return
	 * @throws RuntimeException
	 */
	public List<String> getChildrenNodes(OID... oids) throws RuntimeException {
		TableUtils tUtils = new TableUtils(manager.getSnmp(), new DefaultPDUFactory());
		List<TableEvent> events = tUtils.getTable(getCommunityTarget(), oids, null, null);
		List<String> list = new ArrayList<String>();
		for (TableEvent event : events) {
			if (event.isError()) {
				throw new RuntimeException(event.getErrorMessage());
			}
			String strList = "";
			for (VariableBinding vb : event.getColumns()) {
				strList+=(vb.getVariable().toString());
				list.add(strList);
				strList = "";
			}
		}
		return list;
	}
	
	/**
	 * Metodo que retorna a tabela de rotas.
	 * @return
	 */
	public List<Route> getRotas(){
		List<Route> listRota = new ArrayList<Route>();
		List<String> dest = getChildrenNodes(new OID(MIB.ROTA_DESTINO));
		List<String> mask = getChildrenNodes(new OID(MIB.ROTA_MASK));
		List<String> nexthop = getChildrenNodes(new OID(MIB.ROTA_NEXT_HOP));
		List<String> type = getChildrenNodes(new OID(MIB.ROTA_TIPO));
		List<String> proto = getChildrenNodes(new OID(MIB.ROTA_PROTOCOLO));
		for (int i = 0; i < mask.size(); i++) {
			Route rota = new Route();
			rota.setIpRouteEntry(dest.get(i));
			rota.setIpRouteMask(mask.get(i));
			rota.setIpRouteNextHop(nexthop.get(i));
			String typeDesc = "";
			switch(Integer.parseInt(type.get(i))){
				case Route.TYPE_DIRECT: typeDesc = "DIRETO";break;
				case Route.TYPE_INDIRECT: typeDesc = "INDIRETO";break;
			}
			rota.setIpRouteType(typeDesc);
			String protoDesc = "";
			switch(Integer.parseInt(proto.get(i))){
				case Route.protocol_rip:protoDesc = "RIP";break;
				case Route.protocol_local:protoDesc = "LOCAL";break;
				case Route.protocol_netmgmt:protoDesc = "NETMGMT";break;
				case Route.protocol_icmp:protoDesc = "ICMP";break;
			}
			rota.setIpRouteProtocol(protoDesc);
			listRota.add(rota);
		}
		return listRota;
	}

	
	/**
	 * Retorna o objeto populado com as informações do equipamento a serem exibinas na tela.
	 * @return
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public SNMPModel getSNMPModel() throws IOException, RuntimeException {
		SNMPModel model = new SNMPModel();
		
		model.setRotas(getRotas());
		
		model.setSysUpTime(getInformation(new OID(MIB.SYS_UP_TIME)));
		
		model.setLastReset(getLastReset(model.getSysUpTime()));
		
		String retorno = getInformation(new OID(MIB.DEVICE_MODEL));
		String IOSVersion = "";
		if(retorno.toLowerCase().contains("switch")){
			retorno = "Switch";
			IOSVersion = getInformation(new OID(MIB.SYS_INFO)).toLowerCase().split("version")[1].split(",")[0].trim().toUpperCase();
		}else if(retorno.toLowerCase().contains("ap")){
			retorno = "Access Point";
			IOSVersion = getInformation(new OID(MIB.SYS_INFO)).toLowerCase().split("version")[1].split(",")[0].trim().toUpperCase();
		}else if(retorno.toLowerCase().contains("nosuch")){
			retorno = getInformation(new OID(MIB.SYS_INFO));
			if(retorno.toLowerCase().contains("windows")){
				String aux[] = retorno.split("-");
				String hard[] = aux[0].split(" ");
				String soft[] = aux[1].split(" ");
				retorno = hard[0]+" "+hard[1]+"<br/>"+soft[0]+" "+soft[1]+" "+soft[2]+" "+soft[4];
			}else{
				String aux[] = retorno.split(" ");
				retorno = aux[0]+" "+aux[1]+" "+aux[2];
			}
			IOSVersion = null;
		}else{
			retorno = "Roteador";
			IOSVersion = getInformation(new OID(MIB.SYS_INFO)).toLowerCase().split("version")[1].split(",")[0].trim().toUpperCase();
		}
		model.setDeviceModel(retorno);
		model.setIosVersion(IOSVersion);
		
		List<String> listaIndex = getChildrenNodes(new OID(MIB.INTERFACE_INDEX));
		List<String> ip = getChildrenNodes(new OID(MIB.IP_TABLE));
		List<Interface> listaInterface = new ArrayList<Interface>();
		
		List<String> l1 = getChildrenNodes(new OID(MIB.INTERFACE_INDEX_IP));
		
		for (int i = 0; i< listaIndex.size(); i++) {
			Interface in = new Interface();
			String name = getInformation(new OID(MIB.INTERFACE_NAME_INDEX+"."+listaIndex.get(i)));
			if(name.length() > 100){
				name = name.substring(0, 100);
			}
			in.setName(name);
			in.setStatus(getInformation(new OID(MIB.INTERFACE_STATUS+"."+listaIndex.get(i))));
			in.setMac(getInformation(new OID(MIB.INTERFACE_MAC+"."+listaIndex.get(i))));
			
			String descartadosIn = getInformation(new OID(MIB.DISCARDED_PACKETS_IN+"."+listaIndex.get(i)));
			if(descartadosIn.toLowerCase().contains("nosuch")){
				in.setPkDescartadosIn("");
			}else{
				in.setPkDescartadosIn(descartadosIn);
			}
			String descartadosOut = getInformation(new OID(MIB.DISCARDED_PACKETS_OUT+"."+listaIndex.get(i)));
			if(descartadosOut.toLowerCase().contains("nosuch")){
				in.setPkDescartadosOut("");
			}else{
				in.setPkDescartadosOut(descartadosOut);
			}
			String trafegadosIn = getInformation(new OID(MIB.NUMBER_OF_PACKETS_IN+"."+listaIndex.get(i)));
			if(trafegadosIn.toLowerCase().contains("nosuch")){
				in.setPkTrafegadosIn("");
			}else{
				in.setPkTrafegadosIn(trafegadosIn);
			}
			String trafegadosOut = getInformation(new OID(MIB.NUMBER_OF_PACKETS_OUT+"."+listaIndex.get(i)));
			if(trafegadosOut.toLowerCase().contains("nosuch")){
				in.setPkTrafegadosOut("");
			}else{
				in.setPkTrafegadosOut(trafegadosOut);
			}
			for (int j = 0; j < l1.size(); j++) {
				if(l1.get(j).equals(listaIndex.get(i))){
					in.setIp(ip.get(j));
				}
			}
			listaInterface.add(in);
		}
		model.setInterfaces(listaInterface);
		return model;
	}
	
	/**
	 * Método que calcula o ultimo reset do equipamento
	 * @param sysUpTime
	 * @return
	 */
	private String getLastReset(String sysUpTime) {
		long qtDias = 0;
		if(sysUpTime.toLowerCase().contains("day")){
			String dias = "";
			if(sysUpTime.contains("\\([0-9]*\\")){
				dias = sysUpTime.split(",")[0].split("\\)")[1].split("day?")[0].trim();
			}else{
				dias = sysUpTime.split(",")[0].split("day?")[0].trim();
			}
			qtDias = Long.parseLong(dias) * 86400000;
		}
		String horas = sysUpTime.split(",")[sysUpTime.split(",").length-1].split("\\.")[0].split(":")[0].trim();
		long qtHoras = Long.parseLong(horas) * 3600000;
		String minutos = sysUpTime.split(",")[sysUpTime.split(",").length-1].split("\\.")[0].split(":")[1].trim();
		long qtMinutos = Long.parseLong(minutos) * 60000;
		String segundos = sysUpTime.split(",")[sysUpTime.split(",").length-1].split("\\.")[0].split(":")[2].trim();
		long qtSegundos = Long.parseLong(segundos) * 1000;
		
		Locale brasil = new Locale("pt","br");
		Calendar calendarioNovo = Calendar.getInstance(brasil);
		long tempo = calendarioNovo.getTimeInMillis() - (qtDias+qtHoras+qtMinutos+qtSegundos);
		calendarioNovo.setTimeInMillis(tempo);
		Date data = new Date();
		data.setTime(tempo);
		SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss", brasil);
		return df.format(data);
	}

	/**
	 * @return the manager
	 */
	public Manager getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(Manager manager) {
		this.manager = manager;
	}
}