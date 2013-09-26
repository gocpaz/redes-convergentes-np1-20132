package core.pojo;

import org.snmp4j.Snmp;

public class Manager {
	private Snmp snmp = null;
	private String ipAddress = null;
	private String community = null;
	
	/**
	 * @return the snmp
	 */
	public Snmp getSnmp() {
		return snmp;
	}
	/**
	 * @param snmp the snmp to set
	 */
	public void setSnmp(Snmp snmp) {
		this.snmp = snmp;
	}
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	/**
	 * @return the community
	 */
	public String getCommunity() {
		return community;
	}
	/**
	 * @param community the community to set
	 */
	public void setCommunity(String community) {
		this.community = community;
	}
	
}
