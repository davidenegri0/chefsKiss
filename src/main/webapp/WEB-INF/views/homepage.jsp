<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 09/08/2023
  Time: 16:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String utente = (String)request.getAttribute("user");


%>
<html lang="it">
<head>
    <title>Homepage</title>
</head>
<body>
    <h1>Welcome to Chef's Kiss!!</h1>
    <%if(utente!=null){%>
    <p>Bentornato <%=utente%>!</p>
    <%}%>
    <nav>
        <ul>
            <%if(utente==null){%>
            <li><a href="http://localhost:8080/login">Login</a></li>
            <li><a href="http://localhost:8080/registration">Registrazione</a></li>
            <%}else{%>
            <li><a href="http://localhost:8080/homepage">Logout</a></li>
            <%}%>
        </ul>
    </nav>
</body>
</html>
