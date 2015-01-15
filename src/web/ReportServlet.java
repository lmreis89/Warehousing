package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ROLAP.ROLAPProcess;

public class ReportServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1422890528328399724L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try {
			java.net.InetAddress addr = java.net.InetAddress.getByName( request.getRemoteHost());
			if( ! (addr.isSiteLocalAddress() || addr.isLoopbackAddress())) {
				System.out.println("NOT LOCAL ADDRESS REQUEST");
				request.getRequestDispatcher("/HelloWorld.jsp").forward(request,response);
				return;
			}
		} catch( Exception e) {

		}
		HttpSession session = request.getSession();
		WPWebServer.tlSession.set(session);
		response.setContentType("text/html;charset=UTF-8");
		String op = request.getParameter("op");
		String name = request.getParameter("name");
		
		ROLAPProcess proc = (ROLAPProcess)session.getServletContext().getAttribute( "ROLAPProcess");
		
		if(op.equals("save"))
			proc.saveQuery(name);
		else
			proc.loadReport(name);
	}
}
