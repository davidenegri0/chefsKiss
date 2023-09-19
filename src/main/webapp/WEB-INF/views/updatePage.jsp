<%@ page import="javax.swing.*" %>
<%@ page import="com.project.chefskiss.modelObjects.User" %>
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
    <title>Modifica il Tuo Profilo</title>
</head>
<body>
    <%
        User utente = (User) request.getAttribute("utente");
        String imgPath = (String) request.getAttribute("imgPath");
        //System.out.println("Da jsp: imgPath = "+imgPath);
        //System.out.println(utente.getNome());
    %>
    <h1><%=utente.getNome()%> <%=utente.getCognome()%>'s Profile Edit Page</h1>
    <form method="post" enctype="multipart/form-data">
        <h3>Dati anagrafici</h3>
        <p>
            Nome: <%=utente.getNome()%> <br>
            Cognome: <%=utente.getCognome()%> <br>
            Data di Nascita: <%=utente.getD_Nascita()%> <br>
            CF: <%=utente.getCF()%>
        </p>
        <h3>Dati di contatto</h3>
        <label for="email">Email: </label>
        <input id="email" name="email" type="email" value="<%=utente.getEmail()%>"> <br>
        <label for="tel">Numero di telefono: </label>
        <input id="tel" name="tel" type="number" value="<%=utente.getN_Telefono()%>"> <br>
        <h3>Gestione dell'account</h3>
        <p>
            Iscritto alla piattaforma dal: <%=utente.getD_Iscrizione()%> <br>
        </p>
        <h3>Dati da utente pubblico</h3>
        <label for="username">Username: </label>
        <input id="username" name="username" type="text" value="<%=utente.getUsername()%>"> <br>
        <img src="<%=imgPath%>" height="144px" width="144px">
        <br>
        <input type="file" accept=".jpg" name="file"> <br>
        <input type="submit" value="Aggiorna profilo">
    </form>
    </body>
</html>