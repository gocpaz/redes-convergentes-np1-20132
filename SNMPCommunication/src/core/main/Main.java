package core.main;

import org.snmp4j.smi.OID;

import core.util.ManagerUtil;

@SuppressWarnings("unused")
public class Main {
	private static final String MIB_DOMAIN_NAME = "1.3.6.1.2.1.1.6.0";
	private static final String MIB_SYS_INFO = "1.3.6.1.2.1.1.1.0";
	private static final String LOCALHOST = "udp:127.0.0.1/161";
	private static final String THIAGO = "udp:192.168.0.2/161";
	
	public static void main(String[] args) {
		ManagerUtil manager = new ManagerUtil(LOCALHOST);
		try {
			manager.listenPort();
			System.out.println(manager.getInformation(new OID[] {new OID(MIB_SYS_INFO), new OID(MIB_DOMAIN_NAME)}));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Conexão indisponível.");
		}
	}
}
