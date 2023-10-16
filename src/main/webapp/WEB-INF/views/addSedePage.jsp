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
    Ristorante Risto = (Ristorante)request.getAttribute("Risto");
%>
<html>
<head>
    <title>Aggiungi una sede!</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
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
</style>
<body class="food_background">
    <div class="card m-3 white_transp_background shadow">
        <div class="card-header">
            <h3>AGGIUNTI UNA SEDE AL TUO RISTORANTE!</h3>
            <h5><%=Risto.getNome()%></h5>
        </div>
        <div class="card-body">
            <form method="post">
                <label class="form-label" for="via">Via del locale: </label>
                <input class="form-control" type="text" id="via" name="via" required>
                <label class="form-label" for="n_civ">Numero civico: </label>
                <input class="form-control" type="number" id="n_civ" name="n_civ" required>
                <label class="form-label" for="citta">Città: </label>
                <input class="form-control" type="text" id="citta" name="citta" required>
                <label class="form-label" for="nposti">Numero di posti a sedere: </label>
                <input class="form-control" type="number" min="1" id="nposti" name="nposti" required>
                <input type="hidden" id="idristo" name="idristo" value="<%=Risto.getID_Ristorante()%>">
                <input type="hidden" id="coord" name="coord" value="00.000;00.000">
                <br>
                <input type="button" class="btn btn-success my-2" style="border-radius: 10px" onclick="calculateANDsubmit()" value="Conferma">
                <input type="reset" class="btn btn-danger my-2" style="border-radius: 10px" value="Annulla" onclick=window.history.back()>
            </form>
        </div>
    </div>

    <%@include file="repetedElements/backLink.jsp"%>
</body>
<script>
    async function calculateANDsubmit(){
        let via = document.getElementById("n_civ").value+" "+document.getElementById("via").value;
        via = via.replaceAll(" ","+");
        let citta = document.getElementById("citta").value;
        //console.log(via+" "+citta);
        let url = "https://geocode.maps.co/search?street="+via+"&city="+citta;
        let result = await fetch(url);
        let coordJson = await result.json()
        coord = coordJson[0].lat+";"+coordJson[0].lon;
        //console.log(coord);
        document.getElementById("coord").value = coord;
        document.querySelector("form").submit();
    }
</script>
</html>
