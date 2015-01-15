<%@page import="xml.client.Dimension"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="keywords"
	content="jQuery SwitchButton, SwitchButton Widget, jqxSwitchButton" />
<meta name="description"
	content="jqxSwitchButton is jQuery widget with functionality similar to checkbox. It has two states - check and uncheck (on/off). The user can switch between the different states using mouse clicks or drag and drop." />
<title id='Description'>jqxSwitchButton is jQuery widget with
	behavior similar to the jqxCheckBox. It has two states - checked and
	unchecked (on/off). The user can switch between the different states
	using mouse clicks or by drag and drop of the thumb.</title>
<link rel="stylesheet" href="jqwidgets/styles/jqx.base.css"
	type="text/css" />
<script type="text/javascript" src="scripts/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="jqwidgets/jqxcore.js"></script>
<script type="text/javascript" src="jqwidgets/jqxswitchbutton.js"></script>
<script type="text/javascript" src="jqwidgets/jqxcheckbox.js"></script>
<script type="text/javascript" src="jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript">
        $(document).ready(function () {

            $('body').find('input').each(function(){
            	$(this).jqxButton({ height: 27, width: 81});
            	
            });
            
            $('input').bind('click',function(){
				var slice = $(this).attr('slice');
				var toParse = slice.split("/");
				
				var dimID = toParse[0];
				var levelID = toParse[1];
				var type = toParse[2];
				
				$.ajax({
					type : "GET",
					url : "DimensionSessionServlet",
					data : {
						dimID : dimID,
						levelID : levelID
					}
				});
				
				$.ajax({
					type : "GET",
					url : "InputChooserServlet",
					data : {
						type : type
					}
				});
            });
        });
    </script>
<style type="text/css">
.jqx-switchbutton-label-on-custom {
	filter: progid:DXImageTransform.Microsoft.gradient(  startColorstr='#3065c4',
		endColorstr='#75adfc', GradientType=0 ); /* IE6-9 */
	background-image: linear-gradient(bottom, rgb(118, 174, 252) 20%,
		rgb(48, 103, 197) 62% );
	background-image: -o-linear-gradient(bottom, rgb(118, 174, 252) 20%,
		rgb(48, 103, 197) 62% );
	background-image: -moz-linear-gradient(bottom, rgb(118, 174, 252) 20%,
		rgb(48, 103, 197) 62% );
	background-image: -webkit-linear-gradient(bottom, rgb(118, 174, 252) 20%,
		rgb(48, 103, 197) 62% );
	background-image: -ms-linear-gradient(bottom, rgb(118, 174, 252) 20%,
		rgb(48, 103, 197) 62% );
	background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0.2, rgb(118,
		174, 252) ), color-stop(0.62, rgb(48, 103, 197) ) );
	color: #fff;
	text-shadow: 0px -1px 1px #000;
}

.jqx-switchbutton-label-off-custom {
	background: #cfcfcf; /* Old browsers */
	background: -moz-linear-gradient(top, #cfcfcf 0%, #fefefe 100%);
	/* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #cfcfcf),
		color-stop(100%, #fefefe) ); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #cfcfcf 0%, #fefefe 100%);
	/* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #cfcfcf 0%, #fefefe 100%);
	/* Opera 11.10+ */
	background: -ms-linear-gradient(top, #cfcfcf 0%, #fefefe 100%);
	/* IE10+ */
	background: linear-gradient(top, #cfcfcf 0%, #fefefe 100%); /* W3C */
	filter: progid:DXImageTransform.Microsoft.gradient(  startColorstr='#cfcfcf',
		endColorstr='#fefefe', GradientType=0 ); /* IE6-9 */
	color: #808080;
}

.jqx-switchbutton-thumb-custom {
	background: #bababa; /* Old browsers */
	background: -moz-linear-gradient(top, #bababa 0%, #fefefe 100%);
	/* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #bababa),
		color-stop(100%, #fefefe) ); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #bababa 0%, #fefefe 100%);
	/* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #bababa 0%, #fefefe 100%);
	/* Opera 11.10+ */
	background: -ms-linear-gradient(top, #bababa 0%, #fefefe 100%);
	/* IE10+ */
	background: linear-gradient(top, #bababa 0%, #fefefe 100%); /* W3C */
	filter: progid:DXImageTransform.Microsoft.gradient(  startColorstr='#bababa',
		endColorstr='#fefefe', GradientType=0 ); /* IE6-9 */
	border: 1px solid #aaa;
	-webkit-box-shadow: -6px 0px 17px 1px #275292;
	-moz-box-shadow: -6px 0px 17px 1px #275292;
	box-shadow: -6px 0px 17px 1px #275292;
}

.jqx-switchbutton-custom {
	border: 1px solid #999999;
}

#settings-panel {
	background-color: #fff;
	padding: 20px;
	display: inline-block;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
	border-radius: 10px;
	position: relative;
}

.settings-section {
	background-color: #f7f7f7;
	height: 45px;
	width: 330px;
	border: 1px solid #b4b7bc;
	border-bottom-width: 0px;
}

.settings-section-top {
	border-bottom-width: 0px;
	-moz-border-radius-topleft: 10px;
	-webkit-border-top-left-radius: 10px;
	border-top-left-radius: 10px;
	-moz-border-radius-topright: 10px;
	-webkit-border-top-right-radius: 10px;
	border-top-right-radius: 10px;
}

.sections-section-bottom {
	border-bottom-width: 1px;
	-moz-border-radius-bottomleft: 10px;
	-webkit-border-bottom-left-radius: 10px;
	border-bottom-left-radius: 10px;
	-moz-border-radius-bottomright: 10px;
	-webkit-border-bottom-right-radius: 10px;
	border-bottom-right-radius: 10px;
}

.sections-top-shadow {
	width: 500px;
	height: 1px;
	position: absolute;
	top: 21px;
	left: 21px;
	height: 30px;
	border-top: 1px solid #e4e4e4;
	-moz-border-radius-topleft: 10px;
	-webkit-border-top-left-radius: 10px;
	border-top-left-radius: 10px;
	-moz-border-radius-topright: 10px;
	-webkit-border-top-right-radius: 10px;
	border-top-right-radius: 10px;
}

.settings-label {
	font-weight: bold;
	font-family: Sans-Serif;
	font-size: 14px;
	margin-left: 14px;
	margin-top: 15px;
	float: left;
}

.settings-melody {
	color: #385487;
	font-family: Sans-Serif;
	font-size: 14px;
	display: inline-block;
	margin-top: 7px;
}

.settings-setter {
	float: right;
	margin-right: 14px;
	margin-top: 8px;
}

.events-container {
	margin-left: 20px;
}

#theme {
	margin-left: 20px;
	margin-bottom: 20px;
}
</style>
</head>
<body class='default'>
	<%
	ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
	Iterator<ROLAP.Slice> slices = proc.getQueryDimensions().iterator();
	
%>
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
</body>
</html>