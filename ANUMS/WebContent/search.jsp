<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="head.html" %>
<link href="css/theme" rel="stylesheet" type="text/css" media="screen">
<script language="Javascript">
var word;
var pageIndex;
var source;
var method;
window.onload=function()
{
	word=getQueryParameter("query");
	pageIndex=getQueryParameter("page");
	method=getQueryParameter("method");
	if(word!=null&&word!=""&&word!="null")
		xmlhttpPost("fetch.jsp",false);
}
function getQueryParameter ( parameterName ) {
	  var queryString = window.top.location.search.substring(1);
	  var parameterName = parameterName + "=";
	  if ( queryString.length > 0 ) {
	    begin = queryString.indexOf ( parameterName );
	    if ( begin != -1 ) {
	      begin += parameterName.length;
	      end = queryString.indexOf ( "&" , begin );
	        if ( end == -1 ) {
	        end = queryString.length
	      }
	      return unescape ( queryString.substring ( begin, end ) );
	    }
	  }
	  return "null";
	}
function xmlhttpPost(strURL,fromform) {
var xmlHttpReq = false;
var self = this;
// Mozilla/Safari
if (window.XMLHttpRequest) {
self.xmlHttpReq = new XMLHttpRequest();
}
// IE
else if (window.ActiveXObject) {
self.xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
}
self.xmlHttpReq.open('POST', strURL, true);
self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
document.getElementById("loader").setAttribute("class","loader");
self.xmlHttpReq.onreadystatechange = function() {
if (self.xmlHttpReq.readyState == 4) {
updatepage(self.xmlHttpReq.responseText);
}
}
/* if(fromform==true)
	self.xmlHttpReq.send(getquerystring());
else */
	self.xmlHttpReq.send(buildquery());
}
function buildquery()
{
	qstr = 'query=' + escape(word); // NOTE: no '?' before querystring
	qstr=qstr+"&page="+escape(pageIndex)+"&method="+method;
	return qstr;
}
function getquerystring() {
var form = document.forms['f1'];
var word = form.word.value;
qstr = 'query=' + escape(word); // NOTE: no '?' before querystring
return qstr;
}
function updatepage(str){
	document.getElementById("loader").removeAttribute("class");
document.getElementById("result").innerHTML = str;
}
</script>
<%@ include file="/css/loader.css" %>
</head>
<body>
<%@include file="banner.html" %>

<%@ include file="footer.html" %>
</body>
</html>
