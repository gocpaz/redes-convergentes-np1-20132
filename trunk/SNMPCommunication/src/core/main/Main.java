package core.main;

import org.snmp4j.smi.OID;

import core.util.ManagerUtil;

@SuppressWarnings("unused")
public class Main {
	private static final String MIB_DOMAIN_NAME = "1.3.6.1.2.1.1.6.0";
	private static final String MIB_SYS_INFO = "1.3.6.1.2.1.1.1.0";
	private static final String MIB_SYS_UP_TIME = "1.3.6.1.2.1.1.3.0";
	private static final String MIB_IOS_VERSION = "1.3.6.1.4.1.9.9.25.1.1.1.2.7";
	private static final String MIB_MAC_ADDRESS = "1.3.6.1.2.1.3.1.1.2.28.1.10.255.0.30";
	private static final String MIB_IP = "1.3.6.1.2.1.3.1.1.3.28.1.10.255.0.30";
	private static final String MIB_IP_GATEWAY = "1.3.6.1.2.1.16.19.12.0";
	private static final String MIB_IP_GENERICO = "1.3.6.1.2.1.4.20.1.1";
	private static final String MIB_SYS_LOCATION = "1.3.6.1.2.1.1.6";
	private static final String MIB_TEST = "1.3.6.1.2.1.1.6";
	
	
	private static final String LOCALHOST = "udp:127.0.0.1/161";
	private static final String THIAGO = "udp:192.168.0.2/161";
	private static final String APCICSCO1100 = "udp:10.0.0.1/161";
	private static final String MAQUINALAB = "udp:10.255.0.30/161";
	
	public static void main(String[] args) {
		ManagerUtil manager = new ManagerUtil(MAQUINALAB);
		try {
			manager.listenPort();
			System.out.println(manager.getInformation(new OID[] {new OID(MIB_TEST), new OID(MIB_SYS_UP_TIME), new OID(MIB_IP)}));
//			System.out.println(manager.getInformation(new OID(MIB_IP_GATEWAY)));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Conexão indisponível.");
		}
	}
}
