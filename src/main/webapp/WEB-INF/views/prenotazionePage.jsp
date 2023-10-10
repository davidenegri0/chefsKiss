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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<body>
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
    <h3><%=parola1%> i seguenti campi per <%=parola2%> prenotazione nella nostra sede</h3>

    <%
        if (type == 2){
            data = prenotazione.getData();
            orario = prenotazione.getOrario();
            posti = prenotazione.getN_Posti();
        }
    %>
    <form method="post" onsubmit="return validaData()">
        <p><%= sede.getVia()%>, <%= sede.getCitta()%></p>
        <label for="data">Data:</label>
        <input type="date" id="data" name="data" value="<%= data %>"></br>
        <label for="orario">Orario:</label>
        <input type="time" id="orario" name="orario" value="<%= orario %>"></br>
        <label for="posti">Per quante persone?</label>
        <input type="number" id="posti" name="n_posti" value="<%=posti%>"></br>
        <input type="hidden" name="CF" value="<%=utente.getCF()%>">
        <%
            if(type == 2){
        %>
        <input type="hidden" name="prenotazione" value="<%=prenotazione%>">
        <% } %>
        <br>
        <input type="submit" value="Ok">
        <input type="reset" value="Annulla" onclick=window.history.back()>
    </form>
    <%@include file="repetedElements/backLink.jsp"%>
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
