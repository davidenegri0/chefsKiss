<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 09/08/2023
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<style>
    body {
        margin: 0;
        padding: 0;
        background-image: url("img/food_background_v2.jpg");
        background-size: contain;
    }

    .card{
        margin-bottom:20px;
        border:none;
    }

    .box {
        width: 500px;
        padding: 40px;
        position: absolute;
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
        border: 0;
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

    .box h1 {
        color: white;
        text-transform: uppercase;
        font-weight: 500
    }

    .box input[type="text"]:focus,
    .box input[type="password"]:focus {
        width: 300px;
        border-color: #2ecc71
    }

    .box input[type="submit"] {
        border: 0;
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

    .box input[type="submit"]:hover {
        background: #2ecc71
    }
</style>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-6">
            <div class="card" id="login-form">
                <form class="box" name="loginForm" method="post">
                    <input id="em" name="email" type="text" placeholder="Email" required>
                    <input id="ps" name="pssw" type="password" placeholder="Password" required>
                    <input type="submit" value="Login">
                </form>
            </div>
        </div>
    </div>
</div>
    <%@include file="repetedElements/backLink.jsp"%>
</body>
<script>
    function onLoginRequest(){
        var errorCode = 0;
        <%if(request.getAttribute("errorCode")!=null){%>
        errorCode = <%=request.getAttribute("errorCode")%>;
        console.log(errorCode);
        <%}%>
        if(errorCode == 1) {
            var errorMessage = document.createTextNode("Errore nei dati di login. Riprovare!");
            var form = document.getElementById("login-form");
            form.append(errorMessage);
        }
    }
    window.addEventListener("load", onLoginRequest);
</script>
</html>
