package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ROLAP.ROLAPProcess;

public class InputChooserServlet extends HttpServlet
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
		
		int type = Integer.parseInt(request.getParameter("type"));
		System.out.println("INPUT CHOOSER : "+type);
		if(type == 0)
		{
			request.getRequestDispatcher("/StringLOV.jsp").forward(request,response);
			return;
		}
		else if(type == 1){
			request.getRequestDispatcher("/Date.jsp").forward(request,response);
		}
			
		else if(type == 2){
			request.getRequestDispatcher("/Numeric.jsp").forward(request,response);
			return;
		}
			
		else if(type == 3)
		{
			request.getRequestDispatcher("/Boolean.jsp").forward(request, response);
			return;
		}
			
		else
			 throw new ServletException("WRONG TYPE!!");
	}
}