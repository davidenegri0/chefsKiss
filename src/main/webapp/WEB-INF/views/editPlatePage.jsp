<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.modelObjects.Ingrediente" %>
<%@ page import="com.project.chefskiss.modelObjects.Contiene" %>
<%@ page import="com.project.chefskiss.modelObjects.Piatto" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 27/09/2023
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<%
    User utente = (User)request.getAttribute("user");
    Piatto piatto = (Piatto) request.getAttribute("piatto");
    List<Contiene> contenuto = (List<Contiene>) request.getAttribute("contenutoPiatto");
    List<Ingrediente> listaIngredienti = (List<Ingrediente>)request.getAttribute("listaIngredienti");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Aggiungi la tua creazione</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<script>
        var i = 1;

        function addIngrediente() {
            var ingredienteDiv = document.getElementById("lista_ingredienti");
            var ingredienteSelect = document.getElementById("ingrediente"+i);
            var ingredienteQuant = document.getElementById("quantita1");
            var selectedIngredienteIndex = ingredienteSelect.options.selectedIndex.valueOf();

            if(ingredienteSelect.options.length.valueOf()>1 && i<10)
            {
                i++;

                var newSelect = ingredienteSelect.cloneNode(true);
                var newQuantity = ingredienteQuant.cloneNode(false);
                newSelect.attributes.id.value = "ingrediente"+i;
                //newSelect.attributes.name.value = "ingrediente"+i;
                newSelect.options.remove(selectedIngredienteIndex);
                newQuantity.attributes.id.value = "quantita"+i;
                //newQuantity.attributes.name.value = "quantita"+i;
                ingredienteDiv.appendChild(newSelect);
                ingredienteDiv.appendChild(newQuantity);
                ingredienteDiv.appendChild(document.createElement("br"));
            }
        }

        function removeIngrediente(){
            if (i>1)
            {
                var ingredienteSelect = document.getElementById("ingrediente"+i);
                var ingredienteQuant = document.getElementById("quantita"+i);
                ingredienteQuant.nextElementSibling.remove();
                ingredienteQuant.remove();
                ingredienteSelect.remove();


                i--;
            }
        }
</script>
<body>
    <h1>Modifica la tua creazione!</h1>
    <% if (utente.isPrivato()) { %>
    <h3><%=utente.getUsername()%>, modifica la tua ricetta</h3>
    <% } else { %>
    <h3>Chef <%=utente.getNome()+" "+utente.getCognome()%>, modifica la tua ricetta</h3>
    <% } %>
    <form method="post">
        <input type="hidden" name="id" value="<%=piatto.getId()%>">
        <label for="nome_piatto">Nome del piatto: </label>
        <input type="text" id="nome_piatto" name="nomePiatto" value="<%=piatto.getNome()%>" required>
        <br>
        <% if (utente.isPrivato()) { %>
        <label for="preparazione">Preparazione: </label>
        <br>
        <textarea id="preparazione" name="preparazione" cols="100" rows="20" maxlength="3000" required><%=piatto.getPreparazione()%></textarea>
        <br>
        <% } else { %>
        <label for="preparazione">Preparazione (opzionale): </label>
        <textarea id="preparazione" name="preparazione" cols="100" rows="20" maxlength="3000"><%=piatto.getPreparazione()%></textarea>
        <br>
        <% } %>
        <label for="lista_ingredienti">Lista ingredienti (max 10) + Quantità (in grammi): </label>
        <br>
        <div id="lista_ingredienti">
            <% if (contenuto.size()==0) { %>
            <select id="ingrediente1" name="ingredienti">
                <% for (int i = 0; i < listaIngredienti.size(); i++) { %>
                <option value="<%=listaIngredienti.get(i).getNome()%>"><%=listaIngredienti.get(i).getNome()%></option>
                <% } %>
            </select>
            <input type="number" min="1" step="1" id="quantita1" name="quantita">
            <br>
            <% } %>
            <% for (int j = 0; j < contenuto.size(); j++) { %>
            <select id="ingrediente<%=j+1%>" name="ingredienti">
                <% for (int i = 0; i < listaIngredienti.size(); i++) { %>
                    <% if (contenuto.get(j).getIngredienteC().getNome().equals(listaIngredienti.get(i).getNome())) { %>
                    <option value="<%=listaIngredienti.get(i).getNome()%>" selected>
                        <%=listaIngredienti.get(i).getNome()%>
                    </option>
                    <% } else { %>
                    <option value="<%=listaIngredienti.get(i).getNome()%>">
                        <%=listaIngredienti.get(i).getNome()%>
                    </option>
                    <% } %>
                <% } %>
            </select>
            <input type="number" min="1" step="1" id="quantita1" name="quantita" value="<%=contenuto.get(j).getQuantita()%>">
            <br>
            <% } %>

        </div>
        <br>
        <input type="button" value="Aggiungi" onclick="addIngrediente()">
        <input type="button" value="Rimuovi" onclick="removeIngrediente()">
        <br>
        <br>
        <br>
        <input type="submit" value="Ok">
        <input type="reset" value="Annulla">
    </form>
    <%@include file="repetedElements/homepageLink.html"%>
</body>
</html>
