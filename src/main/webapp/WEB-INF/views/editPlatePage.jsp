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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="/img/chef'skiss_logo_emoji.png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<style>
    .food_background{
        background-image: url("img/food_background_v2.jpg");
        background-size: contain;
    }

    .white_transp_background{
        background-color: rgba(245,245,245,0.7);
    }
</style>
<script>
        <% if(contenuto.size()>0){ %>
        var i = <%=contenuto.size()%>
        <% }else{ %>
        var i = 1;
        <% } %>

        function addIngrediente() {
            var ingredienteDiv = document.getElementById("lista_ingredienti");
            var ingredienteSelect = document.getElementById("ingrediente"+i);
            var ingredienteQuant = document.getElementById("quantita1");
            var selectedIngredienteIndex = ingredienteSelect.options.selectedIndex.valueOf();

            if(ingredienteSelect.options.length.valueOf()>1 && i<10)
            {
                i++;

                var newInpGroup = document.createElement("div");
                newInpGroup.setAttribute("class", "input-group");
                var newSelect = ingredienteSelect.cloneNode(true);
                var newQuantity = ingredienteQuant.cloneNode(false);
                newSelect.attributes.id.value = "ingrediente"+i;
                //newSelect.attributes.name.value = "ingrediente"+i;
                newSelect.options.remove(selectedIngredienteIndex);
                newQuantity.attributes.id.value = "quantita"+i;
                //newQuantity.attributes.name.value = "quantita"+i;
                newInpGroup.appendChild(newSelect);
                newInpGroup.appendChild(newQuantity);
                //newInpGroup.appendChild(document.createElement("br"));
                ingredienteDiv.appendChild(newInpGroup);

            }
        }

        function removeIngrediente(){
            if (i>1)
            {
                var ingredienteSelectDiv = document.getElementById("lista_ingredienti");
                ingredienteSelectDiv.lastElementChild.remove();

                i--;
            }
        }
</script>
<body class="food_background">
    <div class="card m-3 white_transp_background shadow">
        <div class="card-header">
            <h1>Modifica la tua creazione!</h1>
            <% if (utente.isPrivato()) { %>
            <h3><%=utente.getUsername()%>, modifica la tua ricetta</h3>
            <% } else { %>
            <h3>Chef <%=utente.getNome()+" "+utente.getCognome()%>, modifica la tua ricetta</h3>
            <% } %>
        </div>
        <div class="card-body">
            <form method="post">
                <input type="hidden" name="id" value="<%=piatto.getId()%>">
                <label class="form-label" for="nome_piatto">Nome del piatto: </label>
                <input class="form-control" type="text" id="nome_piatto" name="nomePiatto" value="<%=piatto.getNome()%>" required>
                <% if (utente.isPrivato()) { %>
                <label class="form-label" for="preparazione">Preparazione: </label>
                <textarea class="form-control" id="preparazione" name="preparazione" cols="100" rows="20" maxlength="3000" required><%=piatto.getPreparazione()%></textarea>
                <% } else { %>
                <label class="form-label" for="preparazione">Preparazione (opzionale): </label>
                <textarea class="form-control" id="preparazione" name="preparazione" cols="100" rows="20" maxlength="3000"><%=piatto.getPreparazione()%></textarea>
                <% } %>
                <label class="form-label" for="lista_ingredienti">Lista ingredienti (max 10) + Quantità (in grammi): </label>
                <div id="lista_ingredienti">
                    <% if (contenuto.size()==0) { %>
                    <div class="input-group">
                        <select class="form-select-sm" id="ingrediente1" name="ingredienti">
                            <% for (int i = 0; i < listaIngredienti.size(); i++) { %>
                            <option value="<%=listaIngredienti.get(i).getNome()%>"><%=listaIngredienti.get(i).getNome()%></option>
                            <% } %>
                        </select>
                        <input class="form-control-sm" type="number" min="1" step="1" id="quantita1" name="quantita">
                        <br>
                    </div>
                    <% } %>
                    <% for (int j = 0; j < contenuto.size(); j++) { %>
                    <div class="input-group">
                    <select class="form-select-sm" id="ingrediente<%=j+1%>" name="ingredienti">
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
                    <input class="form-control-sm" type="number" min="1" step="1" id="quantita1" name="quantita" value="<%=contenuto.get(j).getQuantita()%>">
                    <br>
                    </div>
                    <% } %>
                </div>
                <button type="button" class="btn btn-success my-2" style="border-radius: 10px" onclick="addIngrediente()"><i class='bx bxs-plus-circle bx-sm'></i></button>
                <button type="button" class="btn btn-danger my-2" style="border-radius: 10px" onclick="removeIngrediente()"><i class='bx bxs-minus-circle bx-sm'></i></button>
                <div class="my-3">
                    <input class="btn btn-success my-2" type="submit" value="Ok">
                    <input class="btn btn-danger my-2" type="reset" value="Annulla" onclick=window.history.back()>
                </div>
            </form>
        </div>
    </div>
    <%@include file="repetedElements/backLink.jsp"%>
</body>
</html>
