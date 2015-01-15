<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ page import="ROLAP.*"%>
<%@ page import="java.util.*"%>
<%
	ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
	
	String display = proc.getCurrentPage();
%>


<div id='jqxWidgetString'
	style="width: 300px; height: 600px; font-size: 13px; font-family: Verdana;">
	<div id='jqxDockPanelString'>
	<script type="text/javascript">
		display ='<%out.print(display);%>';
	</script>
		<div id='TitleString' dock='top'>
			<h2>List of values</h2>
		</div>

		<div id='jqxListString' dock='top' style="margin-bottom: 10px;"></div>
		<div dock='bottom' style="margin-top: 10px; margin-left: 150px;">
			<input type="button" value="Submit" id='jqxButtonString' />
		</div>
	</div>
</div>
