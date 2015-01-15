package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ROLAP.ROLAPProcess;

public class DrillServlet extends HttpServlet
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
		ROLAPProcess proc = (ROLAPProcess)session.getServletContext().getAttribute( "ROLAPProcess");
		
		String dimID = request.getParameter("dimID");
		String levelID = request.getParameter("levelID");
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String html ="<ul origin='"+levelID+"/"+dimID+"'> " +
					"<li> Drill-Up";
		int count = proc.numHierarchies(levelID, dimID);
		
		if(count > 0)
		{
			html+="<ul>";
			for(int i = 0; i < count; i++)
			{
				String current = "<li> Hierarchy " + i + "<ul>";
				System.out.println("DRILLING: " +levelID + " "+ dimID + " occur: "+i);
				List<String> upper = proc.upperHierarchy(levelID, dimID, i);
				if(upper.size() > 0)
				{
					html+= current;
					for(String lvl : upper)
					{
						String[] parsed = lvl.split("/");
						
						html+="<li upper='"+parsed[1]+"'>" + parsed[0] + "</li>";
					}
					html+="</ul></li>";
				}
			}
			html+="</ul>";
		}
		
		html+="</li> <li>Drill-Down";
		
		if(count > 0)
		{
			html+="<ul>";
			for(int i = 0; i < count; i++)
			{
				String current = "<li> Hierarchy " + i + "<ul>";
				List<String> lower = proc.lowerHierarchy(levelID, dimID, i);
				if(lower.size() > 0)
				{
					html+= current;
					for(String lvl : lower)
					{
						String[] parsed = lvl.split("/");
						
						html+="<li lower='"+parsed[1]+"'>" + parsed[0] + "</li>";
					}
					html+="</ul></li>";
				}
			}
			html+="</ul>";
		}
		html+="</li></ul>";
		
		out.print(html);
	}
	
}