<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 09/08/2023
  Time: 16:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String utente = (String)request.getAttribute("user");
    String ricetta1 = (String)request.getAttribute("ricetta1");
    String ricetta2 = (String)request.getAttribute("ricetta2");
    String ricetta3 = (String)request.getAttribute("ricetta3");
    String ricetta4 = (String)request.getAttribute("ricetta4");
%>
<html lang="it">
<head>
    <title>Homepage</title>
</head>
<body>
    <h1>Welcome to Chef's Kiss!!</h1>
    <%if(utente!=null){%>
    <p>Bentornato/a <%=utente%>!</p>
    <%}%>
    <nav>
        <ul>
            <%if(utente==null){%>
            <li><a href="http://localhost:8080/login">Login</a></li>
            <li><a href="http://localhost:8080/registration">Registrazione</a></li>
            <%}else{%>
            <li><a href="http://localhost:8080/logout">Logout</a></li>
            <li><a href="http://localhost:8080/profile">Il Mio Profilo</a></li>
            <%}%>
        </ul>
    </nav>
    <h3>Le 4 Ricette più recenti della settimana!</h3>
    <div id="recipeBlock">
        <p><%=ricetta1%></p>
    </div>
    <br>
    <div id="recipeBlock">
        <p><%=ricetta2%></p>
    </div>
    <br>
    <div id="recipeBlock">
        <p><%=ricetta3%></p>
    </div>
    <br>
    <div id="recipeBlock">
        <p><%=ricetta4%></p>
    </div>
</body>
</html>
