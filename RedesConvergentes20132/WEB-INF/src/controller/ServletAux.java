package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SNMPModel;
import core.util.ManagerUtil;

public class ServletAux extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("includes/routerTemplate.jsp");
		String ipAddress = request.getParameter("ipAddress");
		ManagerUtil manager = new ManagerUtil("udp:"+ipAddress+"/161");
		try {
			manager.listenPort();
			SNMPModel retorno = manager.getSNMPModel();
			ManagerUtil.manager.getSnmp().close();
			retorno.setIpAddress(ipAddress);
			request.setAttribute("retorno", retorno);
			request.setAttribute("ipId", ipAddress.replaceAll("\\.", ""));
			rd.forward(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
