package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import ROLAP.ROLAPProcess;


public class DimTreeServlet extends HttpServlet
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
		ROLAPProcess proc = (ROLAPProcess)session.getServletContext().getAttribute( "ROLAPProcess");
		String op = request.getParameter("op");
		
		if(op.equals("exec"))
		{
			System.out.println("EXEC!!");
			//proc.executeQuery();
		}
		else
		{
			String dimID = request.getParameter("dimID");
			String lvlID = request.getParameter("lvlID");
			System.out.println("op: " + op + " " +dimID + " level: " + lvlID);
			
			if(op.equals("add"))
			{
				proc.addSelectedCollumn(dimID, lvlID);
			}
			else
			{
				proc.removeCollumn(dimID, lvlID);
			}
		}

	}
}
