<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="java.util.*"%>
<%
	ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
	
	String display = proc.getCurrentPage();
%>



<div id='boolDock' style="background: none; border: none;">
<script type="text/javascript">
	display ='<%out.print(display);%>';
</script>
	<div dock='top' style="height: 70px;">
		<div id='switch' style='margin-left: 55px; margin-top: 20px;'></div>
	</div>
	<div dock='bottom' style="height: 50px;">
		<div style='float: left;'>
			<input type="button" value="Cancel" id='boolCancel' />
		</div>
		<div style='float: right;'>
			<input type="button" value="Submit" id='boolSubmit' />
		</div>
	</div>
</div>
