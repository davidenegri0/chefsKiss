<%@ page import="java.util.ArrayList" %>
<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 09/08/2023
  Time: 16:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User utente = (User)request.getAttribute("user");
    List<Piatto> piatti = (List<Piatto>)request.getAttribute("listaPiatti");
%>
<html lang="it">
<head>
    <title>Homepage</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
    <div class="container-fluid p-3 text-white" style="background-color: darkgreen">
        <h1 class="text-center">Welcome to Chef's Kiss!!</h1>
    </div>

    <%@include file="repetedElements/navabar_inc.jsp"%>

    <%if(utente!=null){%>
    <p>Bentornato/a <%=utente.getNome()+" "+utente.getCognome()%>!</p>
    <%}%>

    <h3>Le 4 Ricette più recenti della settimana!</h3>
    <div class="row p-3">
        <div class="col">
            <a href="/plate?id=<%=piatti.get(0).getId()%>">
                <div id="recipeBlock1">
                    <p><%=piatti.get(0).getNome()%></p>
                    <img src="<%=piatti.get(0).getStarsRating()%>" width="140px" height="auto">
                </div>
            </a>
        </div>
        <div class="col">
            <a href="/plate?id=<%=piatti.get(1).getId()%>">
                <div id="recipeBlock2">
                    <p><%=piatti.get(1).getNome()%></p>
                    <img src="<%=piatti.get(1).getStarsRating()%>" width="140px" height="auto">
                </div>
            </a>
        </div>
    </div>
    <div class="row p-3">
        <div class="col">
            <a href="/plate?id=<%=piatti.get(2).getId()%>">
                <div id="recipeBlock3">
                    <p><%=piatti.get(2).getNome()%></p>
                    <img src="<%=piatti.get(2).getStarsRating()%>" width="140px" height="auto">
                </div>
            </a>
        </div>
        <div class="col">
            <a href="/plate?id=<%=piatti.get(3).getId()%>">
                <div id="recipeBlock4">
                    <p><%=piatti.get(3).getNome()%></p>
                    <img src="<%=piatti.get(3).getStarsRating()%>" width="140px" height="auto">
                </div>
            </a>
        </div>
    </div>
</body>
</html>
