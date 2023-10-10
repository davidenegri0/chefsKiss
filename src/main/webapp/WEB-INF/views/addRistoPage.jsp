<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Ristorante" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 03/10/2023
  Time: 16:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User utente = (User)request.getAttribute("user");
%>
<html>
<head>
    <title>Aggiungi il tuo ristorante!</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
    <h3>Aggiungi i dati del tuo ristorante e della tua sede!</h3>
    <form method="post">
        <h5>Dati del ristorante</h5>
        <label for="nome_risto">Nome del ristorante</label>
        <input type="text" id="nome_risto" name="nome_risto" required>
        <h5>Dati della sede</h5>
        <label for="via">Via del locale: </label>
        <input type="text" id="via" name="via" required><br>
        <label for="citta">Città: </label>
        <input type="text" id="citta" name="citta" required><br>
        <label for="nposti">Numero di posti a sedere: </label>
        <input type="number" id="nposti" name="nposti" required><br>
        <input type="submit" value="Conferma">
        <input type="reset" value="Annulla" onclick=window.history.back()>
    </form>
    <%@include file="repetedElements/backLink.jsp"%>
</body>
</html>
