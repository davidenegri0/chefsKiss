<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Ristorante" %>
<%@ page import="com.project.chefskiss.modelObjects.Sede" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 03/10/2023
  Time: 16:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User utente = (User)request.getAttribute("user");
    Sede sede = (Sede)request.getAttribute("sede");
%>
<html>
<head>
    <title>Modifica questa sede!</title>
</head>
<body>
    <h3>Modifica la sede per il tuo ristorante:</h3>
    <form method="post">
        <label for="via">Via e numero civico del locale: </label>
        <input type="text" id="via" name="via" value="<%=sede.getVia()%>" readonly><br>
        <label for="citta">Città: </label>
        <input type="text" id="citta" name="citta" value="<%=sede.getCitta()%>" readonly><br>
        <label for="nposti">Numero di posti a sedere: </label>
        <input type="number" id="nposti" name="nposti" value="<%=sede.getPosti()%>" required><br>
        <input type="submit" value="Conferma">
        <input type="reset" value="Annulla">
    </form>
</body>
</html>
