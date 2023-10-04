<%@ page import="com.project.chefskiss.modelObjects.Ristorante" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.modelObjects.Sede" %>
<%@ page import="com.project.chefskiss.modelObjects.User" %><%--
  Created by IntelliJ IDEA.
  User: spipp
  Date: 26/09/2023
  Time: 14:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ristorante</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<body>
    <%
        User utente = (User) request.getAttribute("user");
        Ristorante ristorante = (Ristorante) request.getAttribute("ristorante");
        List<Sede> sedi = (List<Sede>) request.getAttribute("sedi");
        User ristoratore = (User) request.getAttribute("ristoratore");
    %>

    <h1><%= ristorante.getNome()%></h1>
    <h3>Benvenuti nel nostro ristorante! </h3>

    <h3>Venite a trovarci qui! E scoprirete i nostri menù!</h3>
    <ul>
        <% for ( int i = 0; i < sedi.size(); i++ ){ %>
            <li>
                <a href="/sede?id=<%= sedi.get(i).getCoordinate() %>"><%= sedi.get(i).getVia()%> (<%=sedi.get(i).getCitta()%>)</a>
                <% if (utente!=null && ristoratore.getCF().equals(utente.getCF())){ %>
                <a href="/editSede?idR=<%=ristorante.getID_Ristorante()%>&coord=<%=sedi.get(i).getCoordinate()%>"><button><i class='bx bxs-edit-alt'></i></button></a>
                <% if (sedi.size()>1){ %>
                <a href="/deleteSede?idR=<%=ristorante.getID_Ristorante()%>&coord=<%=sedi.get(i).getCoordinate()%>"><button><i class='bx bxs-trash'></i></button></a>
                <% } %>
                <% } %>
            </li>
        <% } %>
    </ul>
    <% if (utente!=null && ristoratore.getCF().equals(utente.getCF())){ %>
    <a href="/addSede"><button>Aggiungi una sede</button></a>
    <% } %>
    <h4>Questo ristorante è gestito da <%= ristoratore.getNome() %> <%= ristoratore.getCognome() %></h4>
    <div>Per richiedere informazioni scrivere a <i><%= ristoratore.getEmail()%></i> o contattare il numero <i><%=ristoratore.getN_Telefono()%></i></div>
    <%@include file="repetedElements/homepageLink.html"%>
</body>
</html>
