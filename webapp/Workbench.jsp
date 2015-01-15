<%@page import="xml.client.Query"%>
<%@page import="xml.meta.Dimension"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="xml.meta.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>ROLAP CLIENT BROWSER</title>
<link rel="stylesheet" href="jqwidgets/styles/jqx.base.css"
	type="text/css" />
<link rel="stylesheet" href="style/jqModal.css" type="text/css" />
<link rel="stylesheet" href="style/Slice.css" type="text/css" />
<script type="text/javascript" src="scripts/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="jqwidgets/jqxcore.js"></script>
<script type="text/javascript" src="jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript" src="jqwidgets/jqxscrollbar.js"></script>
<script type="text/javascript" src="jqwidgets/jqxtabs.js"></script>
<script type="text/javascript" src="jqwidgets/jqxdata.js"></script> 
<script type="text/javascript" src="jqwidgets/jqxgrid.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.sort.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.pager.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.grouping.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.selection.js"></script> 
<script type="text/javascript" src="jqwidgets/jqxgrid.columnsresize.js"></script>
<script type="text/javascript" src="jqwidgets/jqxpanel.js"></script>
<script type="text/javascript" src="jqwidgets/jqxtree.js"></script>
<script type="text/javascript" src="jqwidgets/jqxcheckbox.js"></script>
<script type="text/javascript" src="jqwidgets/jqxexpander.js"></script>
<script type="text/javascript" src="jqwidgets/jqxdockpanel.js"></script>
<script type="text/javascript" src="jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript" src="jqwidgets/jqxnavigationbar.js"></script>
<script type="text/javascript" src="jqwidgets/jqxmenu.js"></script>
<script type="text/javascript" src="jqwidgets/jqxwindow.js"></script>
<script type="text/javascript" src="jqwidgets/jqxlistbox.js"></script>
<script type="text/javascript" src="jqwidgets/jqxdropdownlist.js"></script>
<script type="text/javascript" src="jqwidgets/jqxnumberinput.js"></script>
<script type="text/javascript" src="jqwidgets/jqxswitchbutton.js"></script>
<script type="text/javascript" src="jqwidgets/jqxdatetimeinput.js"></script>
<script type="text/javascript" src="jqwidgets/jqxcalendar.js"></script>
<script type="text/javascript" src="jqwidgets/globalization/jquery.global.js"></script>
<script type="text/javascript" src="scripts/jquery.blockUI.js"></script>
<script type="text/javascript" src="scripts/jqModal.js"></script>
<script type="text/javascript" src="scripts/Workbench.js"></script>

<%
	ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
	Query query = proc.getQuery();
%>

<script type="text/javascript">
$(document).ready(function(){
	hasResponse = <% out.print((query.getCube().getSelectedFacts().getFact().size() > 0 ? true: false)); %>;
	initPanels();
	if(hasResponse)
	{
		$('#executeStart').trigger('click');
	}
});
</script>
</head>
<body class='default'>
	<%
		//List<Fact> facts = proc.getTESTFacts();
		List<Object> objects = proc.getMeasures();
		List<Fact> facts = new LinkedList<Fact>();
		for (Object obj : objects) {
			if (obj instanceof Fact)
				facts.add((Fact) obj);

			if (obj instanceof Derived) {
				Fact f = new Fact();
				Derived drv = (Derived) obj;

				f.setFactID(drv.getFactID());
				f.setDisplayName(drv.getDisplayName());
				f.setAggregation(new Fact.Aggregation());
				f.getAggregation().getOperation()
						.addAll(drv.getAggregation().getOperation());

				facts.add(f);
			}
		}
	%>
	<div id='jqxWidgetStart' style='width: 1024px;'>

		<div style='float: left;'>
			<div id='jqxDockMain'>
				<div dock='top' style='height: 50px;'>
					<div id='jqxMenu' style='visibility: hidden;'>
						<ul>
							<li>Open
								<ul>
									<li id="reports">Open Report
										<ul>
											<%
												for (String report : proc.getCurrentReports()) {
													out.print("<li rep=''>");
													out.print(report);
													out.print("</li>");
												}
											%>
										</ul>
									</li>
									<li id="cubes">Open cube
										<ul>
											<%
												for (Cube cube : proc.getCubes()) {
													out.print("<li cube='" + cube.getFactTable().getTableID()
															+ "'>");
													out.print(cube.getFactTable().getDisplayName());
													out.print("</li>");
												}
											%>
										</ul>
									</li>
								</ul>
							</li>
							<li id='save'>Save current report</li>
							<li id='status'>Show current query</li>
							<li>About
								<ul>
									<li>Made by</li>
									<li>Luis Reis 25565</li>
									<li>Pedro Amaral 25763</li>
								</ul>
							</li>
						</ul>
					</div>
				</div>
				<div dock='bottom'
					style='height: 80px; margin-top: 10px;'>
					<div style="float: left;">
						<input type="button" value="Slice/Filter" id='slicerfilter' />
					</div>
					<div style="float: left; margin-left: 135px;">
						<input type="button" value="Execute" id='executeStart' />
					</div>
				</div>
				<div id='table' dock='right' style='width: 700px;'>
					<div id='jqxTabs' style="float: left; visibility:hidden">
				            <ul>
				                <li style="margin-left: 30px;">Drilling</li>
				                <li>Grouped</li>
				            </ul>
				            
				            <div>
				            	 <div id="gridPaged"></div>
				            </div>
				            
				            <div>
				            	 <div id="gridGrouped"></div>
				            </div>
					
				    </div>
				</div>
				<div dock='left' style='width: 300px;'>
					<div id='navigationStart'>
						<div id='measures'>Measures</div>
						<div id='jqxTreeMeasures' style='float: left;'>
							<ul>
								<%
									for (Fact fact : facts) {
										out.print("<li>");
										out.print(fact.getDisplayName());
										out.print("<ul>");
										for (String s : fact.getAggregation().getOperation()) {
											out.print("<li agg=" + "'" + fact.getFactID() + "/" + s
													+ "'>");
											out.print(s + "</li>");
										}
										out.print("</ul>");
										out.print("</li>");
									}
								%>
							</ul>
						</div>
						<div id="dims">Dimensions</div>
						<div>
							<div id='jqxTreeStart'></div>
						</div>
					</div>
				</div>
				<div style='width: 24px;'></div>
			</div>
		</div>


		<div id='sliceWindow' style='float: right;'>
			<div id='sliceWindowHeader'>
				<span>Slice And Filter</span>
			</div>
			<div id='sliceWindowContent'></div>
		</div>
		<div id='popup'>
			<div id='popupHeader'></div>
			<div id='popupContent'></div>
		</div>
		<div id='popStatus'>
			<div id='popStatusHeader'>
				<span>Current Query</span>
			</div>
			<div id='popStatusContent'></div>
		</div>
		<div id='popSave'>
			<div id='popSaveHeader'>
				<span>Please enter the report name</span>
			</div>
			<div id='popSaveContent'>
				<input id="inputText" type="text"/>
				<input id="saveSubmit" value="Submit report" type="submit"/>
			</div>
		</div>
	</div>

</body>