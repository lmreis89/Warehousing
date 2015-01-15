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
<script type="text/javascript" src="jqwidgets/jqxexpander.js"></script>
<script type="text/javascript" src="jqwidgets/jqxdockpanel.js"></script>
<script type="text/javascript" src="jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript">
$(document)
.ready(
		function() {
			// create jqxTree
			
			$('#jqxExpanderMeasures').jqxExpander({
				width : '300px',
				expandAnimationDuration : 700,
				collapseAnimationDuration : 700,
				theme : 'summer'
			});
			$('#jqxTreeMeasures').jqxTree({
				height : '300px',
				hasThreeStates : false,
				checkboxes : true,
				width : '298px',
				enableHover: false
			});
			
			$("#jqxButtonMeasures").jqxButton({ width: '100', height: '25',theme : 'summer'});
			
			$("#jqxDockMeasures").jqxDockPanel({ width: '300', height: '400',theme : 'summer'});
			
			$('#jqxTreeMeasures .jqx-tree-dropdown-root').children().each(function() {
				$(this).children().each(function() {
					if ($(this).attr('class') == 'chkbox jqx-widget jqx-checkbox') {
						$(this).remove();
					}
				})
			});
			
			$('#jqxButtonMeasures').click(function(){
				
				$('#jqxTreeMeasures .jqx-tree-dropdown-root').find('.chkbox.jqx-widget.jqx-checkbox').each(function(){
					var checked;
					$(this).children().children().children().each(function(){
						checked = $(this).attr('class');
					});
					
					if(checked == "jqx-checkbox-check-checked")
					{
						var li = $(this).parent();
						var agg = $(li).attr('agg');
						var parsed = agg.split("/");
						var factID = parsed[0];
						var aggVal = parsed[1];
						
						$.ajax({
							type : "GET",
							url : "MeasuresServlet",
							data : {
								factID : factID,
								aggVal : aggVal
							}
						});
					}
				});
				
				$.ajax({
					type : "GET",
					url : "MeasuresServlet",
					data : {
						factID : "",
						aggVal : ""
					}
				});
			});
			
			
			$('#jqxTreeMeasures .chkbox.jqx-widget.jqx-checkbox').bind(
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
									$('#jqxTreeMeasures').jqxTree('checkItem', this,false);
								}
								
							});
							
							$(this).parent().removeAttr('valid');	
						} 
						

					});
			
			$('#jqxExpanderMeasures').bind('collapsed',function(){
				
				$("#jqxDockMeasures").jqxDockPanel();
				$("#jqxDockMeasures").width(300);
				$("#jqxDockMeasures").height(100);
				$('#jqxDockMeasures').jqxDockPanel('render');
			});
			$('#jqxExpanderMeasures').bind('expanding',function(){

				$("#jqxDockMeasures").jqxDockPanel();
				$("#jqxDockMeasures").width(300);
				$("#jqxDockMeasures").height(400);
				$('#jqxDockMeasures').jqxDockPanel('render');
			});

		});
</script>
</head>
<body class='default'>
	<%
		ROLAPProcess proc = (ROLAPProcess) session.getServletContext()
				.getAttribute("ROLAPProcess");
		//List<Fact> facts = proc.getTESTFacts();
		List<Object> objects = proc.getMeasures();
		List<Fact> facts = new LinkedList<Fact>();
		for(Object obj : objects)
		{
			if(obj instanceof Fact)
				facts.add((Fact)obj);
			
			if(obj instanceof Derived)
			{
				Fact f = new Fact();
				Derived drv = (Derived)obj;
				
				f.setFactID(drv.getFactID());
				f.setDisplayName(drv.getDisplayName());
				f.setAggregation(new Fact.Aggregation());
				f.getAggregation().getOperation().addAll(drv.getAggregation().getOperation());
				
				facts.add(f);
			}
		}
	%>
	<div id='jqxWidgetMeasures'>
		<div style='float: left;'>
			<div id='jqxDockMeasures'>
				<div id='jqxExpanderMeasures' dock='top'>
					<div>Measures</div>
					<div id='jqxTreeMeasures' style='float: left;'>
						<ul>
							<%
								for(Fact fact : facts)
								{
									out.print("<li>");
									out.print(fact.getDisplayName());
									out.print("<ul>");
									for(String s : fact.getAggregation().getOperation())
									{
										out.print("<li agg="+"'"+fact.getFactID()+"/"+s+"'>");
										out.print(s+"</li>");
									}
									out.print("</ul>");
									out.print("</li>");
								}
							%>
						</ul>
					</div>
				</div>
				<div dock ='bottom' style="margin-top: 10px; margin-left: 200px;">
					<input type="button" value="Save" id='jqxButtonMeasures'/>
				</div>
			</div>
		</div>
	</div>
</body>