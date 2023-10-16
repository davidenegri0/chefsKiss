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
<%
    User utente = (User) request.getAttribute("utente");
    //String imgPath = (String) request.getAttribute("imgPath");
    String img = Base64.getEncoder().encodeToString(utente.getProfilePicture().getBytes(1, (int)utente.getProfilePicture().length()));
    Integer errorCode = (Integer) request.getAttribute("errorCode");
    //System.out.println("Da jsp: imgPath = "+imgPath);
    //System.out.println(utente.getNome());
%>
<html>
<head>
    <title>Modifica il Tuo Profilo</title>
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
<script>
    <%    if (errorCode!=null){  %>
    var errorCode = <%=errorCode%>;
    <% } else { %>
    var errorCode = 0;
    <% } %>

    if (errorCode==100)
    {
        window.alert("Immagine caricata troppo grande,\ninviare un immagine con dimensione inferiore a 64Kb")
    }
</script>
<body class="food_background">
    <div class="container-fluid bg-dark text-light p-3">
        <h1 class="text-center"><%=utente.getNome()%> <%=utente.getCognome()%>'s Profile Edit Page</h1>
    </div>
    <form method="post" enctype="multipart/form-data">
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
                    <label class="form-label" for="email">Email: </label>
                    <input class="form-control" id="email" name="email" type="email" value="<%=utente.getEmail()%>"> <br>
                    <label class="form-label" for="tel">Numero di telefono: </label>
                    <input class="form-control" id="tel" name="tel" type="number" value="<%=utente.getN_Telefono()%>"> <br>
                </div>
            </div>
            <div class="card m-3 white_transp_background shadow" style="width: 300px">
                <div class="card-header">
                    <h3>Gestione dell'account</h3>
                </div>
                <div class="card-body">
                    <p>
                        Iscritto alla piattaforma dal: <%=utente.getD_Iscrizione()%> <br>
                    </p>
                </div>
            </div>
            <% if (utente.isPrivato()){ %>
            <div class="card m-3 white_transp_background shadow" style="width: 300px">
                <div class="card-header">
                    <h3>Dati da utente pubblico</h3>
                </div>
                <div class="card-body">
                    <label class="form-label" for="username">Username: </label>
                    <input class="form-control"  id="username" name="username" type="text" value="<%=utente.getUsername()%>"> <br>
                    <img class="rounded d-block mx-auto" src="data:image/jpeg;base64,<%=img%>" height="144px" width="144px">
                    <input class="form-control" type="file" accept="image/jpeg" name="file"> <br>
                </div>
            </div>
            <% } %>
        </div>
    </form>
    <div class="container-fluid d-flex my-3 justify-content-center">
        <button class="btn btn-success" type="submit" onclick="document.querySelector('form').submit()">Aggiorna profilo</button>
    </div>
    <%@include file="repetedElements/backLink.jsp"%>
</body>
</html>
