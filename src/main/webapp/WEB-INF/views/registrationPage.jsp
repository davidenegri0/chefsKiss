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
    <link rel="icon" href="/img/chef'skiss_logo_emoji.png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<style>
    body {
        margin: 0;
        padding: 0;
        background-image: url("img/food_background_v2.jpg");
        background-size: contain;
        display: flex;
        justify-content: center;
        align-items: center;
        background-position: center;
    }

    .card{
        margin-bottom:100px;
        border:none;
        align-content: center;
        background-color: transparent;
    }

    .box {
        width: 500px;
        padding: 40px;
        top: 50%;
        left: 50%;
        background: rgba(19,19,19,0.9);
        border-radius: 25px;
        text-align: center;
        transition: 0.25s;
        margin-top: 100px
    }

    .box input[type="text"],
    .box input[type="password"],
    .box input[type="email"],
    .box input[type="date"],
    .box input[type="number"]{
        background: none;
        display: block;
        margin: 20px auto;
        text-align: center;
        border: 2px solid #3498db;
        padding: 10px 10px;
        width: 250px;
        outline: none;
        color: white;
        border-radius: 24px;
        transition: 0.25s
    }

    .box h2 {
        color: white;
        text-transform: uppercase;
        font-weight: 500
    }

    .box input[type="text"]:focus,
    .box input[type="password"]:focus,
    .box input[type="email"]:focus,
    .box input[type="date"]:focus,
    .box input[type="number"]:focus{
        width: 300px;
        border-color: #2ecc71
    }

    .box input[type="button"].conferma {
        background: none;
        width: 150px;
        display: block;
        margin: 20px;
        text-align: center;
        border: 2px solid #2ecc71;
        padding: 14px 40px;
        outline: none;
        color: #dedede;
        border-radius: 24px;
        transition: 0.25s;
        cursor: pointer;
        float: left;
    }

    .box input[type="reset"] {
        background: none;
        width: 150px;
        display: block;
        margin: 20px;
        text-align: center;
        border: 2px solid orangered;
        padding: 14px 40px;
        outline: none;
        color: #dedede;
        border-radius: 24px;
        transition: 0.25s;
        cursor: pointer;
        float: right;
    }

    .box input[type="button"].conferma:hover {
        background: #2ecc71
    }

    .box input[type="reset"]:hover {
        background: orangered;
    }

    .input-box label{
        color: #dedede;
    }

    .form-check-label {
        color: #dedede;
    }

    .form-label {
        color: #dedede;
    }

    .form-control-sm {
        width: 50%;
    }

    .box .login-link {
        text-align: center;
        margin: 20px 0 15px;
        color: white;
    }

    .login-link p a {
        color: white;
        text-decoration: none;
        font-weight: 600;
    }

    .login-link p a:hover {
        text-decoration: underline;
    }

</style>
<body>
<div id="registration-form" class="card">
    <form id="reg_form" name="registration-form" method="post" enctype="multipart/form-data" class="box container-fluid m-3 clearfix">
        <h2>Crea il tuo Account</h2>
        <div class="input-box">
            <label for="nome" hidden>Nome:</label>
            <input id="nome" name="nome" type="text" placeholder="Nome" required>
        </div>
        <div class="input-box">
            <label for="cognome" hidden>Cognome:</label>
            <input id="cognome" name="cognome" type="text" placeholder="Cognome" required>
        </div>
        <div class="input-box">
            <label for="cf" hidden>CF:</label>
            <input id="cf" name="cf" type="text" maxlength="16" placeholder="Codice Fiscale" required>
        </div>
        <div class="input-box">
            <label for="email" hidden>Email:</label>
            <input id="email" name="email" type="email" placeholder="Email" required>
        </div>
        <div class="input-box">
            <label for="telefono" hidden>Telefono:</label>
            <input id="telefono" name="telefono" type="number" maxlength="12" placeholder="Telefono" required>
        </div>
        <div class="input-box">
            <label for="nascita" hidden>Data di nascita:</label>
            <input id="nascita" name="nascita" type="text" placeholder="Data di Nascita" onfocus="(this.type = 'date')" required>
        </div>
        <div class="input-box">
            <label for="pssw" hidden>Password:</label>
            <input id="pssw" name="pssw" type="password" placeholder="Password" required>
        </div>
        <div class="input-box">
            <label for="c_pssw" hidden>Conferma Password:</label>
            <input id="c_pssw" name="c_pssw" type="password" placeholder="Conferma Password" required>
        </div>
        <div class="form-check">
            <input class="form-check-input" id="cliente" name="cliente" type="checkbox" value="true">
            <label for="cliente" class="form-check-label">Sei interessato a effettuare prenotazioni sul sito?</label>
            <br>
        </div>
        <div class="form-check">
            <input class="form-check-input" id="privato" name="privato" type="checkbox" value="true">
            <label for="privato" class="form-check-label">Sei interessato a postare ricette sulla piattaforma?</label>

        </div>

        <div id="priv_data" hidden >
            <div class="input-box">
                <label for="username" hidden>Username: </label>
                <input id="username" name="username" type="text" placeholder="Username">
            </div>
            <div class="mb-3 center">
                <label for="foto_prv" class="form-label">Foto profilo: </label><br>
                <input id="foto_prv" name="foto_prv" type="file" accept="image/jpeg" class="form-control form-control-sm"><br>
            </div>
        </div>
        <div class="form-check">
            <input class="form-check-input" id="chef" name="chef" type="checkbox" value="true">
            <label for="chef" class="form-check-label">Sei uno chef con occupazione o in cerca di un lavoro?</label>

        </div>
        <div id="chef_data" hidden>
            <div class="mb-3">
                <label for="foto_chef" class="form-label">Foto profilo: </label>
                <input id="foto_chef" name="foto_chef" type="file" accept="image/jpeg" class="form-control form-control-sm">
            </div>
            <div class="mb-3">
                <label for="cv_chef" class="form-label">Curriculum Vitae: </label>
                <input id="cv_chef" name="cv_chef" type="file" accept="text/plain" class="form-control form-control-sm"><br>
            </div>
        </div>
        <div class="form-check">
            <label for="ristoratore" class="form-check-label">Sei proprietario di un ristorante?</label>
            <input class="form-check-input" id="ristoratore" name="ristoratore" type="checkbox" value="true"><br>
        </div>
        <div class="d-flex justify-content-around">
            <input class="conferma" type="button" value="Conferma" onclick="checkPasswordANDsubmit()">
            <input class="annulla" type="reset" value="Annulla" onclick=window.history.back()>
        </div>

        <div class="login-link">
            <p>Hai già un account?
                <a href="/login">Fai il login!</a>
            </p>
        </div>
    </form>
</div>

<div>
    <%@include file="repetedElements/backLink.jsp"%>
</div>
</body>
<script>
    <% Integer errorCode = (Integer) request.getAttribute("errorCode");
    if(errorCode!=null && errorCode==2){%>
    alert("L'immagine caricata è troppo grande! Max 64Kb");
    <%}%>

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

    function checkPasswordANDsubmit(){
        var passwordInput = document.getElementById("pssw").value;
        var passwordCheck = document.getElementById("c_pssw").value;
        if (passwordInput != passwordCheck){
            alert("Le due password non coincidono!");
        }
        else document.querySelector('form').submit();
    }

</script>
</html>
