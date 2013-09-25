package core.pojo;

public class Route {
	public static final int TYPE_OTHER = 1;
	public static final int TYPE_INVALID = 2;
	public static final int TYPE_DIRECT = 3;
	public static final int TYPE_INDIRECT = 4;
	
	public static final int protocol_other     =  1 ;
	public static final int protocol_local    =  2 ;
	public static final int protocol_netmgmt  =  3 ;
	public static final int protocol_icmp     =  4 ;
	public static final int protocol_egp      =  5 ;
	public static final int protocol_ggp      =  6 ;
	public static final int protocol_hello    =  7 ;
	public static final int protocol_rip      =  8 ;
	public static final int protocol_is_is    =  9 ;
    public static final int protocol_es_is     = 10 ;
    public static final int protocol_ciscoIgrp = 11 ;
    public static final int protocol_bbnSpfIgp = 12 ;
    public static final int protocol_ospf      = 13 ;
    public static final int protocol_bgp       = 14 ;
	
	
	private String ipRouteEntry;
	private String ipRouteMask;
	private String ipRouteNextHop;
	private String ipRouteType;
	private String ipRouteProtocol;
	
	public String getIpRouteEntry() {
		return ipRouteEntry;
	}
	public void setIpRouteEntry(String ipRouteEntry) {
		this.ipRouteEntry = ipRouteEntry;
	}
	public String getIpRouteMask() {
		return ipRouteMask;
	}
	public void setIpRouteMask(String ipRouteMask) {
		this.ipRouteMask = ipRouteMask;
	}
	public String getIpRouteNextHop() {
		return ipRouteNextHop;
	}
	public void setIpRouteNextHop(String ipRouteNextHop) {
		this.ipRouteNextHop = ipRouteNextHop;
	}
	public String getIpRouteType() {
		return ipRouteType;
	}
	public void setIpRouteType(String ipRouteType) {
		this.ipRouteType = ipRouteType;
	}
	public String getIpRouteProtocol() {
		return ipRouteProtocol;
	}
	public void setIpRouteProtocol(String ipRouteProtocol) {
		this.ipRouteProtocol = ipRouteProtocol;
	}
	@Override
	public String toString() {
		
		return ipRouteEntry+" "+ipRouteMask+" "+ipRouteNextHop+" "+ipRouteType+" "+ipRouteProtocol;
	}
}
