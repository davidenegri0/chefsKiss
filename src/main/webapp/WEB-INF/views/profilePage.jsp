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
<script>
    /*
    //Quando ridirezionata, la pagina si ricarica con l'indirizzo corretto
    function onLoadHandler()
    {
        //console.log(window.location.toString());
        if(!window.location.toString().includes("/profile")){
            window.location.replace("/profile");
        }
    }
    window.addEventListener("load", onLoadHandler)
     */
</script>
<body>
    <%
        User utente = (User) request.getAttribute("utente");
        String imgPath = (String) request.getAttribute("imgPath");
        //System.out.println("Da jsp: imgPath = "+imgPath);
    %>
    <h1><%=utente.getNome()%> <%=utente.getCognome()%>'s Profile</h1>
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
    <h3>Dati dell'account</h3>
    <p>
        Iscritto alla piattaforma dal: <%=utente.getD_Iscrizione()%> <br>
    </p>
    <p>
        <h3>Password</h3>
        <a href="http://localhost:8080/changePassword"><button>Click here to change password</button></a>
    </p>

    <h3>Dati da utente pubblico</h3>
    <p>
        Username: <%=utente.getUsername()%> <br>
    </p>
    <img src="<%=imgPath%>" height="144px" width="144px"> <br>
    <!--
        Ci sono due soluzioni: Condificare l'immagine in formato di testo e poi farla renderizzare direttamente
        al browser attraverso <img src="data:image/jpg;base64, <- base64Data ->">
        Oppure fare una response diretta con l'immagine >>> Metodo testato e funzionante
    -->

    <a href="http://localhost:8080/updateProfile"><button>Modifica i dati del tuo account</button></a>

    <%@ include file="repetedElements/homepageLink.html"%>
</body>
</html>
