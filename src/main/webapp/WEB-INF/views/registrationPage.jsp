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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<body>
    <h2>Registration Page</h2>
    <div id="registration-form">
        <form id="reg_form" name="registration-form" method="post" enctype="multipart/form-data">
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
            <label for="cliente">Sei interessato a effettuare prenotazioni sul sito?</label>
            <input id="cliente" name="cliente" type="checkbox" value="true"><br>
            <label for="privato">Sei interessato a postare ricette sulla piattaforma?</label>
            <input id="privato" name="privato" type="checkbox" value="true"><br>
            <div id="priv_data" hidden>
                <label for="username">Username: </label>
                <input id="username" name="username" type="text"><br>
                <label for="foto_prv">Foto profilo: </label>
                <input id="foto_prv" name="foto_prv" type="file" accept="image/jpeg"><br>
            </div>
            <label for="chef">Sei uno chef con occupazione o in cerca di un lavoro?</label>
            <input id="chef" name="chef" type="checkbox" value="true"><br>
            <div id="chef_data" hidden>
                <label for="foto_chef">Foto profilo: </label>
                <input id="foto_chef" name="foto_chef" type="file" accept="image/jpeg"><br>
                <label for="cv_chef">Curriculum Vitae: </label>
                <input id="cv_chef" name="cv_chef" type="file" accept="text/plain"><br>
            </div>
            <label for="ristoratore">Sei proprietario di un ristorante?</label>
            <input id="ristoratore" name="ristoratore" type="checkbox" value="true"><br>
            <input type="submit" value="Conferma">
            <input type="reset" value="Annulla" onclick=window.history.back()>
        </form>
    </div>
    <%@include file="repetedElements/backLink.jsp"%>
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

    var privatoCheckbox = document.getElementById("privato");
    function privatoCheck(){
        if (privatoCheckbox.checked){
            document.getElementById("priv_data").removeAttribute("hidden");
        } else {
            document.getElementById("priv_data").setAttribute("hidden", "hidden");
        }
    }
    privatoCheckbox.addEventListener('change', privatoCheck);

    var chefCheckbox = document.getElementById("chef");
    function chefCheck(){
        if (chefCheckbox.checked){
            document.getElementById("chef_data").removeAttribute("hidden");
        } else {
            document.getElementById("chef_data").setAttribute("hidden", "hidden");
        }
    }
    chefCheckbox.addEventListener('change', chefCheck);

</script>
</html>
