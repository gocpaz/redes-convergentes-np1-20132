package model;

/**
 * @author argeu
 */
public class SNMPModel {
	private String ipAddress;
	private String sysUpTime;
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
	 * @return the sysUpTime
	 */
	public String getSysUpTime() {
		return sysUpTime;
	}
	/**
	 * @param sysUpTime the sysUpTime to set
	 */
	public void setSysUpTime(String sysUpTime) {
		this.sysUpTime = sysUpTime;
	}
}
