<%@ page import="java.util.ArrayList" %>
<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %><%--
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
    <link href='https://fonts.googleapis.com/css?family=Bagel Fat One' rel='stylesheet'>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<style>
    .fancy_font{
        font-family: 'Bagel Fat One';
        font-size: 64px;
    }
    .food_background{
        background-image: url("img/food_background_v2.jpg");
        background-size: contain;
    }

    .white_transp_background{
        background-color: rgba(245,245,245,0.7);
    }
</style>
<body class="food_background">
    <div class="container-fluid p-3 text-white" style="background-color: darkgreen">
        <h1 class="text-center fancy_font">Welcome to Chef's Kiss!!</h1>
    </div>

    <%@include file="repetedElements/navabar_inc.jsp"%>
    <%if(utente!=null){%>
    <div class="bg-dark p-2">
        <p class="h5 text-center text-light">Bentornato/a <%=utente.getNome()+" "+utente.getCognome()%>!</p>
    </div>
    <%}%>

    <div class="container-fluid text-dark p-3 white_transp_background">
        <h3>Le 4 Ricette più recenti della settimana!</h3>
    </div>

    <div class="row p-3 mx-0">
        <div class="col card mx-auto white_transp_background" style="max-width: 450px">
            <a class="text-decoration-none" href="/plate?id=<%=piatti.get(0).getId()%>">
                <div class="card-body" id="recipeBlock1">
                    <img class="card-img-top mx-auto d-block" style="height: 256px; width: auto" src="data:image/jpeg;base64,<%=Base64.getEncoder().encodeToString(piatti.get(0).getImmaginePiatto().getBytes(1, (int)piatti.get(0).getImmaginePiatto().length()))%>">
                    <p class="card-text h5 text-dark text-center"><%=piatti.get(0).getNome()%></p>
                    <img class="mx-auto d-block" src="<%=piatti.get(0).getStarsRating()%>" width="140px" height="auto">
                </div>
            </a>
        </div>
        <div class="col card mx-auto white_transp_background" style="max-width: 450px">
            <a class="text-decoration-none" href="/plate?id=<%=piatti.get(1).getId()%>">
                <div class="card-body" id="recipeBlock2">
                    <img class="card-img-top mx-auto d-block" style="height: 256px; width: auto" src="data:image/jpeg;base64,<%=Base64.getEncoder().encodeToString(piatti.get(1).getImmaginePiatto().getBytes(1, (int)piatti.get(1).getImmaginePiatto().length()))%>">
                    <p class="card-text h5 text-dark text-center"><%=piatti.get(1).getNome()%></p>
                    <img class="mx-auto d-block" src="<%=piatti.get(1).getStarsRating()%>" width="140px" height="auto">
                </div>
            </a>
        </div>
    </div>
    <div class="row p-3 mx-0">
        <div class="col card mx-auto white_transp_background" style="max-width: 450px">
            <a class="text-decoration-none" href="/plate?id=<%=piatti.get(2).getId()%>">
                <div class="card-body" id="recipeBlock3">
                    <img class="card-img-top mx-auto d-block" style="height: 256px; width: auto" src="data:image/jpeg;base64,<%=Base64.getEncoder().encodeToString(piatti.get(2).getImmaginePiatto().getBytes(1, (int)piatti.get(2).getImmaginePiatto().length()))%>">
                    <p class="card-text h5 text-dark text-center"><%=piatti.get(2).getNome()%></p>
                    <img class="mx-auto d-block" src="<%=piatti.get(2).getStarsRating()%>" width="140px" height="auto">
                </div>
            </a>
        </div>
        <div class="col card mx-auto white_transp_background" style="max-width: 450px">
            <a class="text-decoration-none" href="/plate?id=<%=piatti.get(3).getId()%>">
                <div class="card-body" id="recipeBlock4">
                    <img class="card-img-top mx-auto d-block" style="height: 256px; width: auto" src="data:image/jpeg;base64,<%=Base64.getEncoder().encodeToString(piatti.get(3).getImmaginePiatto().getBytes(1, (int)piatti.get(3).getImmaginePiatto().length()))%>">
                    <p class="card-text h5 text-dark text-center"><%=piatti.get(3).getNome()%></p>
                    <img class="mx-auto d-block" src="<%=piatti.get(3).getStarsRating()%>" width="140px" height="auto">
                </div>
            </a>
        </div>
    </div>
</body>
</html>
