package core.util;

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

import core.pojo.Manager;

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

	public List<List<String>> getTableAsStrings(OID... oids) {
		TableUtils tUtils = new TableUtils(manager.getSnmp(), new DefaultPDUFactory());

		List<TableEvent> events = tUtils
				.getTable(getCommunityTarget(), oids, null, null);

		List<List<String>> list = new ArrayList<List<String>>();
		for (TableEvent event : events) {
			if (event.isError()) {
				throw new RuntimeException(event.getErrorMessage());
			}
			List<String> strList = new ArrayList<String>();
			list.add(strList);
			for (VariableBinding vb : event.getColumns()) {
				strList.add(vb.getVariable().toString());
			}
		}
		return list;
	}

	public List<String> getTableAsStrings2(OID... oids) {
		TableUtils tUtils = new TableUtils(manager.getSnmp(), new DefaultPDUFactory());

		List<TableEvent> events = tUtils
				.getTable(getCommunityTarget(), oids, null, null);

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
	
}