<%@ page import="com.project.chefskiss.modelObjects.Ristorante" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.modelObjects.Sede" %>
<%@ page import="com.project.chefskiss.modelObjects.User" %><%--
  Created by IntelliJ IDEA.
  User: spipp
  Date: 26/09/2023
  Time: 14:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ristorante</title>
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

    ul {
        list-style-type: none;
    }

</style>
<body class="food_background">
    <%
        User utente = (User) request.getAttribute("user");
        Ristorante ristorante = (Ristorante) request.getAttribute("ristorante");
        List<Sede> sedi = (List<Sede>) request.getAttribute("sedi");
        User ristoratore = (User) request.getAttribute("ristoratore");
    %>
    <%@include file="repetedElements/navabar_inc.jsp"%>
    <div class="container-fluid my-3 mr-2 clearfix">
        <div class="card mx-auto white_transp_background shadow">
            <div class="card-body">

                <h1><%= ristorante.getNome()%></h1>
                <h3>Benvenuti nel nostro ristorante! </h3>

                <h4>Venite a trovarci qui! E scoprirete i nostri menù!</h4>
                <ul class="list-group">
                    <% if (utente!=null && ristoratore.getCF().equals(utente.getCF())){ %>
                    <a href="/addSede"><button class="btn btn-sm btn-success">Aggiungi una sede</button></a>
                    <% } %>
                    <% for ( int i = 0; i < sedi.size(); i++ ){ %>
                        <li>
                            <% if (utente!=null && ristoratore.getCF().equals(utente.getCF())){ %>
                            <a href="/editSede?idR=<%=ristorante.getID_Ristorante()%>&coord=<%=sedi.get(i).getCoordinate()%>"><button class="btn btn-sm btn-primary"><i class='bx bxs-edit-alt'></i></button></a>
                                <% if (sedi.size()>1){ %>
                                <a href="/deleteSede?idR=<%=ristorante.getID_Ristorante()%>&coord=<%=sedi.get(i).getCoordinate()%>"><button class="btn btn-sm btn-danger"><i class='bx bxs-trash'></i></button></a>
                                <% } %>
                            <% } %>
                            <div class="row">
                                <div class="col-4">
                                    <a href="/sede?id=<%= sedi.get(i).getCoordinate() %>" class="list-group-item list-group-item-action"><%= sedi.get(i).getVia()%> (<%=sedi.get(i).getCitta()%>)</a>
                                </div>
                            </div>
                        </li>
                    <% } %>
                </ul>
            </div>
        </div>
    </div>

    <div class="d-flex card m-3 white_transp_background justify-content-center shadow" id="reviewBlock">
        <div class="text-center">
            <h5>Questo ristorante è gestito da <%= ristoratore.getNome() %> <%= ristoratore.getCognome() %></h5>
            <div>Per richiedere informazioni scrivere a <i><%= ristoratore.getEmail()%></i> o contattare il numero <i><%=ristoratore.getN_Telefono()%></i></div>
        </div>
    </div>

    <%@include file="repetedElements/backLink.jsp"%>
</body>
</html>
