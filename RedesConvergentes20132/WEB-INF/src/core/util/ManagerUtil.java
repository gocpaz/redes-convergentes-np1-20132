package core.util;

import gui.model.SNMPModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<Route> getRotas(){
		List<Route> listRota = new ArrayList<Route>();
		List<String> dest = getChildrenNodes(new OID(MIB.ROTA_DESTINO));
		List<String> mask = getChildrenNodes(new OID(MIB.ROTA_MASK));
		List<String> nexthop = getChildrenNodes(new OID(MIB.ROTA_NEXT_HOP));
		List<String> type = getChildrenNodes(new OID(MIB.ROTA_TIPO));
		List<String> proto = getChildrenNodes(new OID(MIB.ROTA_PROTOCOLO));
		for (int i = 0; i < dest.size(); i++) {
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
				case Route.protocol_icmp:protoDesc = "ICMP";break;
			}
			rota.setIpRouteProtocol(protoDesc);
			listRota.add(rota);
		}
		return listRota;
	}

	public SNMPModel getSNMPModel() throws IOException, RuntimeException {
		SNMPModel model = new SNMPModel();
		model.setRotas(getRotas());
		model.setSysUpTime(getInformation(new OID(MIB.SYS_UP_TIME)));
		String retorno = getInformation(new OID(MIB.DEVICE_MODEL));
		if(retorno.toLowerCase().contains("switch")){
			retorno = "Switch";
		}else if(retorno.toLowerCase().contains("ap")){
			retorno = "Access Point";
		}else if(retorno.toLowerCase().contains("nosuch")){
			retorno = getInformation(new OID(MIB.SYS_INFO));
			String aux[] = retorno.split(" ");
			retorno = aux[0]+" "+aux[1]+" "+aux[2];
		}else{
			retorno = "Roteador";
		}
		model.setDeviceModel(retorno);
		
		List<String> listaNome = getChildrenNodes(new OID(MIB.INTERFACE_NAME));
		List<String> listaStatus = getChildrenNodes(new OID(MIB.INTERFACE_STATUS));
		List<String> listaIndex = getChildrenNodes(new OID(MIB.INTERFACE_INDEX));
//		for (String string : listaIndex) {
//			System.out.println(string);
//		}
	for (int i = 0; i< listaIndex.size(); i++) {
		System.out.println(getInformation(new OID(MIB.INTERFACE_NAME_INDEX+"."+listaIndex.get(i))));
		System.out.println(getInformation(new OID(MIB.INTERFACE_STATUS+"."+listaIndex.get(i))));
	}
//		for (int i = 0; i< listaNome.size(); i++) {
//			System.out.println(listaNome.get(i)+" ; "+listaIP.get(i)+" ; "+listaStatus.get(i));
//		}
		return model;
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