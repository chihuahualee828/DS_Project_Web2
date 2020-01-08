<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>

<%@page import="javaPackage.Score" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body bgcolor="#00CCFF">
<%

String imagePath=request.getParameter("file");
//out.println(imagePath);
TreeMap<Integer,List<String>> finalMap=(TreeMap<Integer,List<String>>)request.getSession().getAttribute("final");
//HashMap<String,String> queryHashMap=(HashMap<String,String>)request.getSession().getAttribute("query");

//List<Score> scoreList=(List<Score>)request.getSession().getAttribute("scoreList");

//for(Score each: scoreList) {
		
//		String link=queryHashMap.get(each.getTitleString());
//		out.println(each.getTitleString()+"<br />");
//		out.println(link+"<br />");
//		out.println("<br />");
//}


for (Map.Entry<Integer, List<String>> entry : finalMap.entrySet()) {%>     
	<a href='<%= entry.getValue().get(1) %>'><%= entry.getValue().get(0) %></a><h style="font-size:15px ;"></h><br>
	<a href='<%= entry.getValue().get(1) %>'><%= entry.getValue().get(1) %></a><h style="font-size:15px ;"></h><br><br><br>
	<p />


<%
//			out.println(entry.getValue().get(0)+"<br />");
//        	out.println("<a href>"+entry.getValue().get(1)+"</a><br />");
//        	out.println("<br />");
        	
        
//        out.println("<br /><br />");
//        out.println("<p />");
    }


%>



</body>
</html>