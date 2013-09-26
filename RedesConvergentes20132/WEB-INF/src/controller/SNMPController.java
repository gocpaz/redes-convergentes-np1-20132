package controller;

import gui.model.SNMPModel;

import java.util.Date;


public class SNMPController {

	public SNMPModel getMessage(String ipAddress) {
		String newTimeString = new Date().toString();
		SNMPModel model = new SNMPModel(); //SMNPMAnager.getSMNPModel(ipAddress);
		model.setIpAddress(ipAddress);
		model.setSysUpTime(newTimeString);
		return model;
	}
}
