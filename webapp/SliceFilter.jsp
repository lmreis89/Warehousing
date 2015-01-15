<%@page import="xml.client.Filter"%>
<%@page import="xml.client.SelectedFacts"%>
<%@page import="xml.client.Dimension"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="java.util.*"%>


<%
	ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
	Iterator<ROLAP.Slice> slices = proc.getQueryDimensions().iterator();
	Iterator<ROLAP.Filter> factsBefore = proc.getSelectedFacts().iterator();
	Iterator<ROLAP.Filter> factsAfter = proc.getSelectedFacts().iterator();
%>
<div id='jqxDockPanelMenu'>
	<div id='topMenu' style="height: 70px;" dock='top'></div>
	<div id='bottom1Menu' style="height: 20px;" dock='bottom'></div>

	<div style="width: 343px;" dock='right'>
		<div id="filter" style="margin-right: 50px;">
			<div id="settings-panel">
				<div class="settings-section-top settings-section">
					<div class="settings-label">Filter Before</div>
					<div class="settings-setter"></div>
				</div>
				<%  while(factsBefore.hasNext())
						{
							ROLAP.Filter fact = factsBefore.next();
							if(factsBefore.hasNext())
							{
								out.print("<div class='settings-section'>");
							}
							else
							{
								out.print("<div class='sections-section-bottom settings-section'>");
							}
							out.print("<div class='settings-label'>"+ fact.getDisplay() + "</div>");
							out.print("<div class='settings-setter'>");
							out.print("<div>");
							out.print("<input type='button' value='Filter' filter='before/" + fact.getFactID() +"' />");
							out.print("</div>");
							out.print("</div>");
							out.print("</div>");
						}
					%>
				<div class="settings-section-top settings-section"
					style='margin-top: 20px;'>
					<div class="settings-label">Filter After</div>
					<div class="settings-setter"></div>
				</div>

				<%  while(factsAfter.hasNext())
						{
							ROLAP.Filter fact = factsAfter.next();
							if(factsAfter.hasNext())
							{
								out.print("<div class='settings-section'>");
							}
							else
							{
								out.print("<div class='sections-section-bottom settings-section'>");
							}
							out.print("<div class='settings-label'>"+ fact.getDisplay() + "</div>");
							out.print("<div class='settings-setter'>");
							out.print("<div>");
							out.print("<input type='button' value='Filter' filter='after/" + fact.getFactID() +"' />");
							out.print("</div>");
							out.print("</div>");
							out.print("</div>");
						}
					%>
			</div>
		</div>
	</div>
	<% slices = proc.getQueryDimensions().iterator(); %>
	<div style="width: 355px;" dock='left'>
		<div id="slice" style="margin-left: 10px;">
			<div id="settings-panel">
				<div class="settings-section-top settings-section">
					<div class="settings-label">Slice</div>
					<div class="settings-setter"></div>
				</div>
				<%  while(slices.hasNext())
						{
							ROLAP.Slice slice = slices.next();
							if(slices.hasNext())
							{
								out.print("<div class='settings-section'>");
							}
							else
							{
								out.print("<div class='sections-section-bottom settings-section'>");
							}
							out.print("<div class='settings-label'>"+ slice.getDisplay() + "</div>");
							out.print("<div class='settings-setter'>");
							out.print("<div>");
							out.print("<input type='button' value='Slice' slice='" + slice.getDimID() + "/" + slice.getLevelID() + "/" + slice.getType() +"' />");
							out.print("</div>");
							out.print("</div>");
							out.print("</div>");
						}
					%>
			</div>
		</div>
	</div>
	<div id='contentMenu' style="width: 105px;"></div>
</div>