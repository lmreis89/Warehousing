<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="java.util.*"%>

<%
	ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
	
	String display = proc.getCurrentPage();
%>
<div id='content'>
	<div id='jqxWidgetDate'>
	<script type="text/javascript">
		display ='<%out.print(display);%>';
	</script>
		<div id="jqxDockDate">
			<div dock="top" style="margin-left: 20px;">
				<h1>Choose a date</h1>
			</div>
			<div id="jqxCalendarDate" dock="top"></div>

			<div dock='bottom' style="margin-top: 20px; margin-left: 20px;">
				<input type="button" value="Cancel" id='jqxCancelButtonDate' /> <input
					type="button" value="Submit" id='jqxButtonDate'
					style='margin-right: 10px;' />
			</div>
		</div>
	</div>
</div>
