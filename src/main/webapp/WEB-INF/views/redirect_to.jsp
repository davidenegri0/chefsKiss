<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 26/09/2023
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String URL = (String)request.getAttribute("url");
%>
<html lang="it">
<head>
  <title>Redirecting...</title>
</head>
<body>
You will be redirected...
<br>
<br>
If that doesn't happen, click <a href="http://localhost:8080/<%=URL%>">here</a>
</body>
<script>
  function onLoadHandler()
  {
    window.location.replace("http://localhost:8080<%=URL%>");
  }
  window.addEventListener("load", onLoadHandler)
</script>
</html>