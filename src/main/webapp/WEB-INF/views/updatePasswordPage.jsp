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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<script>
    <% if(errorCode==1){ %>
    alert("La vecchia password non corrisponde!");
    <% } %>
</script>
<body>
    <h2>Aggiorna la tua password</h2>
    <form method="post">
      <label for="oldP">Vecchia Password</label>
      <input id="oldP" type="password" name="oldPassword" required>
        <br>
      <label for="newP">Nuova Password</label>
      <input id="newP" type="password" name="newPassword" required>
        <br>
      <label for="newPRep">Ripeti Nuova Password</label>
      <input id="newPRep" type="password" name="newPasswordRep" required>
        <br>
      <input type="submit" value="Invia">
      <input type="reset" value="Annulla" onclick=window.history.back()>
    </form>
    <%@include file="repetedElements/backLink.jsp"%>
</body>
</html>