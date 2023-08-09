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
    <form name="login">
        <label for="usr">Username:</label>
        <input id="usr" name="usr" type="text">
        <label for="pssw">Password:</label>
        <input id="pssw" name="pssw" type="text">
        <input type="submit" value="Login">
    </form>
</body>
<script>
    function onLoginRequest(){
        var status = ${loginStatus};
        console.log(status);
        if(status){
            console.log("Logged in");
        }
        else{
            console.log("Not working");
        }
    }
    window.addEventListener("load", onLoginRequest);
</script>
</html>
