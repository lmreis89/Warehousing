package web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ROLAP.ROLAPProcess;



public class LoginServlet extends HttpServlet{

	private static final long serialVersionUID = 8467143275313331548L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
		javax.servlet.ServletContext srvContext = (javax.servlet.ServletContext)session.getServletContext();

		try {
			session.setAttribute( "user", null);
			session.setAttribute( "password", null);
			session.setAttribute( "error", null);
			session.setAttribute("userList", null);

			String value = request.getParameter( "login");
			if( value != null && value.equalsIgnoreCase( "Login")) {
				String name = request.getParameter( "nome");
				if( name == null)
					name = "";
				name.trim();
				String segredo = request.getParameter( "segredo");
				if( segredo == null)
					segredo = "";
				name.trim();
				if( name == null || name.length() == 0) {
					session.setAttribute( "error", "Name must be non null.");
					request.getRequestDispatcher("/login.jsp").forward(request,response);
				} else if( segredo == null || segredo.length() == 0) {
					session.setAttribute( "error", "Password must be non null.");
					request.getRequestDispatcher("/login.jsp").forward(request,response);
				} else {

					ROLAPProcess proc = (ROLAPProcess)session.getServletContext().getAttribute( "ROLAPProcess");
					List<String> users = proc.register( name, segredo);
	

					srvContext.setAttribute( "user", name);
					session.setAttribute( "user", name);
					session.setAttribute( "password", segredo);	
					session.setAttribute("userList", users);
					request.getRequestDispatcher("/ListUsers.jsp").forward(request,response);
				}
			} else {
				request.getRequestDispatcher("/login.jsp").forward(request,response);
			}
		} catch( Exception e) {
			StringBuffer buf = new StringBuffer();
			buf.append("Internal error : " + e.getMessage() + "\n");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream( baos);
			e.printStackTrace( ps);
			ps.flush();
			buf.append("<PRE>");
			buf.append( new String(baos.toByteArray()));
			buf.append("</PRE>");


			session.setAttribute( "error", buf.toString());
			request.getRequestDispatcher("/").forward(request,response);
		}
	}

}
