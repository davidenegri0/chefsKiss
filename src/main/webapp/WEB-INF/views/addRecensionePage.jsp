<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 26/09/2023
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User utente = (User)request.getAttribute("user");
    boolean isRistorante = (boolean)request.getAttribute("isRistorante");
    String nomeSoggetto = "Undefined";
    String ID = null;
    int type = 1;
    Piatto piatto = null;
    if (isRistorante){
        //TODO: Ricevere i dati del ristorante e settare nomeSoggetto
        type = 2;
    } else {
        piatto = (Piatto)request.getAttribute("piatto");
        nomeSoggetto = piatto.getNome();
        ID = Integer.toString(piatto.getId());
    }
%>
<html>
<head>
    <title>Dicci la tua opinione</title>
</head>
<body>
    <h1>DICCI LA TUA OPINIONE!</h1>
    <h3>Che cosa ne pensi di <%=nomeSoggetto%>?</h3>
    <form method="post">
        <p><%=utente.getNome()+" "+utente.getCognome()%>, quanto valuteresti <%=nomeSoggetto%>, da 1 a 5 stelle?</p>
        <input type="range" id="voto" name="voto" min="1" max="5" value="3" step="1" list="starValues">
        <datalist id="starValues">
            <option value="1">
            <option value="2">
            <option value="3">
            <option value="4">
            <option value="5">
        </datalist>
        <% if(!isRistorante){ %>
        <p>Vuoi aggiungere un commento (opzionale)? Esprimi al meglio il tuo parere</p>
        <textarea id="commento" name="commento" cols="100" rows="20" maxlength="300"></textarea>
        <% } %>
        <input type="hidden" name="type" value="<%=type%>">
        <input type="hidden" name="ID" value="<%=ID%>">
        <br>
        <input type="submit" value="Ok">
        <input type="reset" value="Annulla">
    </form>
    <%@include file="repetedElements/homepageLink.html"%>
</body>
</html>
