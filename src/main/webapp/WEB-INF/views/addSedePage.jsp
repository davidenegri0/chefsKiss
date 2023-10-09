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
</head>
<body>
    <h3>Aggiungi una sede per il tuo ristorante: <%=Risto.getNome()%></h3>
    <form method="post">
        <label for="via">Via del locale: </label>
        <input type="text" id="via" name="via" required><br>
        <label for="n_civ">Numero civico: </label>
        <input type="number" id="n_civ" name="n_civ" required><br>
        <label for="citta">Città: </label>
        <input type="text" id="citta" name="citta" required><br>
        <label for="nposti">Numero di posti a sedere: </label>
        <input type="number" id="nposti" name="nposti" required><br>
        <input type="hidden" id="idristo" name="idristo" value="<%=Risto.getID_Ristorante()%>">
        <input type="hidden" id="coord" name="coord">
        <br>
        <input type="button" onclick="calculateANDsubmit()" value="Conferma">
        <input type="reset" value="Annulla">
    </form>
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
