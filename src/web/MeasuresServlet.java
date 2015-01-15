package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ROLAP.ROLAPProcess;

public class MeasuresServlet extends HttpServlet
{

	private static final long serialVersionUID = -3070004611037359188L;

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
		
		String factID = request.getParameter("factID");
		String value = request.getParameter("aggVal");
		
		System.out.println("fact " + factID + " value: "+value);
		
		if(factID.equals("") && value.equals(""))
		{
			System.out.println("Calculating Dimensions..");
			session.setAttribute("Dimensions", proc.getDimensions());
		}
		else
		{
			proc.addFact(factID, value);
		}


	}
}