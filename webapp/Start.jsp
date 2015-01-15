<%@page import="xml.meta.Dimension"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="xml.meta.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="stylesheet" href="jqwidgets/styles/jqx.base.css"
	type="text/css" />
<script type="text/javascript" src="scripts/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="jqwidgets/jqxcore.js"></script>
<script type="text/javascript" src="jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript" src="jqwidgets/jqxscrollbar.js"></script>
<script type="text/javascript" src="jqwidgets/jqxpanel.js"></script>
<script type="text/javascript" src="jqwidgets/jqxtree.js"></script>
<script type="text/javascript" src="jqwidgets/jqxcheckbox.js"></script>
<script type="text/javascript" src="jqwidgets/jqxdockpanel.js"></script>
<script type="text/javascript" src="jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript">
$(document)
.ready(
		function() {
			// create jqxTree
			
			$('#jqxTreeFirst').jqxTree({
				height : '300px',
				hasThreeStates : false,
				checkboxes : true,
				enableHover: false
			});
			
			$("#jqxButtonFirst").jqxButton({ width: '100', height: '25',theme : 'summer'});
			
			$("#jqxDockFirst").jqxDockPanel({ width: '300', height: '420',theme : 'summer'});
			
			$('#jqxTreeFirst .jqx-tree-dropdown-root').children().each(function() {
				$(this).children().each(function() {
					if ($(this).attr('class') == 'chkbox jqx-widget jqx-checkbox') {
						$(this).remove();
					}
				})
			});
			
			
			$('#jqxTreeFirst .chkbox.jqx-widget.jqx-checkbox').bind(
					'click',
					function(event) {
						
						var checked;
						$(this).children().children().children()
								.each(
										function() {
											checked = $(this).attr(
													'class');
										})
						if(checked == "jqx-checkbox-check-checked")
						{
							$(this).parent().attr('valid',"");
							
							$('#jqxTreeFirst .jqx-tree-dropdown-root').find('li').each(function() {

								if($(this).attr('valid') == null)
								{
									$('#jqxTreeFirst').jqxTree('checkItem', this,false);
								}
								
							});
							
							$(this).parent().removeAttr('valid');	
						} 
						

					});
			
			$('#jqxButtonFirst').bind('click',function(){
				
				var li = $('#jqxTreeFirst .jqx-checkbox-check-checked').parent().parent().parent().parent();
				var id = $(li).attr('cubeID');

				if(id == null)
				{
					var val = $(li).attr('report');
					$.ajax({
						type : "GET",
						url : "StartServlet",
						data : {
							op : "open",
							value : val
						},
						success : function(){
							window.location = "http://localhost:8080/Workbench.jsp";
						}
					});
				}
				else
				{
					$.ajax({
						type : "GET",
						url : "StartServlet",
						data : {
							op : "cube",
							value : id
						},
						success : function(){
							window.location = "http://localhost:8080/Workbench.jsp";
						}
					});
				}
			});

	});
</script>
</head>
<body class='default'>
	<%
		ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
		List<Cube> cubes = proc.getCubes();
		List<String> reps = proc.getReports();
	%>
	<div id='jqxWidgetFirst'>
		<div style='float: left;'>
			<div id='jqxDockFirst'>
				<div dock='top'>
					<h2>Welcome to ROLAP Client</h2>
				</div>
				<div dock='bottom' style="height: 50px; margin-top: 10px; margin-left: 200px;">
					<input type="button" value="Start" id='jqxButtonFirst' />
				</div>
				<div dock='left' style="width:150px;">
					<div id='jqxTreeFirst' dock='left' style="width:298px;">
						<ul>
							<li>Choose a Cube
								<ul>
									<%
										for(Cube cube : cubes)
										{
											out.print("<li cubeID='" + cube.getFactTable().getTableID()+"'>");
											out.print(cube.getFactTable().getDisplayName());
											out.print("</li>");
										}
									%>
								</ul>
							</li>
							<li>Open a previously saved report
								<ul>
									<%
										for(String report : reps)
										{
											out.print("<li report='" + report + "'>");
											out.print(report);
											out.print("</li>");
										}
									%>
								</ul>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>