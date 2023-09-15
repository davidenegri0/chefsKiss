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
    <title>Il Tuo Profilo</title>
</head>
<body>
    <%
        User utente = (User) request.getAttribute("utente");
    %>
    <h1><%=utente.getNome()%> <%=utente.getCognome()%>'s Profile</h1>
    <h2>Questo è il tuo profilo!</h2>
    <h3>Dati anagrafici</h3>
    <p>
        Nome: <%=utente.getNome()%> <br>
        Cognome: <%=utente.getCognome()%> <br>
        Data di Nascita: <%=utente.getD_Nascita()%> <br>
        CF: <%=utente.getCF()%>
    </p>
    <h3>Dati di contatto</h3>
    <p>
        Email: <%=utente.getEmail()%> <br>
        Recapito telefonico: <%=utente.getN_Telefono()%>
    </p>
    <h3>Gestione dell'account</h3>
    <!-- Potrebbe non funzionare -->
    <p>
        Iscritto alla piattaforma dal: <%=utente.getD_Iscrizione()%> <br>
        <a href="http://localhost:8080/changePassword"><button>Click here to change password</button></a>
    </p>
</body>
</html>
