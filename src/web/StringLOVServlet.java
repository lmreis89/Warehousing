package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import xml.meta.Dimension;

import ROLAP.ROLAPProcess;

public class StringLOVServlet extends HttpServlet{

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
		PrintWriter out = response.getWriter();

		ROLAPProcess proc = (ROLAPProcess)session.getServletContext().getAttribute( "ROLAPProcess");
		
		String dimID = proc.getDim();
		String levelID = proc.getLevel();

		List<String> values = proc.DimensionListOfValues(dimID, levelID);
		
		for(String s : values)
		{
			out.print(s +",");
		}
	}
}
