<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 09/08/2023
  Time: 15:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Redirecting...</title>
</head>
<body>
    You will be redirected to the homepage...
    <br>
    <br>
    If that doesn't happen, click <a href="http://localhost:8080/homepage">here</a>
</body>
<script>
    function onLoadHandler()
    {
        console.log("Working...")
        window.location.replace("/homepage");
    }
    window.addEventListener("load", onLoadHandler)
</script>
</html>
