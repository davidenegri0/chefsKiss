<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.modelObjects.Ristorante" %>
<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 30/09/2023
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User utente = (User) request.getAttribute("user");
    List<Ristorante> ristoranti = (List<Ristorante>)request.getAttribute("ristoranti");
%>
<html>
<head>
    <title>I Ristoranti</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<body>
    <h1>I Ristoranti che ci piacciono!</h1>
    <h3>Stai cercando dove mangiare oggi? Fai una ricerca diretta!</h3>
    <div>
        <form id="searchForm">
            <label for="searchType">Cerca per:</label>
            <select id="searchType" name="searchType">
                <option value="1">Nome del ristorante</option>
                <!--<option value="2">Tag</option>-->
            </select>
            <br>
            <input id="search" type="search" placeholder="Search.." name="search">
            <button type="submit"><i class='bx bx-search-alt-2 bx-sm'></i></button>
        </form>
    </div>

    NEXT : MAPPA

    <h3>Oppure prova uno di questi locali:</h3>
    <% for (int i = 0; i < Math.min(10, ristoranti.size()); i++) { %>
    <a href="/restaurant?id=<%=ristoranti.get(i).getID_Ristorante()%>">
        <div>
            <p><%=ristoranti.get(i).getNome()%></p>
        </div>
    </a>
    <% } %>
    <%@include file="repetedElements/homepageLink.html"%>
</body>
</html>