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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<body>
    <%
        User utente = (User)request.getAttribute("user");
        Sede sede = (Sede) request.getAttribute("sede");
        List<User> chefs = (List<User>) request.getAttribute("chefs");
        List<Piatto> piatti = (List<Piatto>) request.getAttribute("piatti");
        List<Valutazione> valutazioni = (List<Valutazione>) request.getAttribute("valutazioni");
        Boolean isProprietario = utente!=null && utente.isRistoratore() && sede.getRistoranteS().getUtenteRi().getCF().equals(utente.getCF());
    %>
    <h2><a href="/restaurant?id=<%= sede.getRistoranteS().getID_Ristorante() %>"><%=sede.getRistoranteS().getNome()%></a> vi da il benvenuto nella sede in <%= sede.getVia() %> a <%= sede.getCitta() %></h2>
    <p> Da noi potrete trovare il seguente menù preparato con tanta cura e amore dagli chef: </p>
    <% for (int i = 0; i < chefs.size(); i++) { %>
    <p>
        <%=chefs.get(i).getNome()+" "+chefs.get(i).getCognome()%>
        <% if (isProprietario){ %>
        <a href="/deleteChef?CF=<%=chefs.get(i).getCF()%>&ID=<%=sede.getCoordinate()%>">
            <button><i class='bx bxs-trash'></i></button>
        </a>
        <% } %>
    </p>
    <% } %>
    <% if (isProprietario){ %>
    <a href="/addChef?ID=<%=sede.getCoordinate()%>"><button>Aggiungi chef</button></a>
    <% } %>
    <h3>Menu della sede</h3>
    <nav>
        <ul>
            <%
                for (int i = 0; i < piatti.size(); i++){
            %>
            <li>
                <a href="/plate?id=<%= piatti.get(i).getId()%>"><%= piatti.get(i).getNome()%></a> - <%= piatti.get(i).getVotoMedio() %> <i class='bx bxs-star bx-tada' style='color:#ffea00'></i>
            </li>
            <% } %>
        </ul>
    </nav>

    <% if (utente != null && utente.isCliente()){ %>
    <h4>Vuoi venire ad assaggiare i nostri piatti?</h4>
    <a href="/addPrenotazione?coordinate=<%=sede.getCoordinate()%>"><button>Prenota qui!</button></a>
    <p>Hai cambiato idea e vuoi modificare o cancellare la prenotazione?</p>
    <a href="/prenotazioniList"><button>Le mie prenotazioni</button></a>
    <% } %>

    <h4>Cosa ne pensano i nostri clienti...</h4>
    <%
        for ( int i = 0; i < valutazioni.size(); i++){
    %>
        <p id="valutazioneBlock">
            <div id="valutazioneBlock_utente">
                <%= valutazioni.get(i).getUtenteV().getNome() %> <%= valutazioni.get(i).getUtenteV().getCognome() %>
            </div>
            <div id="valutazioneBlock_voto">
                <img src="<%= valutazioni.get(i).getStarsRating() %>" width="8%" height="auto">
            </div>
        </p>
    <%
        }
        if (utente != null && utente.isCliente()) {
    %>
        <a href="/addRecensione?type=2&id=<%=sede.getCoordinate()%>"><button>Aggiungi valutazione</button></a>
        <a href="/modifyRecensione?type=4&id=<%=sede.getCoordinate()%>"><button>Modifica valutazione</button></a>

        <button onclick="confermaCancellazione()">Cancella valutazione</button>
    <%
        }
    %>

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

                alert(messaggio);
                break;
            }
        }
    }
    window.addEventListener("load", getErrorMessage());
</script>
</html>
