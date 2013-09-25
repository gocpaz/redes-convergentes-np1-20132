package controller;

import java.io.IOException;
import java.util.Date;

import core.util.ManagerUtil;

import model.SNMPModel;

public class SNMPController {

	public SNMPModel getMessage(String ipAddress) {
		ManagerUtil manager = new ManagerUtil(ipAddress);
		try {
			manager.listenPort();
		SNMPModel retorno = manager.getSNMPModel();
		retorno.setIpAddress(ipAddress);
		return retorno;
		} catch (IOException e) {
			e.printStackTrace();
		}
		String newTimeString = new Date().toString();
		SNMPModel model = new SNMPModel(); //SMNPMAnager.getSMNPModel(ipAddress);
		model.setIpAddress(ipAddress);
		model.setSysUpTime(newTimeString);
		return model;
	}
}
