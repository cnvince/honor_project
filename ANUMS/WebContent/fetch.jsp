<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.broker.Controller"%>
<%@page import="com.results.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.datatype.*"%>
<%@page import="com.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String query = request.getParameter("query");
	int pageSize = 15;
	String cpage = request.getParameter("page");
	int PageIndex = 1;
	int imethod=0;
	if (cpage != null && !cpage.equals("null") && cpage != "")
		PageIndex = Integer.parseInt(cpage);
	ArrayList<Result> results = new ArrayList<Result>();
	if (cpage == null || cpage == ""||cpage.equals("null")) {
		Controller controller = new Controller();
		String method=request.getParameter("method");
		/* System.out.println(method); */
		
		if(method!=null)
			imethod=Integer.parseInt(method);
		results = controller.fetchResult(query, imethod);
		session.setAttribute("result", results);
	} else
	{
		results = (ArrayList<Result>) session.getAttribute("result");
	}
		System.out.println("result size:"+results.size());
	int totalPage = results.size() / pageSize + 1;
%>
<p id="fb-matching" class="">
	<span class="fb-result-count" id="fb-page-start">Page: <%=PageIndex%></span>
	of <span class="fb-result-count" id="fb-total-matching"><%=results.size()%></span>
	search results for <strong><%=query%> By Algorithm:<%=TermMapping.toMethod(imethod) %></strong>
</p>

<ol id="fb-results">

	<!-- EACH RESULT -->
	<!-- Padre error status: 0 -->
	<%
		for (int j = pageSize * (PageIndex - 1); j < pageSize * (PageIndex); j++) {
			if (j >= results.size())
				continue;
			Result result = results.get(j);
			int source = result.getSource();
	%>

	<li>
		<h3>
			<a href="<%=result.getLink()%>" title="<%=result.getLink()%>"> <b><%=result.getTitle()%></b>
			</a>
		</h3>
		<%
				if (!result.getImgLink().trim().equals(""))
				{
			%>
			<a href="<%=result.getLink()%>"><img alt="<%=result.getLink()%>"
				src="<%=result.getImgLink()%>"></a>
			<%
				}
			%>
		<p>
			<span class="fb-result-summary"><%=result.getDisplaySummary() %></span>
		<p>
			<cite>Retrieval From:<%=result.getSourceName()%></cite>
		</p> <span class="tagging"> </span>
	</li>
	<%
		}
	%>
</ol>

<p class="fb-page-nav">
	<span class="fb-page-nav"> <%
 	for (int i = 1; i <= totalPage; i++) {
 		if (i == PageIndex) {
 %> <%=i%> <%
 	} else {
 %> <a
		href="search.jsp?query=<%=query%>&page=<%=i%>" class="fb-page-nav"><%=i%></a>
		<%
			}
			}
		%>
	</span>
</p>