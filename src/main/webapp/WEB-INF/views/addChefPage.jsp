<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 05/10/2023
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
<%
    User utente = (User)request.getAttribute("user");
    List<User> chefs = (List<User>)request.getAttribute("chefs");
    String Coord = (String)request.getAttribute("CoordSede");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new employees</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
    <h3>Aggiungi nuovi dipendenti alla sede</h3>
    <p>Ecco un elenco di chef iscritti alla piattaforma che non hanno un'occupazione:</p>
    <% for (int i = 0; i < chefs.size(); i++) { %>
    <div>
        <p><%=chefs.get(i).getNome()+" "+chefs.get(i).getCognome()%></p>
        <img src="/chef/<%=chefs.get(i).getCF()%>/image" height="100px" width="auto">
        <a href="/chef/<%=chefs.get(i).getCF()%>/CV" download>
            <button>Scarica il curriculum</button>
        </a>

    </div>
    <a href="/addChef?Coord=<%=Coord%>&CF=<%=chefs.get(i).getCF()%>"><button>Aggiungi</button></a>
    <% } %>
    <%@include file="repetedElements/backLink.jsp"%>
</body>
</html>