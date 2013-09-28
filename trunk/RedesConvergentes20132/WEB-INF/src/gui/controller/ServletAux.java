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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("includes/routerTemplate.jsp");
		String ipAddress = request.getParameter("ipAddress");
		String community = request.getParameter("community");
		ManagerUtil manager = new ManagerUtil("udp:"+ipAddress+"/161", community);
		try{
			manager.listenPort();
			SNMPModel retorno = manager.getSNMPModel();
			retorno.setIpAddress(ipAddress);
			manager.getManager().getSnmp().close();
			request.setAttribute("ipId", ipAddress.replaceAll("\\.", ""));
			request.setAttribute("retorno", retorno);
			rd.forward(request, response);
		} catch(Exception e){
			request.setAttribute("mensagem", e.getMessage());
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

}
