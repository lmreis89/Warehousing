<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="scripts/jquery-1.7.2.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	$.ajax({
		type : "GET",
		url : "DrillServlet",
		data : {
			dimID: 'D1',
			levelID: 'L4'
		},
		dataType: 'html',
		success: function(data){
			alert(data);
		}
	});
});
</script>
</body>
</html>