package core.pojo;

public class Interface {
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int TESTING = 3;
	public static final int UNKNOWN = 4;
	public static final int DORMANT = 5;
	public static final int NOT_PRESENT = 6;
	public static final int LOW_LAYER_DOWN = 7;
	
	private String name;
	private String ip;
	private String status;
	private String mac;
	private String pkTrafegadosIn;
	private String pkTrafegadosOut;
	private String pkDescartadosIn;
	private String pkDescartadosOut;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		int nrStatus = Integer.parseInt(status);
		switch(nrStatus){
			case UP:status = "UP";break;
			case DOWN:status = "DOWN";break;
			case TESTING:status = "TESTING";break;
			case UNKNOWN:status = "UNKNOWN";break;
			case DORMANT:status = "DORMANT";break;
			case NOT_PRESENT:status = "NOT PRESENT";break;
			case LOW_LAYER_DOWN:status = "LOW LAYER DOWN";break;
		}
		this.status = status;
	}
	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
	/**
	 * @return the pkTrafegadosIn
	 */
	public String getPkTrafegadosIn() {
		return pkTrafegadosIn;
	}
	/**
	 * @param pkTrafegadosIn the pkTrafegadosIn to set
	 */
	public void setPkTrafegadosIn(String pkTrafegadosIn) {
		this.pkTrafegadosIn = pkTrafegadosIn;
	}
	/**
	 * @return the pkTrafegadosOut
	 */
	public String getPkTrafegadosOut() {
		return pkTrafegadosOut;
	}
	/**
	 * @param pkTrafegadosOut the pkTrafegadosOut to set
	 */
	public void setPkTrafegadosOut(String pkTrafegadosOut) {
		this.pkTrafegadosOut = pkTrafegadosOut;
	}
	/**
	 * @return the pkDescartadosIn
	 */
	public String getPkDescartadosIn() {
		return pkDescartadosIn;
	}
	/**
	 * @param pkDescartadosIn the pkDescartadosIn to set
	 */
	public void setPkDescartadosIn(String pkDescartadosIn) {
		this.pkDescartadosIn = pkDescartadosIn;
	}
	/**
	 * @return the pkDescartadosOut
	 */
	public String getPkDescartadosOut() {
		return pkDescartadosOut;
	}
	/**
	 * @param pkDescartadosOut the pkDescartadosOut to set
	 */
	public void setPkDescartadosOut(String pkDescartadosOut) {
		this.pkDescartadosOut = pkDescartadosOut;
	}
}
