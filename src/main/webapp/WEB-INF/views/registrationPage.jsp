<%--
  Created by IntelliJ IDEA.
  User: spipp
  Date: 11/08/2023
  Time: 17:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registrazione</title>
</head>
<body>
    <h2>Registration Page</h2>
    <div id="registration-form">
        <form name="registration-form" method="post">
            <label for="nome">Nome:</label>
            <input id="nome" name="nome" type="text" required><br>
            <label for="cognome">Cognome:</label>
            <input id="cognome" name="cognome" type="text" required><br>
            <label for="cf">CF:</label>
            <input id="cf" name="cf" type="text" maxlength="16" required><br>
            <label for="email">Email:</label>
            <input id="email" name="email" type="email" required><br>
            <label for="telefono">Telefono:</label>
            <input id="telefono" name="telefono" type="number" maxlength="12" required><br>
            <label for="nascita">Data di nascita:</label>
            <input id="nascita" name="nascita" type="date" required><br>
            <label for="pssw">Password:</label>
            <input id="pssw" name="pssw" type="password" required><br>
            <label for="c_pssw">Conferma Password:</label>
            <input id="c_pssw" name="c_pssw" type="password" required><br>
            <input type="submit" value="Conferma">
        </form>
    </div>
</body>
<script>
    function onLoadRequest(){
        var errorCode = 0;
        <%if(request.getAttribute("errorCode")!=null){%>
        errorCode = <%=request.getAttribute("errorCode")%>;
        <%}%>
        if(errorCode == 1){
            window.alert("Errore nell'inserimento dei dati! Email o CF già in uso");
        }
    }
    window.addEventListener("load", onLoadRequest);
</script>
</html>
