package core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.SNMPModel;

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

import core.pojo.Manager;
import core.pojo.Route;

public class ManagerUtil {
	Manager manager;

	/**
	 * Construtor padrão
	 * @param ipAddress - endereço de ip do agente
	 */
	public ManagerUtil(String ipAddress) {
		this.manager = new Manager();
		manager.setIpAddress(ipAddress);
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
	public String getInformation(OID oid) throws IOException {
		try {
			ResponseEvent event = get(new OID[] { oid });
			return event.getResponse().get(0).getVariable().toString();
		} catch (NullPointerException e) {
			return "Sem resposta do servidor.";
		}
	}

	/**
	 * Retorno o evento com os valores das mibs que foram passadas
	 * 
	 * @param oids
	 * @return
	 * @throws IOException
	 */
	public ResponseEvent get(OID oids[]) throws IOException {
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
		communityTarget.setCommunity(new OctetString("public"));
		communityTarget.setAddress(ipDestino);
		communityTarget.setRetries(2);
		communityTarget.setTimeout(1500);
		communityTarget.setVersion(SnmpConstants.version2c);
		return communityTarget;
	}

	public String getInformation(OID[] oids) {
		try {
			ResponseEvent event = get(oids);
			String retorno = "";
			for (int i = 0; i < oids.length; i++) {
				retorno = retorno.concat(oids[i]+" -> "+event.getResponse().get(i).getVariable().toString()+"\n");
			}
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return "Sem resposta do servidor.";
		}
	}

	public List<String> getTableAsStrings2(OID... oids) {
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
	
	public String extractSingleString(ResponseEvent event) {
		return event.getResponse().get(0).getVariable().toString();
	}
	
	public List<Route> getRotas(){
		List<Route> listRota = new ArrayList<Route>();
//			1.3.6.1.2.1.4.21.1.1 - dest
		List<String> dest = getTableAsStrings2(new OID("1.3.6.1.2.1.4.21.1.1"));
//			1.3.6.1.2.1.4.21.1.11 - mask
		List<String> mask = getTableAsStrings2(new OID("1.3.6.1.2.1.4.21.1.11"));
//			1.3.6.1.2.1.4.21.1.7 - nexthop
		List<String> nexthop = getTableAsStrings2(new OID("1.3.6.1.2.1.4.21.1.7"));
//			1.3.6.1.2.1.4.21.1.8 - type
		List<String> type = getTableAsStrings2(new OID("1.3.6.1.2.1.4.21.1.8"));
//		1.3.6.1.2.1.4.21.1.8 - proto
		List<String> proto = getTableAsStrings2(new OID("1.3.6.1.2.1.4.21.1.9"));
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
			System.out.println(rota.toString());
			listRota.add(rota);
		}
		return listRota;
	}

	public SNMPModel getSNMPModel() throws IOException {
		SNMPModel model = new SNMPModel();
		model.setRotas(getRotas());
		model.setSysUpTime(getInformation(new OID("1.3.6.1.2.1.1.3.0")));
		return null;
	}
}