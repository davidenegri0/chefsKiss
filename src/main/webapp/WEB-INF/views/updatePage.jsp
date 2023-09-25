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
<%
    User utente = (User) request.getAttribute("utente");
    String imgPath = (String) request.getAttribute("imgPath");
    Integer errorCode = (Integer) request.getAttribute("errorCode");
    //System.out.println("Da jsp: imgPath = "+imgPath);
    //System.out.println(utente.getNome());
%>
<html>
<head>
    <title>Modifica il Tuo Profilo</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
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
<body>
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
        <input type="file" accept="image/jpeg" name="file"> <br>
        <input type="submit" value="Aggiorna profilo">
    </form>
    </body>
</html>
