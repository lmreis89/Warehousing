package web;

import org.mortbay.jetty.*;
import org.mortbay.jetty.webapp.*;
import org.mortbay.jetty.handler.*;

import ROLAP.ROLAPProcess;
import ROLAP.ServerException;

import javax.servlet.http.*;

public class WPWebServer
{
	public static ThreadLocal<HttpSession> tlSession = new ThreadLocal<HttpSession>();
	int port;
	String docbase;
	Server server;
    ROLAPProcess process;
	WebAppContext webapp;
	
	public WPWebServer( int port, String docbase, ROLAPProcess proc) {
		this.port = port;
		this.docbase = docbase;
		this.process = proc;
	}
	
	public void setCustomMessage( String str) {
		tlSession.get().setAttribute("error-custom", str);
	}

	public void stop() throws ServerException {
		if( server != null)
			try {
				server.stop();
			} catch( Exception e) {
				throw new ServerException( "Impossivel terminar servidor.");
			}
	}
	
	public void start() throws ServerException {
		try {
			server = new Server(port);
			WebAppContext webapp = new WebAppContext(docbase, "/");
			webapp.setAttribute( "ROLAPProcess", process);
			HandlerList hl = new HandlerList();   
			hl.setHandlers(new Handler[]{webapp});   
			server.setHandler(hl);   
		
			server.start();
		} catch( Exception e) {
			throw new ServerException("Impossivel inicar servidor.");
		}
	}

}
