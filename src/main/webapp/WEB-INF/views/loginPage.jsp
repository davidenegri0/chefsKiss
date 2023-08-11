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
</head>
<body>
    <h2>Login Page</h2>
    <div id="login-form">
        <form name="loginForm" method="post">
            <label for="us">Username:</label>
            <input id="us" name="usr" type="text">
            <label for="ps">Password:</label>
            <input id="ps" name="pssw" type="password">
            <input type="submit" value="Login">
        </form>
    </div>
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
