<%@ page import="com.project.chefskiss.modelObjects.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.modelObjects.Ingrediente" %><%--
  Created by IntelliJ IDEA.
  User: david
  Date: 27/09/2023
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<%
    User utente = (User)request.getAttribute("user");
    List<Ingrediente> listaIngredienti = (List<Ingrediente>)request.getAttribute("listaIngredienti");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Aggiungi la tua creazione</title>
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
    <h1>Aggiungi la tua creazione!</h1>
    <% if (utente.isPrivato()) { %>
    <h3><%=utente.getUsername()%>, che ricetta vuoi condividere oggi?</h3>
    <% } else { %>
    <h3>Chef <%=utente.getNome()+" "+utente.getCognome()%>, che ricetta vuoi condividere oggi?</h3>
    <% } %>
    <form method="post">
        <label for="nome_piatto">Nome del piatto: </label>
        <input type="text" id="nome_piatto" name="nomePiatto" required>
        <br>
        <% if (utente.isPrivato()) { %>
        <label for="preparazione">Preparazione: </label>
        <br>
        <textarea id="preparazione" name="preparazione" cols="100" rows="20" maxlength="3000" required></textarea>
        <br>
        <% } else { %>
        <label for="preparazione">Preparazione (opzionale): </label>
        <textarea id="preparazione" name="preparazione" cols="100" rows="20" maxlength="3000"></textarea>
        <br>
        <% } %>
        <label for="lista_ingredienti">Lista ingredienti (max 10) + Quantità (in grammi): </label>
        <br>
        <div id="lista_ingredienti">
            <select id="ingrediente1" name="ingredienti">
                <% for (int i = 0; i < listaIngredienti.size(); i++) { %>
                <option value="<%=listaIngredienti.get(i).getNome()%>"><%=listaIngredienti.get(i).getNome()%></option>
                <% } %>
            </select>
            <input type="number" min="1" step="1" id="quantita1" name="quantita">
            <br>
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
</body>
</html>
