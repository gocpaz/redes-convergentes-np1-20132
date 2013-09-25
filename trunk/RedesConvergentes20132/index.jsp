<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SMNP Helper</title>
<script type="text/javascript" src="dwr/engine.js"></script>
<script type="text/javascript" src="dwr/util.js"></script>
<script type="text/javascript" src="dwr/interface/SMNPdwr.js"></script>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
function updateData(){
	dwr.engine.setActiveReverseAjax(true);
	setInterval(function(){
			SMNPdwr.getMessage(function(data){
				console.log(data);
				});
			},200);
}
</script>
</head>
<body>
<input type="button" value="MODA" onclick="updateData();"/>
<div id="s"></div>
</body>
</html>