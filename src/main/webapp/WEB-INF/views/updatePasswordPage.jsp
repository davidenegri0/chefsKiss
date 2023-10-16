<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 28/09/2023
  Time: 12:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int errorCode = (int) request.getAttribute("errorCode");
%>
<html>
<head>
    <title>Cambia la password</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="/img/chef'skiss_logo_emoji.png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
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
    .box input[type="password"] {
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
    .box input[type="password"]:focus {
        width: 300px;
        border-color: #2ecc71
    }

    .box input.success[type="button"] {
        background: none;
        display: block;
        margin: 20px auto;
        text-align: center;
        border: 2px solid #2ecc71;
        padding: 14px 40px;
        outline: none;
        color: white;
        border-radius: 24px;
        transition: 0.25s;
        cursor: pointer
    }

    .box input.success[type="button"]:hover {
        background: #2ecc71
    }

    .box input.undo[type="button"] {
        background: none;
        display: block;
        margin: 20px auto;
        text-align: center;
        border: 2px solid darkred;
        padding: 14px 40px;
        outline: none;
        color: white;
        border-radius: 24px;
        transition: 0.25s;
        cursor: pointer
    }

    .box input.undo[type="button"]:hover {
        background: darkred;
    }

    .box .register-link {
        text-align: center;
        margin: 20px 0 15px;
        color: white;
    }

    .register-link p a {
        color: white;
        text-decoration: none;
        font-weight: 600;
    }

    .register-link p a:hover {
        text-decoration: underline;
    }

</style>
<script>
    <% if(errorCode==1){ %>
    alert("La vecchia password non corrisponde!");
    <% } %>

    function checkPasswordANDsubmit(){
        var passwordInput = document.getElementById("newP").value;
        var passwordCheck = document.getElementById("newPRep").value;
        if (passwordInput != passwordCheck){
            alert("Le due password non coincidono!");
        }
        else document.querySelector('form').submit();
    }
</script>
<body>
    <div class="card">
        <form class="box" method="post">
            <h2>Aggiorna la tua password</h2>
            <div class="input-box">
                <input id="oldP" type="password" name="oldPassword" placeholder="Vecchia Password" required>
            </div>
            <div class="input-box">
                <input id="newP" type="password" name="newPassword" placeholder="Nuova Password" required>
            </div>
            <div class="input-box">
                <input id="newPRep" type="password" name="newPasswordRep" placeholder="Ripeti Nuova Password" required>
            </div>
            <input class="success" type="button" value="Invia" onclick="checkPasswordANDsubmit()">
            <input class="undo" type="button" value="Annulla" onclick=window.history.back()>
        </form>
    </div>
    <%@include file="repetedElements/backLink.jsp"%>
</body>
</html>