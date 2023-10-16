<%@ page import="javax.xml.validation.Validator" %>
<%@ page import="com.project.chefskiss.modelObjects.*" %><%--
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
    Sede sede = null;
    Recensione recensione = null;
    Valutazione valutazione = null;
    String nomeSoggetto = null;
    String ID = null;

    if (type == 1 || type == 3){ // aggiunta recensione piatto
        piatto = (Piatto) request.getAttribute("piatto");
        nomeSoggetto = piatto.getNome();
        ID = String.valueOf(piatto.getId());
        if (type == 3) recensione = (Recensione) request.getAttribute("recensione");
    }
    if (type == 2 || type == 4){ // aggiunta recensione sede
        sede = (Sede) request.getAttribute("sede");
        nomeSoggetto = sede.getVia() + " " + sede.getCitta();
        ID = sede.getCoordinate();
        if (type == 4) valutazione = (Valutazione) request.getAttribute("valutazione");
    }
    /*if (type == 3){ // modifica recensione piatti
        piatto = (Piatto) request.getAttribute("piatto");

        nomeSoggetto = piatto.getNome();
        ID = String.valueOf(piatto.getId());
    }
    if (type == 4){ // modifica recensione sede
    }*/

    /*
    boolean isRistorante = (boolean)request.getAttribute("isRistorante");
    String nomeSoggetto = "Undefined";
    String ID = null;
    int type = 1;
    Piatto piatto = null;
    if (isRistorante){
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
            <h1 class="text-center">DICCI LA TUA OPINIONE!</h1>
            <h3 class="ms-3">Che cosa ne pensi di <%=nomeSoggetto%>?</h3>
        </div>
        <div class="card-body">
            <form method="post">
                <div class="container my-2">
                    <p><%=utente.getNome()+" "+utente.getCognome()%>, quanto valuteresti <%=nomeSoggetto%>, da 1 a 5 stelle?</p>
                    <%
                        Integer value_voto = 3;
                        if (type == 3 && recensione != null) value_voto = recensione.getVoto();
                        if (type == 4 && valutazione != null) value_voto = valutazione.getVoto();
                        // else if (recensione != null) value_voto = 3; // le aggiunte hanno valore di default da visualizzare
                    %>
                    <div class="container w-50 mx-0">
                        <img class="mx-auto d-block w-100 mh-auto" src="/img/rating_stars/rating-star-icon-5-of-5.png">
                        <input class="form-range w-75 d-block mx-auto" type="range" id="voto" name="voto" min="1" max="5" value="<%= value_voto %>" step="1" list="starValues">
                        <datalist id="starValues">
                            <option value="1">
                            <option value="2">
                            <option value="3">
                            <option value="4">
                            <option value="5">
                        </datalist>
                    </div>
                </div>
                <% //if(!isRistorante){ %>
                <div class="container my-2">
                    <%
                        String value_commento = "";
                        if (type == 3 && recensione != null) value_commento = recensione.getCommento();
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
                    <% if (type == 4) { %>
                    <input type="hidden" name="valutazione" value="<%=valutazione%>">
                    <% } %>
                </div>
                <div class="container my-2">
                    <input class="btn btn-success" type="submit" value="Ok">
                    <input class="btn btn-danger" type="reset" value="Annulla" onclick=window.history.back()>
                </div>
            </form>
        </div>
    </div>

    <%@include file="repetedElements/backLink.jsp"%>
</body>
</html>
