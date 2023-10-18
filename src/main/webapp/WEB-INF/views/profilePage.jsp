<%@ page import="javax.swing.*" %>
<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="java.util.Base64" %>
<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 14/09/2023
  Time: 15:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Il Tuo Profilo</title>
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
    <%
        User utente = (User) request.getAttribute("utente");
        //String imgPath = (String) request.getAttribute("imgPath");
        String img = null;
        if(utente.isPrivato()) {
            img = Base64.getEncoder().encodeToString(utente.getProfilePicture().getBytes(1, (int) utente.getProfilePicture().length()));
        }
        //System.out.println("Da jsp: imgPath = "+imgPath);
    %>
    <div class="container-fluid bg-dark text-light p-3">
        <h1 class="text-center fancy_font"><%=utente.getNome()%> <%=utente.getCognome()%>'s Profile</h1>
    </div>
    <nav class="navbar navbar-expand-md bg-dark justify-content-center" data-bs-theme="dark">
        <ul class="navbar-nav">
            <%if(utente.isCliente()){%>
            <li class="nav-item">
                <a class="nav-link" href="/prenotazioniList">Le mie prenotazioni</a>
            </li>
            <%}%>
            <%if(utente.isPrivato() || utente.isChef()){ %>
            <li class="nav-item">
                <a class="nav-link" href="/myRecipes"> Le mie ricette</a>
            </li>
            <%}%>
            <%if(utente.isChef()){%>
            <li class="nav-item">
                <a class="nav-link" href="/sede?id=<%=utente.getSedeU().getCoordinate()%>">Il mio locale</a>
            </li>
            <%}%>
            <%if(utente.isRistoratore()){%>
            <li class="nav-item">
                <a class="nav-link" href="/restaurant?id=<%=utente.getRistoranteU().getID_Ristorante()%>">Il mio ristorante</a>
            </li>
            <%}%>
        </ul>
    </nav>
    <div class="container d-flex justify-content-around flex-wrap">
        <div class="card m-3 white_transp_background shadow" style="width: 300px">
            <div class="card-header">
                <h3>Dati anagrafici</h3>
            </div>
            <div class="card-body">
                <p>
                    Nome: <%=utente.getNome()%> <br>
                    Cognome: <%=utente.getCognome()%> <br>
                    Data di Nascita: <%=utente.getD_Nascita()%> <br>
                    CF: <%=utente.getCF()%>
                </p>
            </div>
        </div>
        <div class="card m-3 white_transp_background shadow" style="width: 300px">
            <div class="card-header">
                <h3>Dati di contatto</h3>
            </div>
            <div class="card-body">
                <p>
                    Email: <%=utente.getEmail()%> <br>
                    Recapito telefonico: <%=utente.getN_Telefono()%>
                </p>
            </div>
        </div>
        <div class="card m-3 white_transp_background shadow" style="width: 300px">
            <div class="card-header">
                <h3>Dati dell'account</h3>
            </div>
            <div class="card-body">
                <p>
                    Iscritto alla piattaforma dal: <%=utente.getD_Iscrizione()%> <br>
                </p>
                <p>
                <h3>Password</h3>
                <a href="/changePassword"><button class="btn btn-warning">Click here to change password</button></a>
                </p>
            </div>
        </div>
        <% if (utente.isPrivato()){ %>
        <div class="card m-3 white_transp_background shadow" style="width: 300px">
            <div class="card-header">
                <h3>Dati da utente pubblico</h3>
            </div>
            <div class="card-body">
                <img class="rounded d-block mx-auto" src="data:image/jpeg;base64,<%=img%>" height="144px" width="144px"> <br>
                <p>
                    Username: <%=utente.getUsername()%> <br>
                </p>
            </div>
        </div>
        <% } %>
    </div>

    <!--
        Ci sono due soluzioni: Condificare l'immagine in formato di testo e poi farla renderizzare direttamente
        al browser attraverso <img src="data:image/jpg;base64, <- base64Data ->">
        Oppure fare una response diretta con l'immagine >>> Metodo testato e funzionante
    -->
    <div class="container-fluid d-flex my-3 justify-content-center">
        <a href="/updateProfile"><button class="btn btn-primary">Modifica i dati del tuo account</button></a>
        <button class="btn btn-danger" onclick="confermaCancellazione()">Cancella i dati del tuo account</button>
    </div>
    <%@ include file="repetedElements/backLink.jsp"%>
</body>
<script>
    function confermaCancellazione() {
        var richiesta = window.confirm("Sei sicuro di voler cancellare il tuo account?");
        if (richiesta) {
            window.location.replace("/deleteProfile");
        }
        //else // visualizzare pagina della sede
    }
</script>
</html>
