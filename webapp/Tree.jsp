<%@page import="xml.meta.Dimension"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="xml.meta.*"%>
<%@ page import="java.util.*"%>

	<%
		ROLAPProcess proc = (ROLAPProcess) session.getServletContext()
				.getAttribute("ROLAPProcess");
		//List<Dimension> dims = proc.getTESTDims();
		List<Dimension> dims = (List<Dimension>) session
				.getAttribute("Dimensions");
	%>
	<ul>
		<%
				for (Dimension dim : dims) {
					out.print("<li>");

					out.print(dim.getDisplayName());
					if (dim.getLevels() != null) {
						out.print("<ul>");
						for (Level level : dim.getLevels().getLevel()) {
							out.print("<li lvl=" + "'" + dim.getDimID() + "/"
									+ level.getLevelID() + "'>");
							out.print(level.getDisplayName() + "</li>");
						}
						out.print("</ul>");
						out.print("</li>");
					}
				}
			%>
	</ul>
