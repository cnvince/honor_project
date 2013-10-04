<!DOCTYPE html>
<html>
<head>
<script src="js/jquery-1.9.1.js">
</script>
<script>
$(document).ready(function(){
  $("input").click(function(){
   /*  $(this).hide(); */
   $(this).attr('value','yes');
  });
});
</script>
</head>
<body>
<p>If you click on me, I will disappear.</p>
<p>Click me away!</p>
<p>Click me too!</p>
<%
	session.setAttribute("test", "the session works");
%>
<form action="test2.jsp">
<input type="submit" value="go">
</form>
<input type="text" id="t1" value="hello">
</body>
</html>