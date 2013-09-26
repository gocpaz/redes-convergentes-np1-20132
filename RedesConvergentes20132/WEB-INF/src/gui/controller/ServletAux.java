package gui.controller;

import gui.model.SNMPModel;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.util.ManagerUtil;

public class ServletAux extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, RuntimeException {
		RequestDispatcher rd = request.getRequestDispatcher("includes/routerTemplate.jsp");
		String ipAddress = request.getParameter("ipAddress");
		String community = request.getParameter("community");
		ManagerUtil manager = new ManagerUtil("udp:"+ipAddress+"/161", community);
		manager.listenPort();
		SNMPModel retorno = manager.getSNMPModel();
		manager.getManager().getSnmp().close();
		retorno.setIpAddress(ipAddress);
		request.setAttribute("retorno", retorno);
		request.setAttribute("ipId", ipAddress.replaceAll("\\.", ""));
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
