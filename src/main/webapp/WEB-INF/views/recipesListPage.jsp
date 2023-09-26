<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 24/09/2023
  Time: 19:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User utente = (User)request.getAttribute("user");
    List<Piatto> piatti = (List<Piatto>)request.getAttribute("listaPiatti");
    Boolean searched = (Boolean)request.getAttribute("searched");
%>
<html>
<head>
    <title>La nostra lista di Piatti</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<script>
    var i = 0;
    function addAllergene(){
        var allergeneDiv = document.getElementById("allergene_div");
        if(i==0){
            var allergeneTag = document.createElement("p");
            var text = document.createTextNode("Elenco allergeni:");
            allergeneTag.append(text);
            allergeneDiv.append(allergeneTag);
        }
        var allergeneNode = document.createElement("input");
        allergeneNode.setAttribute("name", "allergene"+i.toString());
        allergeneNode.setAttribute("type", "text");
        allergeneDiv.appendChild(allergeneNode);
        i++;
        if(i==5) document.getElementById("searchForm").removeChild(document.getElementById("add_allergene"));
    }
</script>
<body>
    <h1>LA NOSTRA LISTA DEI PIATTI</h1>
    <h3>Voglia di qualcosa in particolare? Cerca ciò che più ti fa gola!</h3>
    <div>
        <form id="searchForm">
            <label for="searchType">Cerca per:</label>
            <select id="searchType" name="searchType">
                <option value="1">Nome del piatto</option>
                <option value="2">Ingrediente</option>
            </select>
            <br>
            <input id="search" type="search" placeholder="Search.." name="search">
            <br>
            <div id="allergene_div"></div>
            <input id="add_allergene" type="button" onclick="addAllergene()" value="Aggiungi un allergene">
            <br>
            <button type="submit"><i class='bx bx-search-alt-2 bx-sm'></i></button>
        </form>
    </div>
    <%      if (searched){    %>
        <h3>I risultati della tua ricerca</h3>
    <%      } else {    %>
        <h3>Oppure, se hai solo voglia di cucinare, lasciati ispirare da una di queste ricette...</h3>
    <%      }       %>
    <%for (int i = 0; i < piatti.size(); i++) {%>
        <a href="http://localhost:8080/plate?id=<%=piatti.get(i).getId()%>">
                <div id="recipeBlock">
                    <p><%=piatti.get(i).getNome()%></p>
                    <img src="<%=piatti.get(i).getStarsRating()%>" width="20%" height="auto">
                </div>
        </a>
    <%}%>
    <%@ include file="repetedElements/homepageLink.html"%>
</body>
</html>