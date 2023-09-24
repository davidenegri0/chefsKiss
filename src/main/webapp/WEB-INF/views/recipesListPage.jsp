<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 24/09/2023
  Time: 19:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User utente = (User)request.getAttribute("user");
    List<Piatto> piatti = (List<Piatto>)request.getAttribute("listaPiatti");
%>
<html>
<head>
    <title>La nostra lista di Piatti</title>
</head>
<body>
    <h1>LA NOSTRA LISTA DEI PIATTI</h1>
    <h3>Voglia di qualcosa in particolare? Cerca ciò che più ti fa gola!</h3>
    <p>QUI CI SARA' LA SEARCH BAR (DA IMPLEMENTARE)</p>
    <h3>Oppure, se hai solo voglia di cucinare, lasciati ispirare da una di queste ricette...</h3>
    <%for (int i = 0; i < piatti.size(); i++) {%>
            <div id="recipeBlock">
                <p><%=piatti.get(i).getNome()%></p>
                <img src="<%=piatti.get(i).getStarsRating()%>" width="20%" height="auto">
            </div>
    <%}%>
</body>
</html>
