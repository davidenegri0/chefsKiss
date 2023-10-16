<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.dataAccessObjects.ContieneDAO" %>
<%@ page import="com.project.chefskiss.modelObjects.*" %>
<%@ page import="java.util.Base64" %>
<%--
  Created by IntelliJ IDEA.
  User: spipp
  Date: 24/09/2023
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Piatto</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="/img/chef'skiss_logo_emoji.png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<%
    User utente = (User)request.getAttribute("user");
    Piatto piatto = (Piatto) request.getAttribute("piatto_passato");
    List<Contiene> ingredienti = (List<Contiene>) request.getAttribute("ingredienti");
    //Integer id_piatto = (Integer) request.getAttribute("id_piatto");
    //String nome_piatto = (String) request.getAttribute("nome_piatto");
    User utente_post = (User) request.getAttribute("utente_post");
    List<Sede> sedi = (List<Sede>) request.getAttribute("sedi");
    List<Ristorante> ristoranti = (List<Ristorante>) request.getAttribute("ristoranti");
    List<Recensione> recensioni = (List<Recensione>) request.getAttribute("recensioni");
    List<User> utenti_recensori = (List<User>) request.getAttribute("utenti_recensori");
%>
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
    <%@include file="repetedElements/navabar_inc.jsp"%>
    <div class="container-fluid my-3 mr-3 clearfix">
        <div class="row">
            <div class="col-sm-6">
                <div class="card float-end mx-auto white_transp_background shadow" style="width: 100%; height: 100%">
                    <div class="card-body">
                        <h1><%=piatto.getNome()%></h1>
                        <p>
                            Caricato da: <%= utente_post.getNome() %> <%= utente_post.getCognome() %>
                        </p>
                        <% if(utente!=null && utente_post.getCF().equals(utente.getCF())){ %>
                        <a href="/editPlate?id=<%=piatto.getId()%>"><button class="btn btn-sm btn-primary">Modifica la tua ricetta</button></a>
                        <button class="btn btn-sm btn-danger" onclick="confermaCancellazionePiatto()">Cancella la tua ricetta</button>
                        <% } %>
                        <h3>Ingredienti</h3>
                        <div>Per 4 persone:</div>
                        <table>
                            <tr>
                                <th><i>Ingrediente</i></th><th><i>Quantità</i></th>
                            </tr>
                            <%
                                ContieneDAO contiene;
                                String ingrediente;
                                Integer quantita;

                                for (int i = 0; i < ingredienti.size(); i++) {
                                    ingrediente = ingredienti.get(i).getIngredienteC().getNome();
                                    quantita = (ingredienti.get(i).getQuantita())*4;

                            %>
                            <tr>
                                <td><%= ingrediente %></td>
                                <td><%= quantita %> gr</td>
                            </tr>
                            <%
                                }
                            %>
                        </table>

                        <!--
                            FC was here!
                        -->

                        <h3>Preparazione</h3>
                        <p>
                            <%= piatto.getPreparazione() %>
                        </p>

                        <h4>Si può trovare tra i menù dei seguenti ristoranti:</h4>
                        <nav>
                            <ul class="list-group">
                                <%
                                    Integer id_ristorante;

                                    for (int i = 0; i < sedi.size(); i++){
                                %>

                                <li class="d-flex justify-content-center align-items-center">
                                    <div class="container">
                                        <div class="row">
                                            <div class="col-md-auto">
                                                <a href = "/restaurant?id=<%= ristoranti.get(i).getID_Ristorante() %>" class="list-group-item list-group-item-action"><%= ristoranti.get(i).getNome() %></a>
                                            </div>
                                            <div class="col-md-auto">
                                                <a href = "/sede?id=<%= sedi.get(i).getCoordinate() %>" class="list-group-item list-group-item-action"><i class='bx bx-map'></i>  <%= sedi.get(i).getVia() %>, (<%= sedi.get(i).getCitta() %>)</a>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <% } %>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="card float-end mx-auto white_transp_background shadow" style="width: 100%; height: 100%">
                    <div class="card-body">
                        <img class="img-thumbnail shadow float-end w-100 h-auto" src="data:image/jpeg;base64,<%=Base64.getEncoder().encodeToString(piatto.getImmaginePiatto().getBytes(1, (int)piatto.getImmaginePiatto().length()))%>">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid p-2 bg-dark text-white text-center">
        <h3>Recensioni</h3>
    </div>

    <div class="container mx-auto">
        <div class="d-flex p-3 text-dark justify-content-around flex-wrap">
            <%
                String CF_utente, commento;
                Integer voto;


                for (int i = 0; i < recensioni.size(); i++){
                    CF_utente = recensioni.get(i).getUtenteR().getCF();
                    voto = recensioni.get(i).getVoto();
                    commento = recensioni.get(i).getCommento();
            %>
            <div class="d-flex card m-3 white_transp_background justify-content-center shadow" style="width: 256px" id="reviewBlock">
                <div class="text-center" id="reviewBlock_utente">
                    <%= utenti_recensori.get(i).getNome() %> <%= utenti_recensori.get(i).getCognome() %>
                    <%
                    if (utenti_recensori.get(i).isClienteVerificato()){
                    %>
                    <i class='bx bx-badge-check' style='color:#1c00ff' ></i>
                    <% } %>
                </div>
                <div id="reviewBlock_voto">
                    <img class="mx-auto d-block" src="<%=recensioni.get(i).getStarsRating()%>" width="64px" height="auto">
                </div>
                <div class="text-center" id="reviewBlock_commento">
                    <%= commento %>
                </div>
            </div>
            <%
                }
            %>
        </div>
        <%
            if (utente != null) {
        %>
        <div class="d-flex justify-content-around">
            <a href="/addRecensione?type=1&id=<%=piatto.getId()%>"><button class="btn btn-sm btn-success">Aggiungi recensione</button></a>
            <a href="/modifyRecensione?type=3&id=<%=piatto.getId()%>"><button class="btn btn-sm btn-primary">Modifica recensione</button></a>
            <button class="btn btn-sm btn-danger" onclick="confermaCancellazione()">Cancella recensione</button>

        </div>
        <br>
        <%
            }
        %>
    </div>

    <%@include file="repetedElements/backLink.jsp"%>
