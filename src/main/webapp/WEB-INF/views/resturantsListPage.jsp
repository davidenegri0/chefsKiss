<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.modelObjects.Ristorante" %>
<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Sede" %>
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
    List<Sede> sedi = (List<Sede>)request.getAttribute("sedi");
    Boolean searched = (Boolean)request.getAttribute("searched");
%>
<html>
<head>
    <title>I Ristoranti</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="/img/chef'skiss_logo_emoji.png">
    <link href='https://fonts.googleapis.com/css?family=Bagel Fat One' rel='stylesheet'>
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
<body class="food_background">

    <div class="container-fluid bg-dark text-light py-2">
        <h1 class="text-center fancy_font">I RISTORANTI CHE CI PIACCIONO!</h1>
    </div>

    <%@include file="repetedElements/navabar_inc.jsp"%>

    <div class="card m-3 p-3 white_transp_background shadow">
        <h3>Stai cercando dove mangiare oggi? Fai una ricerca diretta!</h3>
        <div class="container m-3">
            <form id="searchForm">
                <label class="form-label" for="searchType">Cerca per:</label>
                <select class="form-select" id="searchType" name="searchType">
                    <option value="1">Nome del ristorante</option>
                    <option value="2">Città</option>
                    <!--<option value="2">Tag</option>-->
                </select>
                <br>
                <div class="input-group">
                    <input class="form-control" id="search" type="search" placeholder="Search.." name="search">
                    <button class="btn btn-secondary" type="submit"><i class='bx bx-search-alt-2 bx-sm'></i></button>
                </div>
            </form>
        </div>
    </div>

    <!-- Visualizzazione risultati di ricerca per città (visualizza sedi cercate e ristoranti proposti) -->
    <%      if (searched && sedi != null){    %>
    <div class="container-fluid py-3 px-0">
        <div class="container-fluid bg-dark text-light py-3">
            <h3>I risultati della tua ricerca</h3>
        </div>
    </div>
    <%
        for (int i = 0; i < sedi.size(); i++) {//System.out.println(sedi.get(i).getVia() + sedi.get(i).getCitta());
    %>

        <div class="d-flex card m-3 white_transp_background justify-content-center shadow" id="sediBlock">
            <div class="card-body justify-content-center">
                <p class="text-decoration-none text-center">
                    <a class="text-decoration-none" href="/sede?id=<%=sedi.get(i).getCoordinate()%>">
                        <%=sedi.get(i).getVia()%>, <%=sedi.get(i).getCitta()%>
                    </a>
                    -
                    <a class="text-decoration-none" href="/restaurant?id=<%=sedi.get(i).getRistoranteS().getID_Ristorante()%>">
                        <%= sedi.get(i).getRistoranteS().getNome() %>
                    </a>
                    <br>
                    <img src="<%=sedi.get(i).getStarsRating()%>" width="140px" height="auto" alt="Immagine rating">
                </p>
            </div>

        </div>

    <%}}%>


    <!-- Visualizza solo i ristoranti cercati -->
    <div class="container-fluid px-0">
        <div class="container-fluid bg-dark text-light py-3">
            <%      if (searched && sedi == null){    %>
            <h3>I risultati della tua ricerca</h3>
            <%}else{%>
            <h3>Oppure prova uno di questi locali:</h3>
            <%}%>
        </div>
    </div>

    <div class="container white_transp_background p-2 rounded-bottom" style="max-width: fit-content">
        <h5>Ordina per: </h5>
        <div class="btn-group align-content-center justify-content-center">
            <button class="btn btn-secondary" onclick="orderBy(1)">Nome</button>
            <%      if (searched && sedi != null){    %>
            <button class="btn btn-secondary" onclick="orderBy(2)">Valutazione</button>
            <%}%>
        </div>
    </div>

    <div class="d-flex p-3 text-dark justify-content-around flex-wrap">
        <% for (int i = 0; i < Math.min(10, ristoranti.size()); i++) { %>
            <div class="d-flex card m-3 white_transp_background justify-content-center shadow" style="width: 320px">
                <a class="text-decoration-none" href="/restaurant?id=<%=ristoranti.get(i).getID_Ristorante()%>">
                    <div class="card-body justify-content-center">
                        <p class="card-text h5 text-dark text-center"><%=ristoranti.get(i).getNome()%></p>
                    </div>
                </a>
            </div>
        <% } %>
    </div>

    <%@include file="repetedElements/backLink.jsp"%>
</body>
<script>
    function orderBy(type){
        let url = window.location.href;
        console.log(url);
        let href = new URL(url);
        href.searchParams.set("ord", type.toString());
        window.location.replace(href.toString());
    }
</script>
</html>
