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
            <h2 class="text-center">SELEZIONA GLI CHEF!</h2>
            <h4 class="text-center">Aggiungi nuovi dipendenti alla sede</h4>
        </div>
        <div class="card-body">
            <div class="container my-2">
                <div class="row">
                    <p>Ecco un elenco di chef iscritti alla piattaforma che non hanno un'occupazione:</p>
                    <% for (int i = 0; i < chefs.size(); i++) { %>
                    <div class="col-md-4 mb-3">
                        <div class="card">
                            <div class="card-body text-center">
                                <div class="card-title">
                                    <h5><%=chefs.get(i).getNome()+" "+chefs.get(i).getCognome()%></h5>
                                </div>
                                <p class="card-text">
                                    <br>
                                    <img src="/chef/<%=chefs.get(i).getCF()%>/image" height="100px" width="auto">
                                    <br><br>
                                    <a href="/chef/<%=chefs.get(i).getCF()%>/CV" download>
                                        <button class="btn btn-primary">Scarica il curriculum</button>
                                    </a>
                                </p>
                            </div>
                            <div class="card-footer">
                                <a href="/addChef?Coord=<%=Coord%>&CF=<%=chefs.get(i).getCF()%>"><button class="btn btn-success">Aggiungi</button></a>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
            <%@include file="repetedElements/backLink.jsp"%>
        </div>
    </div>
</body>
</html>