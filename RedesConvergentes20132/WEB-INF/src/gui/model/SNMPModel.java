package gui.model;

import java.util.List;

import core.pojo.Interface;
import core.pojo.Route;

/**
 * @author argeu
 */
public class SNMPModel {
	private String ipAddress;
	private String sysUpTime;
	private List<Route> rotas;
	private String deviceModel;
	private List<Interface> interfaces;
	private String iosVersion;
	private String lastReset;
	
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
	/**
	 * @return the rotas
	 */
	public List<Route> getRotas() {
		return rotas;
	}
	/**
	 * @param rotas the rotas to set
	 */
	public void setRotas(List<Route> rotas) {
		this.rotas = rotas;
	}
	/**
	 * @return the deviceModel
	 */
	public String getDeviceModel() {
		return deviceModel;
	}
	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	/**
	 * @return the interfaces
	 */
	public List<Interface> getInterfaces() {
		return interfaces;
	}
	/**
	 * @param interfaces the interfaces to set
	 */
	public void setInterfaces(List<Interface> interfaces) {
		this.interfaces = interfaces;
	}
	/**
	 * @return the iosVersion
	 */
	public String getIosVersion() {
		return iosVersion;
	}
	/**
	 * @param iosVersion the iosVersion to set
	 */
	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}
	/**
	 * @return the lastReset
	 */
	public String getLastReset() {
		return lastReset;
	}
	/**
	 * @param lastReset the lastReset to set
	 */
	public void setLastReset(String lastReset) {
		this.lastReset = lastReset;
	}
}
