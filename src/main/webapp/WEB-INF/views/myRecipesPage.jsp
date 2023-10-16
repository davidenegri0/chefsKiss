<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %><%--
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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Bagel Fat One' rel='stylesheet'>
    <link rel="icon" href="/img/chef'skiss_logo_emoji.png">
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

    .fancy_font{
        font-family: 'Bagel Fat One';
        font-size: 64px;
    }
</style>
<script>
    function orderBy(type){
        let url = window.location.href;
        console.log(url);
        let href = new URL(url);
        href.searchParams.set("ord", type.toString());
        window.location.replace(href.toString());
    }
</script>
<body class="food_background">
    <%@include file="repetedElements/navabar_inc.jsp"%>

    <div class="container-fluid bg-dark text-light py-2">
        <h1 class="text-center fancy_font">I TUOI PIATTI</h1>
    </div>
    <div class="card m-3 white_transp_background shadow">
        <% if(utente!= null && (utente.isPrivato() || utente.isChef())){ %>
        <div class="container row m-3">
            <div class="col-9 p-0">
                <h3><%=utente.getNome()+" "+utente.getCognome()%> vuoi condividere una tua creazione?</h3>
            </div>
            <div class="col-3">
                <a href="/addPlate"><button class="btn btn-success">Clicca qui</button></a>
            </div>
        </div>
        <% } %>
    </div>


    <div class="container-fluid py-3 px-0">
        <div class="container-fluid bg-dark text-light py-3">
            <h3>I tuoi piatti</h3>
        </div>

        <div class="container white_transp_background p-2 rounded-bottom" style="max-width: fit-content">
            <h5>Ordina per: </h5>
            <div class="btn-group">
                <button class="btn btn-secondary" onclick="orderBy(1)">Nome</button>
                <button class="btn btn-secondary" onclick="orderBy(2)">Valutazione</button>
                <button class="btn btn-secondary" onclick="orderBy(0)">Data upload</button>
            </div>
        </div>

        <div class="d-flex p-3 text-dark justify-content-around flex-wrap">
            <%for (int i = 0; i < piatti.size(); i++) {%>
            <div class="d-flex card m-3 white_transp_background justify-content-center shadow" style="width: 320px">
                <a class="text-decoration-none" href="/plate?id=<%=piatti.get(i).getId()%>">
                    <div class="card-body justify-content-center" id="recipeBlock">
                        <img class="card-img-top mx-auto d-block" style="height: auto; width: 256px" src="data:image/jpeg;base64,<%=Base64.getEncoder().encodeToString(piatti.get(i).getImmaginePiatto().getBytes(1, (int)piatti.get(i).getImmaginePiatto().length()))%>">
                        <p class="card-text h5 text-dark text-center"><%=piatti.get(i).getNome()%></p>
                        <img class="mx-auto d-block" src="<%=piatti.get(i).getStarsRating()%>" width="140px" height="auto">
                    </div>
                </a>
            </div>
            <%}%>
        </div>
    </div>

    <%@ include file="repetedElements/backLink.jsp"%>
</body>
</html>
