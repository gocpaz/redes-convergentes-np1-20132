package gui.model;

import java.util.List;

import core.pojo.Route;

/**
 * @author argeu
 */
public class SNMPModel {
	private String ipAddress;
	private String sysUpTime;
	private List<Route> rotas;
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
}
