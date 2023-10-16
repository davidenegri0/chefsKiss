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
    Ristorante ristorante = (Ristorante) request.getAttribute("ristorante");
%>
<html>
<head>
    <title>Modifica questa sede!</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<style>
    .food_background{
        background-image: url("img/food_background_v2.jpg");
        background-size: contain;
    }

    .white_transp_background{
        background-color: rgba(245,245,245,0.7);
    }
</style>
<body class="food_background">
    <div class="card m-3 white_transp_background shadow">
        <div class="card-header">
            <h3>MODIFICA LA SEDE DEL TUO RISTORANTE!</h3>
            <h5><%=ristorante.getNome()%></h5>
        </div>
        <div class="card-body">
            <form method="post">
                <label class="form-label" for="via">Via e numero civico del locale: </label>
                <input class="form-control" type="text" id="via" name="via" value="<%=sede.getVia()%>" readonly>
                <label class="form-label" for="citta">Città: </label>
                <input class="form-control" type="text" id="citta" name="citta" value="<%=sede.getCitta()%>" readonly>
                <label class="form-label" for="nposti">Numero di posti a sedere: </label>
                <input class="form-control" type="number" id="nposti" name="nposti" value="<%=sede.getPosti()%>" required><br>
                <input type="submit" class="btn btn-success my-2" style="border-radius: 10px" value="Conferma">
                <input type="reset" class="btn btn-danger my-2" style="border-radius: 10px" value="Annulla" onclick=window.history.back()>
            </form>
        </div>
    </div>

    <%@include file="repetedElements/backLink.jsp"%>
</body>
</html>
