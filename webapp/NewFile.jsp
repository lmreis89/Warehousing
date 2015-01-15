<%@page import="xml.meta.Dimension"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="xml.meta.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="keywords" content="jQuery Tree, Tree Widget, Tree" />
<meta name="description"
	content="The jqxTree can display a checkboxes next to its items. You can also enable three-state checkboxes. In this mode, when the user
     checks an item, its sub items also become checked. When there is an unchecked item, the parent item is in indeterminate state." />
<title id='Description'>The jqxTree can display a checkboxes
	next to its items. You can also enable three-state checkboxes. In this
	mode, when the user checks an item, its sub items also become checked.
	When there is an unchecked item, the parent item is in indeterminate
	state.</title>
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
			
			$('#jqxTree').jqxTree({
				height : '300px',
				hasThreeStates : false,
				checkboxes : true,
				width : '298px',
				enableHover: false
			});
			
			$("#jqxButton").jqxButton({ width: '100', height: '25',theme : 'summer'});
			
			$("#jqxDock").jqxDockPanel({ width: '300', height: '400',theme : 'summer'});
			
			$('#jqxTree .jqx-tree-dropdown-root').children().each(function() {
				$(this).children().each(function() {
					if ($(this).attr('class') == 'chkbox jqx-widget jqx-checkbox') {
						$(this).remove();
					}
				})
			});
			
			
			$('#jqxTree .chkbox.jqx-widget.jqx-checkbox').bind(
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
							
							$(this).parent().parent().children().each(function(){

								if($(this).attr('valid') == null)
								{
									$('#jqxTree').jqxTree('checkItem', this,false);
								}
								
							});
							
							$(this).parent().removeAttr('valid');	
						} 
						

					});

	});
</script>
</head>
<body class='default'>
	<%
		ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
		List<Cube> cubes = proc.getTESTCubes();
		List<String> reps = proc.getTESTReports();
	%>
	<div id='jqxWidget'>
		<div style='float: left;'>
			<div id='jqxDock'>
				<div dock='top'>
					<h2>Dimensions</h2>
				</div>
				<div id='jqxTree' style='float: left;' dock ='top'>
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
										out.print("<li>");
										out.print(report);
										out.print("</li>");
									}
								%>
							</ul>
						</li>
					</ul>
				</div>
				<div dock='bottom' style="margin-top: 10px; margin-left: 250px;">
					<input type="button" value="Start" id='jqxButton' />
				</div>
			</div>
		</div>
	</div>
</body>
</html>