<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Sede" %>
<%@ page import="com.project.chefskiss.modelObjects.Prenotazione" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.sql.Time" %><%--
  Created by IntelliJ IDEA.
  User: spipp
  Date: 04/10/2023
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User utente = (User) request.getAttribute("user");
    Sede sede = (Sede) request.getAttribute("sede");
    Integer type = (Integer) request.getAttribute("type");
    Prenotazione prenotazione = (Prenotazione) request.getAttribute("prenotazione");
%>
<html>
<head>
    <title>Prenotazione</title>
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
            <%
                Date data = null;
                Time orario = null;
                Integer posti = null;
            %>
            <%
                String parola1, parola2;
                if (type == 1){
                    parola1 = "Compila";
                    parola2 = "effettuare una nuova";
                }
                else {
                    parola1 = "Modifica";
                    parola2 = "modificare la tua";
                }
            %>
            <h1 class="text-center">VIENI A TROVARCI!</h1>
            <h3 class="ms-3"><%=parola1%> i seguenti campi per <%=parola2%> prenotazione nella nostra sede</h3>
        </div>
        <div class="card-body">
            <%
                if (type == 2){
                    data = prenotazione.getData();
                    orario = prenotazione.getOrario();
                    posti = prenotazione.getN_Posti();
                }
            %>
            <div class="container my-2">
                <h4><%= sede.getVia() %>, <%= sede.getCitta() %></h4>
                <form method="post" onsubmit="return validaData()">
                    <div class="mb-3">
                        <label for="data" class="form-label">Data:</label>
                        <input type="date" class="form-control" id="data" name="data" value="<%= data %>">
                    </div>

                    <div class="mb-3">
                        <label for="orario" class="form-label">Orario:</label>
                        <input type="time" class="form-control" id="orario" name="orario" value="<%= orario %>">
                    </div>

                    <div class="mb-3">
                        <label for="posti" class="form-label">Per quante persone vuoi prenotare?</label>
                        <input type="number" class="form-control" id="posti" name="n_posti" value="<%= posti %>">
                    </div>

                    <input type="hidden" name="CF" value="<%= utente.getCF() %>">
                    <% if (type == 2) { %>
                    <input type="hidden" name="prenotazione" value="<%= prenotazione %>">
                    <% } %>

                    <div class="mb-3">
                        <button type="submit" class="btn btn-success">Ok</button>
                        <button type="button" class="btn btn-danger" onclick="window.history.back()">Annulla</button>
                    </div>
                </form>
            </div>

            <%@include file="repetedElements/backLink.jsp"%>
        </div>
    </div>
</body>
<script>
    function validaData(){
        var dataInserita = new Date(document.getElementById("data").value);
        var dataCorrente = new Date();
        if ( dataInserita <= dataCorrente ){
            alert("La data inserita deve essere successiva a quella attuale!");
            return false;
        }
        return true;
    }
</script>
</html>
