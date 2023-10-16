<%@ page import="java.util.List" %>
<%@ page import="com.project.chefskiss.modelObjects.Prenotazione" %><%--
  Created by IntelliJ IDEA.
  User: spipp
  Date: 05/10/2023
  Time: 11:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Prenotazione> prenotazioni = (List<Prenotazione>) request.getAttribute("prenotazioni");
%>
<html>
<head>
    <title>Le tue prenotazioni</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
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
<body class="food_background">
    <div class="card m-3 white_transp_background shadow">
        <div class="card-header">
            <h2 class="text-center">LE TUE PRENOTAZIONI</h2>
        </div>
        <div class="card-body">
            <div class="container my-2">
                <div class="row">
                    <% for (int i = 0; i < prenotazioni.size(); i++) { %>
                    <div class="col-md-4 mb-3">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Prenotazione numero: <%= prenotazioni.get(i).getID() %></h5>
                                <p class="card-text">
                                    Presso: <%= prenotazioni.get(i).getSedeP().getVia() %>, <%= prenotazioni.get(i).getSedeP().getCitta() %><br>
                                    Data: <%= prenotazioni.get(i).getData() %><br>
                                    Orario: <%= prenotazioni.get(i).getOrario() %><br>
                                    Posti: <%= prenotazioni.get(i).getN_Posti() %>
                                </p>
                            </div>
                            <div class="card-footer">
                                <a href="/editPrenotazione?id=<%= prenotazioni.get(i).getID() %>&coordinate=<%= prenotazioni.get(i).getSedeP().getCoordinate() %>" class="btn btn-primary"><i class='bx bxs-edit-alt'></i> Modifica</a>
                                <button class="btn btn-danger" onclick="confermaCancellazione(<%= prenotazioni.get(i).getID() %>)"><i class='bx bxs-trash'></i> Cancella</button>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="repetedElements/backLink.jsp"%>
</body>
<script>

    function confermaCancellazione(id_prenotazione) {
        var richiesta = window.confirm("Sei sicuro di voler cancellare la tua prenotazione?");
        if (richiesta) {
            window.location.replace("/deletePrenotazione?id="+id_prenotazione);
        }
        //else // visualizzare pagina della sede
    }

    function getErrorMessage(){
        var url = window.location.search;
        url = url.substring(1);
        var parametri = url.split("&");

        for (var i = 0; i < parametri.length; i++){
            var coppia = parametri[i].split("=");
            var nomeParametro = decodeURIComponent(coppia[0]);
            var valore = decodeURIComponent(coppia[1]);
            var messaggio;

            if (nomeParametro === "error"){
                console.log("messaggio");
                if (valore == 1) messaggio = "Prenotazione non effettuata! \nGià esistente con gli stessi dati!"

                alert(messaggio);
                break;
            }
        }
    }
    window.addEventListener("load", getErrorMessage());
</script>
</html>
