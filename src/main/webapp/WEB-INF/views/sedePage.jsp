<%@ page import="com.project.chefskiss.modelObjects.Valutazione" %>
<%@ page import="com.project.chefskiss.modelObjects.Sede" %>
<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %><%--
  Created by IntelliJ IDEA.
  User: spipp
  Date: 02/10/2023
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sede</title>
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
        background-color: rgb(245, 245, 245, 0.7);
    }

    ul {
        list-style-type: none;
    }

    .plate_list a {
        position: relative;
        text-decoration: none;
        color: #000000;
        transition: all 1s;
    }

    .plate_list a:hover {
        transform: translate(-1px, -2px);
    }

    .link {
        color: #28282f;
    }

</style>
<body class="food_background">
    <%
        User utente = (User)request.getAttribute("user");
        Sede sede = (Sede) request.getAttribute("sede");
        List<User> chefs = (List<User>) request.getAttribute("chefs");
        List<Piatto> piatti = (List<Piatto>) request.getAttribute("piatti");
        List<Valutazione> valutazioni = (List<Valutazione>) request.getAttribute("valutazioni");
        Boolean isProprietario = utente!=null && utente.isRistoratore() && sede.getRistoranteS().getUtenteRi().getCF().equals(utente.getCF());
    %>
    <%@include file="repetedElements/navabar_inc.jsp"%>
    <div class="container-fluid my-3 mr-2 clearfix">
        <div class="row">
            <div class="col-sm-6">
                <div class="card mx-auto white_transp_background shadow" style="width: 100%; height: 100%">
                    <div class="card-body">
                        <h3 class="text-center"><a href="/restaurant?id=<%= sede.getRistoranteS().getID_Ristorante() %>" class="link"><%=sede.getRistoranteS().getNome()%></a> <br>vi da il benvenuto nella sede in <br><%= sede.getVia() %> a <%= sede.getCitta() %></h3>
                        <p> Da noi potrete trovare il seguente menù preparato con tanta cura e amore dagli chef: </p>
                        <% for (int i = 0; i < chefs.size(); i++) { %>
                        <p>
                            <%=chefs.get(i).getNome()+" "+chefs.get(i).getCognome()%>
                            <% if (isProprietario){ %>
                            <a href="/deleteChef?CF=<%=chefs.get(i).getCF()%>&ID=<%=sede.getCoordinate()%>">
                                <button class="btn btn-sm btn-danger"><i class='bx bxs-trash'></i></button>
                            </a>
                            <% } %>
                        </p>
                        <% } %>
                        <% if (isProprietario){ %>
                        <a href="/addChef?ID=<%=sede.getCoordinate()%>"><button class="btn btn-sm btn-success">Aggiungi chef</button></a>
                        <% } %>

                        <br>


                        <br>
                        <% if (utente != null && utente.isCliente()){ %>
                        <div>
                            <h5 >Vuoi venire ad assaggiare i nostri piatti?</h5>
                            <div >Hai cambiato idea e vuoi modificare o cancellare la prenotazione?</div><br>
                            <div class="d-flex justify-content-around">
                                <a href="/addPrenotazione?coordinate=<%=sede.getCoordinate()%>"><button class="btn btn-sm btn-success" style="width: 140px">Prenota qui!</button></a>
                                <a href="/prenotazioniList"><button class="btn btn-sm btn-primary" style="width: 140px">Le mie prenotazioni</button></a>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="card float-end mx-auto white_transp_background shadow" style="width: 100%; height: 100%">
                    <div class="card-body text-center">
                        <h3>Menu della sede</h3><br>
                        <div class="plate_list">
                                <%
                                    for (int i = 0; i < piatti.size(); i++){
                                %>
                                    <a href="/plate?id=<%= piatti.get(i).getId()%>" class="list-group-item list-group-item-action"><%= piatti.get(i).getNome()%> - <%= piatti.get(i).getVotoMedio() %> <i class='bx bxs-star bx-tada' style='color:#ffea00'></i></a>

                                <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </div>


    <div class="container-fluid p-2 bg-dark text-white text-center">
        <h4>Cosa ne pensano i nostri clienti...</h4>
    </div>

    <div class="container mx-auto">
        <div class="d-flex p-3 text-dark justify-content-around flex-wrap">
            <%
                for ( int i = 0; i < valutazioni.size(); i++){
            %>
                <div class="d-flex card m-3 white_transp_background justify-content-center shadow" style="width: 256px; height: 55px" id="valutazioneBlock">
                    <div class="text-center" id="valutazioneBlock_utente">
                        <%= valutazioni.get(i).getUtenteV().getNome() %> <%= valutazioni.get(i).getUtenteV().getCognome() %>
                        <%
                            if (valutazioni.get(i).getUtenteV().isClienteVerificato()){
                        %>
                        <i class='bx bx-badge-check' style='color:#1c00ff' ></i>
                        <% } %>
                    </div>
                    <div id="valutazioneBlock_voto">
                        <img src="<%= valutazioni.get(i).getStarsRating() %>" width="64px" height="auto" class="mx-auto d-block" alt="Immagine rating">
                    </div>
                </div>
            <% } %>
        </div>
            <%
                if (utente != null && utente.isCliente()) {
            %>
                <div class="d-flex justify-content-around">
                    <a href="/addRecensione?type=2&id=<%=sede.getCoordinate()%>"><button class="btn btn-sm btn-success">Aggiungi valutazione</button></a>
                    <a href="/modifyRecensione?type=4&id=<%=sede.getCoordinate()%>"><button class="btn btn-sm btn-primary">Modifica valutazione</button></a>

                    <button class="btn btn-sm btn-danger" onclick="confermaCancellazione()">Cancella valutazione</button>
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
        var richiesta = window.confirm("Sei sicuro di voler cancellare la tua valutazione?");
        if (richiesta) {
            window.location.replace("/deleteRecensione?type=4&id=<%=sede.getCoordinate()%>");
        }
        //else // visualizzare pagina della sede
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
                if (valore == 1) messaggio = "Impossibile aggiungere una nuova valutazione. \nModificare o cancellare quella già presente!";
                if (valore == 2) messaggio = "Valutazione inesistente, impossibile modificarla!";
                if (valore == 3) messaggio = "Valutazione inesistente, impossibile cancellarla!";
                if (valore == 4) messaggio = "Impossibile aggiungere una valutazione se non si ha consumato nel ristorante almeno una volta!";
                if (valore == 5) messaggio = "Prenotazione non effettuata, superato il numero di posti limite!"
                if (valore == 6) messaggio = "Prenotazione non effettuata! \nGià esistente con gli stessi dati!"

                alert(messaggio);
                break;
            }
        }
    }
    window.addEventListener("load", getErrorMessage());
</script>
</html>
