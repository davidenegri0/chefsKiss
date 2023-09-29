<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %>
<%@ page import="com.project.chefskiss.modelObjects.Recensione" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 26/09/2023
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User utente = (User)request.getAttribute("user");
    Integer type = (Integer) request.getAttribute("typecode");
    Piatto piatto = null;
    Recensione recensione = null;
    String nomeSoggetto = null;
    String ID = null;

    if (type == 1 || type == 3){ // aggiunta recensione piatto
        piatto = (Piatto) request.getAttribute("piatto");
        nomeSoggetto = piatto.getNome();
        ID = String.valueOf(piatto.getId());
        if (type == 3) recensione = (Recensione) request.getAttribute("recensione");
    }
    if (type == 2){ // aggiunta recensione sede
        // TODO: cosa viene passato?
    }
    /*if (type == 3){ // modifica recensione piatti
        piatto = (Piatto) request.getAttribute("piatto");

        nomeSoggetto = piatto.getNome();
        ID = String.valueOf(piatto.getId());
    }*/
    if (type == 4){ // modifica recensione sede
        // TODO: cosa viene passato?
    }

    /*
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
    }*/
%>
<html>
<head>
    <title>Dicci la tua opinione</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<body>
    <h1>DICCI LA TUA OPINIONE!</h1>
    <h3>Che cosa ne pensi di <%=nomeSoggetto%>?</h3>
    <form method="post">
        <p><%=utente.getNome()+" "+utente.getCognome()%>, quanto valuteresti <%=nomeSoggetto%>, da 1 a 5 stelle?</p>
        <%
            Integer value_voto = 3;
            if ((type == 3 || type == 4) && recensione != null) value_voto = recensione.getVoto();
            // else if (recensione != null) value_voto = 3; // le aggiunte hanno valore di default da visualizzare
        %>
        <input type="range" id="voto" name="voto" min="1" max="5" value="<%= value_voto %>" step="1" list="starValues">
        <datalist id="starValues">
            <option value="1">
            <option value="2">
            <option value="3">
            <option value="4">
            <option value="5">
        </datalist>
        <% //if(!isRistorante){ %>

        <%
            String value_commento = "";
            if ((type == 3 || type == 4) && recensione != null) value_commento = recensione.getCommento();
            //else if (recensione != null)value_commento = ""; // le aggiunte non hanno ancora nessun dato da visualizzare
        %>

        <% if(type == 1 || type == 3){ // se è piatto %>
            <p>Vuoi aggiungere un commento (opzionale)? Esprimi al meglio il tuo parere</p>
            <textarea id="commento" name="commento" cols="100" rows="20" maxlength="300"><%= value_commento %></textarea>
        <% } %>
        <input type="hidden" name="type" value="<%=type%>">
        <input type="hidden" name="ID" value="<%=ID%>">
        <% if (type == 3) { %>
            <input type="hidden" name="recensione" value="<%=recensione%>">
        <% } %>

        <br>
        <input type="submit" value="Ok">
        <input type="reset" value="Annulla" onclick=window.history.back()>
    </form>
    <%@include file="repetedElements/homepageLink.html"%>
</body>
</html>
