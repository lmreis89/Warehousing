<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="java.util.*"%>

<%
	ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
	
	String dimID = proc.getDim();
	String levelID = proc.getLevel();

	List<String> values = proc.DimensionListOfValues(dimID, levelID);
	String min, max;
	if(values.size() >0)
	{
		min = values.get(0);
		max = values.get(1);
	}
	else
	{
		max = "3200";
		min = "0";
	}
	String display = proc.getCurrentPage();
%>

<div id='jqxDockPanelNumeric' style="background: none; border: none;">
	<script type="text/javascript">
		max = <% out.print(max);%>;
		min = <% out.print(min);%>;
		display = "<%out.print(display);%>";
	</script>
	<div id='top' style="height: 45px;" dock='top'>
		Range of values( Max: <% out.print(max); %>	Min: <% out.print(min); %>)
	</div>
	<div style="width: 200px;" dock='left'>
		<div id='numericInputNumeric'></div>
		<div style="margin-top: 20px; margin-left: 60px;">
			<input type="button" value="Add" id='jqxAddButtonNumeric' />
		</div>
	</div>
	<div id="content" style="width: 150px; margin-right: 5px;">
		<div id='jqxListNumeric'></div>
		<div style="margin-top: 20px; margin-left: 60px;">
			<input type="button" value="Submit" id='jqxButtonNumeric' />
		</div>
	</div>

</div>
<div id="popupNumeric">
	<ul>
		<li id="removeNumeric">Remove</li>
	</ul>
</div>
