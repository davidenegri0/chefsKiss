<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.dataAccessObjects.ContieneDAO" %>
<%@ page import="com.project.chefskiss.modelObjects.*" %>
<%--
  Created by IntelliJ IDEA.
  User: spipp
  Date: 24/09/2023
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Piatto</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<body>
    <%
        User utente = (User)request.getAttribute("user");
        Piatto piatto = (Piatto) request.getAttribute("piatto_passato");
        List<Contiene> ingredienti = (List<Contiene>) request.getAttribute("ingredienti");
        //Integer id_piatto = (Integer) request.getAttribute("id_piatto");
        //String nome_piatto = (String) request.getAttribute("nome_piatto");
        User utente_post = (User) request.getAttribute("utente_post");
        List<Sede> sedi = (List<Sede>) request.getAttribute("sedi");
        List<Ristorante> ristoranti = (List<Ristorante>) request.getAttribute("ristoranti");
        List<Recensione> recensioni = (List<Recensione>) request.getAttribute("recensioni");
        List<User> utenti_recensori = (List<User>) request.getAttribute("utenti_recensori");
    %>
    <h1><%=piatto.getNome()%></h1>
    <p>
        Caricato da: <%= utente_post.getNome() %> <%= utente_post.getCognome() %>
    </p>
    <div>Qui ci andrebbe l'immagine del piatto</div>
    <h3>Ingredienti</h3>
    <div>Per 4 persone:</div>
    <table>
        <tr>
            <th><i>Ingrediente</i></th><th><i>Quantità</i></th>
        </tr>
        <%
            ContieneDAO contiene;
            String ingrediente;
            Integer quantita;

            for (int i = 0; i < ingredienti.size(); i++) {
                ingrediente = ingredienti.get(i).getIngredienteC().getNome();
                quantita = (ingredienti.get(i).getQuantita())*4;

        %>
            <tr>
                <td><%= ingrediente %></td>
                <td><%= quantita %> gr</td>
            </tr>
        <%
            }
        %>
    </table>

    <!--
        FC was here!
    -->

    <h3>Preparazione</h3>
    <p>
        <%= piatto.getPreparazione() %>
    </p>

    <h4>Si può trovare tra i menù dei seguenti ristoranti:</h4>
    <nav>
        <ul>
            <%
                Integer id_ristorante;

                for (int i = 0; i < sedi.size(); i++){
            %>
            <li>
                <a href = "http://localhost:8080/restaurant?id=<%= ristoranti.get(i).getID_Ristorante()%>"><%= ristoranti.get(i).getNome() %></a> -
                <%= sedi.get(i).getVia() %>, (<%= sedi.get(i).getCitta() %>)
            </li>
            <% } %>
        </ul>
    </nav>

    <h3>Recensioni</h3>

    <%
        String CF_utente, commento;
        Integer voto;


        for (int i = 0; i < recensioni.size(); i++){
            CF_utente = recensioni.get(i).getUtenteR().getCF();
            voto = recensioni.get(i).getVoto();
            commento = recensioni.get(i).getCommento();
    %>
        <p id="reviewBlock">
            <div id="reviewBlock_utente">
                <%= utenti_recensori.get(i).getNome() %> <%= utenti_recensori.get(i).getCognome() %>
    </div>
            <div id="reviewBlock_voto">
                <img src="<%=recensioni.get(i).getStarsRating()%>" width="8%" height="auto">
            </div>
            <div id="reviewBlock_commento">
                <%= commento %>
            </div>
        </p>
    <%
        }
    %>
    <%
        if (utente != null) {
    %>
        <a href="http://localhost:8080/addRecensione?type=1&id=<%=piatto.getId()%>"><button>Aggiungi recesione</button></a>
        <a href="http://localhost:8080/modifyRecensione?type=3&id=<%=piatto.getId()%>"><button>Modifica recesione</button></a>
        <a href="http://localhost:8080/deleteRecensione?type=1&id=<%=piatto.getId()%>"><button>Cancella recesione</button></a>
    <%
        }
    %>

    <%@include file="repetedElements/homepageLink.html"%>
</body>
</html>
