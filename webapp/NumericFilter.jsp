<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="java.util.*"%>
<%
	ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
	
	String factID = proc.getFactID();

	List<String> values = proc.getMinMax(factID);
	String min = values.get(0);
	String max = values.get(1);
	String display = proc.getCurrentPage();
%>

<div id='jqxWidgetFilter'>
	<div id='jqxDockPanelNumFilter' style="background: none; border: none;">
		<script type="text/javascript">
			max = <% out.print(max);%>;
			min = <% out.print(min);%>;
			display = "<%out.print(display);%>";
			source = [
			                // Business & Investing
			                    {html: "<div style='padding: 1px;'><div>Equality</div></div>", title: "Equality", group: "Operations" },
			                    {html: "<div style='padding: 1px;'><div>Greater</div></div>", title: "Greater", group: "Operations" },
			                    {html: "<div style='padding: 1px;'><div>Less</div></div>", title: "Less", group: "Operations" }
			                 ];
		</script>
		<div id='top' style="height: 80px;" dock='top'>
			Range of values( Max:
			<% out.print(max); %>
			Min:
			<% out.print(min); %>)
		</div>
		<div dock='bottom' style='height: 130px; background: #efefef;''>
			<div id="dropDownFilterList"></div>
			<div style="margin-top: 20px;">
				<input type="button" value="Cancel" id='FilterCancelNumFilter' />
			</div>
			<div style="margin-top: -25px; margin-left: 120px;">
				<input type="button" value="Submit" id='jqxButtonNumFilter' />
			</div>
		</div>
		<div dock='bottom' style='height: 40px;'>
			
		</div>
		<div style="width: 200px;" dock='left'>
			<div id='numericInputNumFilter'></div>
		</div>
	
	</div>
</div>