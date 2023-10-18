<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.project.chefskiss.modelObjects.Ingrediente" %><%--
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
    Boolean searched = (Boolean)request.getAttribute("searched");
    List<String> allergeni = (List<String>) request.getAttribute("allergeni");
    List<Ingrediente> ingredienti = (List<Ingrediente>) request.getAttribute("ingredienti");
%>
<html>
<head>
    <title>La nostra lista di Piatti</title>
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
<script>
    var i = 0;
    function addAllergene(){
        if(i<5){
            var allergeneDiv = document.getElementById("allergene_div");

            if(i==0){
                var allergeneTag = document.createElement("p");
                allergeneTag.setAttribute("class", "my-3")
                var text = document.createTextNode("Elenco allergeni:");
                allergeneTag.append(text);
                allergeneDiv.append(allergeneTag);
                var undoButton = document.getElementById("remove_allergene");
                undoButton.hidden = false;
            }

            var allergeneNode = document.createElement("input");
            allergeneNode.setAttribute("name", "allergeni");
            allergeneNode.setAttribute("list", "allergeni_opt");
            allergeneNode.setAttribute("class", "form-control");
            allergeneDiv.appendChild(allergeneNode);
            i++;

        }
    }

    function removeAllergene() {
        if (i > 0) {
            var allergeneDiv = document.getElementById("allergene_div");
            allergeneDiv.lastChild.remove();
            if (i==1){
                allergeneDiv.lastChild.remove();
                var undoButton = document.getElementById("remove_allergene");
                undoButton.hidden = true;
            }
            i--;
        }
    }

    function inputOptions() {
        var searchType = document.getElementById("searchType").value;
        console.log('Cambiamento in ' + searchType);
        var campoInput = document.getElementById("search");
        if (searchType==2){
            campoInput.setAttribute("list", "ingredienti_opt");
        } else {
            campoInput.removeAttribute("list");
        }
    }

    function orderBy(type){
        let url = window.location.href;
        console.log(url);
        let href = new URL(url);
        href.searchParams.set("ord", type.toString());
        window.location.replace(href.toString());
    }

</script>
<body class="food_background">

    <div class="container-fluid bg-dark text-light py-2">
        <h1 class="text-center fancy_font">LA NOSTRA LISTA DEI PIATTI</h1>
    </div>
    <%@include file="repetedElements/navabar_inc.jsp"%>
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

        <div class="container m-3">
            <h3>Voglia di qualcosa in particolare? Cerca ciò che più ti fa gola!</h3>
            <form id="searchForm">
                <label class="form-label" for="searchType">Cerca per:</label>
                <select class="form-select" id="searchType" name="searchType" onchange="inputOptions()">
                    <option value="1">Nome del piatto</option>
                    <option value="2">Ingrediente</option>
                </select>
                <br>
                <div class="input-group" id="campoInput">
                    <input class="form-control" id="search" type="search" placeholder="Search.." name="search">
                    <button class="btn btn-secondary" type="submit"><i class='bx bx-search-alt-2 bx-sm'></i></button>
                </div>
                <datalist id="ingredienti_opt">
                    <% for (int i = 0; i < ingredienti.size(); i++) { %>
                    <option value="<%=ingredienti.get(i).getNome()%>"><%=ingredienti.get(i).getNome()%></option>
                    <% } %>
                </datalist>
                <div class="container my-3 px-0">
                    <div id="allergene_div">
                        <datalist id="allergeni_opt">
                            <% for (int i = 0; i < allergeni.size(); i++) {%>
                            <option value="<%=allergeni.get(i)%>"><%=allergeni.get(i)%></option>
                            <% } %>
                        </datalist>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-warning" id="add_allergene" type="button" onclick="addAllergene()">Aggiungi un allergene</button>
                        <button class="btn btn-danger" id="remove_allergene" type="button" onclick="removeAllergene()" hidden><i class='bx bx-undo bx-sm'></i></button>
                    </div>
                </div>
           </form>
        </div>
    </div>


    <div class="container-fluid py-3 px-0">
        <div class="container-fluid bg-dark text-light py-3">
            <%      if (searched){    %>
            <h3>I risultati della tua ricerca</h3>
            <%      } else {    %>
            <h3>Oppure, se hai solo voglia di cucinare, lasciati ispirare da una di queste ricette...</h3>
            <%      }       %>
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
                        <img class="card-img-top mx-auto d-block" style="height: auto; width: 256px" src="data:image/jpeg;base64,<%=Base64.getEncoder().encodeToString(piatti.get(i).getImmaginePiatto().getBytes(1, (int)piatti.get(i).getImmaginePiatto().length()))%>" alt="Foto piatto <%=piatti.get(i).getNome()%>">
                        <p class="card-text h5 text-dark text-center"><%=piatti.get(i).getNome()%></p>
                        <img class="mx-auto d-block" src="<%=piatti.get(i).getStarsRating()%>" width="140px" height="auto" alt="Immagine rating">
                    </div>
                </a>
            </div>
            <%}%>
        </div>
    </div>
    <%@ include file="repetedElements/backLink.jsp"%>
</body>
</html>
