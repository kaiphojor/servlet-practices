<%@ page language="java" import="java.net.*, java.io.*" pageEncoding="UTF-8"%><%
URL url = new URL(request.getParameter("getUrl"));
URLConnection connection = url.openConnection();
connection.setRequestProperty("CONTENT-TYPE", "text/plain");
BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
//BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

String inputLine;
String buffer = "";
while ((inputLine = in.readLine()) != null) {
	buffer += inputLine.trim();
}

/*
String inputLine;
final StringBuilder buffer = new StringBuilder();
while ((inputLine = in.readLine()) != null) {
	buffer.append(inputLine).append("\n");
}
*/
//System.out.println("buffer : " + buffer);
in.close();
if(request.getParameter("getUrl").contains("json")){
	response.setContentType("application/json");	
}else{
	response.setContentType("application/xml");	
}
out.print(buffer);
/*
import="java.io.*,java.net.*" 
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
%>