</body>
<script>
    function confermaCancellazione() {
        var richiesta = window.confirm("Sei sicuro di voler cancellare la tua recensione?")
        if (richiesta) {
            window.location.replace("/deleteRecensione?type=3&id=<%=piatto.getId()%>");
        }
        //else // visualizzare pagina del piatto
    }

    function confermaCancellazionePiatto() {
        var richiesta = window.confirm("Sei sicuro di voler cancellare la tua ricetta?")
        if (richiesta) {
            window.location.replace("/deletePlate?id=<%=piatto.getId()%>");
        }
        //else // visualizzare pagina del piatto
    }

    function getErrorMessage(){
        var url = window.location.search;
        url = url.substring(1);
        var parametri = url.split("&");

        for (var i = 0; i < parametri.length; i++){
            var coppia = parametri[i].split("=");
            var nomeParametro = decodeURIComponent(coppia[0]);
            var valore = decodeURIComponent(coppia[1]);
            var messaggio;

            if (nomeParametro === "error"){
                console.log("messaggio");
                if (valore == 1) messaggio = "Impossibile aggiungere una nuova recensione. \nModificare o cancellare quella già presente!";
                if (valore == 2) messaggio = "Recensione inesistente, impossibile modificarla!";
                if (valore == 3) messaggio = "Recensione inesistente, impossibile cancellarla!";

                alert(messaggio);
                break;
            }
        }
    }
    window.addEventListener("load", getErrorMessage());

</script>
</html>
