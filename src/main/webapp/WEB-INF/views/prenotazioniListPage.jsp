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
<body>
    <h2>Le tue prenotazioni</h2>
    <% if(prenotazioni.isEmpty()){ %>
    <p>
        Sembra che tu non abbia prenotazioni in sospeso...
    </p>
    <% } else { %>
    <%
        Integer id_prenotazione = 0;
        for ( int i = 0; i < prenotazioni.size(); i++){
            id_prenotazione = prenotazioni.get(i).getID();
    %>
    <table>
        <tr>
            <td rowspan="2">
                Prenotazione numero: <%=prenotazioni.get(i).getID()%><br>
                Presso: <%=prenotazioni.get(i).getSedeP().getVia()%>, <%=prenotazioni.get(i).getSedeP().getCitta()%><br>
                Il giorno: <%=prenotazioni.get(i).getData()%> <br>
                Alle ore: <%=prenotazioni.get(i).getOrario()%> <br>
                Per: <%=prenotazioni.get(i).getN_Posti()%> persone
            </td>
            <td>
                <a href="/editPrenotazione?id=<%=prenotazioni.get(i).getID()%>&coordinate=<%=prenotazioni.get(i).getSedeP().getCoordinate()%>"><button><i class='bx bxs-edit-alt'></i></button></a>
            </td>
        </tr>
        <tr>
            <td><button onclick="confermaCancellazione(<%=prenotazioni.get(i).getID()%>)"><i class='bx bxs-trash'></i></button></td>
        </tr>
    </table>
    <br>
    <% }} %>
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
</script>
</html>
