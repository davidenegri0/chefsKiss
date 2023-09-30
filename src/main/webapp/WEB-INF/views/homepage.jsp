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
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<body>
    <h1>Welcome to Chef's Kiss!!</h1>
    <%if(utente!=null){%>
    <p>Bentornato/a <%=utente.getNome()+" "+utente.getCognome()%>!</p>
    <%}%>
    <nav>
        <ul>
            <%if(utente==null){%>
            <li><a href="/login">Login</a></li>
            <li><a href="/registration">Registrazione</a></li>
            <%}else{%>
            <li><a href="/logout">Logout</a></li>
            <li><a href="/profile">Il Mio Profilo</a></li>
            <%}%>
            <li><a href="/recipesView">Le ricette di Chef's Kiss</a></li>
            <li><a href="/resturantsList">I Ristoranti</a></li>
        </ul>
    </nav>
    <h3>Le 4 Ricette più recenti della settimana!</h3>
    <a href="/plate?id=<%=piatti.get(0).getId()%>">
        <div id="recipeBlock1">
            <p><%=piatti.get(0).getNome()%></p>
            <img src="<%=piatti.get(0).getStarsRating()%>" width="140px" height="auto">
        </div>
    </a>
    <a href="/plate?id=<%=piatti.get(1).getId()%>">
        <div id="recipeBlock2">
            <p><%=piatti.get(1).getNome()%></p>
            <img src="<%=piatti.get(1).getStarsRating()%>" width="140px" height="auto">
        </div>
    </a>
    <a href="/plate?id=<%=piatti.get(2).getId()%>">
        <div id="recipeBlock3">
            <p><%=piatti.get(2).getNome()%></p>
            <img src="<%=piatti.get(2).getStarsRating()%>" width="140px" height="auto">
        </div>
    </a>
    <a href="/plate?id=<%=piatti.get(3).getId()%>">
        <div id="recipeBlock4">
            <p><%=piatti.get(3).getNome()%></p>
            <img src="<%=piatti.get(3).getStarsRating()%>" width="140px" height="auto">
        </div>
    </a>
</body>
</html>
