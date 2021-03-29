<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" import="java.io.*,java.net.*" pageEncoding="UTF-8"%>
<%
URL url = new URL(request.getParameter("getUrl"));
URLConnection connection = url.openConnection();
connection.setRequestProperty("CONTENT-TYPE", "application/xml");
BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
String inputLine;
String buffer = "";
while ((inputLine = in.readLine()) != null) {
	buffer += inputLine.trim();
}
System.out.println("buffer : " + buffer);
in.close();
/*
String url = request.getParameter("getUrl");
BufferedReader br = null;
try {
    HttpURLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();
    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    String line;
    final StringBuilder sb = new StringBuilder();
    while ((line = br.readLine()) != null) {
sb.append(line).append("\n");
    }

    //return sb.toString();
} catch (Exception e) {
    e.printStackTrace();
    //return null;
} finally {
    try {
if (br != null) br.close();
    } catch (IOException e) {
e.printStackTrace();
    }
}
*/
%><%=buffer%>
