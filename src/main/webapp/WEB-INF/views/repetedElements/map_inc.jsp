<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 17/10/2023
  Time: 12:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://openlayers.org/en/v6.13.0/build/ol.js"></script>
<link rel="stylesheet" href="https://openlayers.org/en/v6.13.0/css/ol.css" type="text/css">
<style>
  #map {
    position: relative;
    width: 100%;
    height: 500px;
  }
  #info {
    position: absolute;
    top: 10px;
    left: 10px;
    background-color: white;
    padding: 5px;
    z-index: 100;
    white-space: pre-wrap;
  }
</style>
<div class="w-100 shadow" id="map">
  <div id="info"></div>
</div>

<script>
  var map = new ol.Map({
    target: 'map',
    layers: [
      new ol.layer.Tile({
        source: new ol.source.OSM()     // mappa di base di openalayer
      })
    ],
    view: new ol.View({
      center: ol.proj.fromLonLat([12.5674, 41.8719]), // Coordinate di Roma
      zoom: 6
    })
  });

  var markerLayer = new ol.layer.Vector({
    source: new ol.source.Vector()
  });
  map.addLayer(markerLayer);

  var pointerMoveInteraction = new ol.interaction.Pointer({
    handleMoveEvent: function(event) {
      var coordinate = event.coordinate;
      var pixel = event.pixel;
      var hit = map.forEachFeatureAtPixel(pixel, function(feature) {
        return feature;
      });
      if (hit) {
        var infoElement = document.getElementById('info');
        var name = hit.get('name');
        infoElement.innerHTML = name;
        infoElement.style.display = 'block';
        infoElement.style.left = pixel[0] + 10 +'px';
        infoElement.style.top = (pixel[1] - 15) + 'px';
      } else {
        document.getElementById('info').style.display = 'none';
      }
    }
  });
  map.addInteraction(pointerMoveInteraction);

  var marker = []
  <%for(int i=0; i<sedi.size(); i++){%>
  <%
    Float lat = Float.parseFloat(sedi.get(i).getCoordinate().split(";")[0]);
    Float lon = Float.parseFloat(sedi.get(i).getCoordinate().split(";")[1]);
  %>
  marker.push(
          new ol.Feature({
            geometry: new ol.geom.Point(ol.proj.fromLonLat([<%=lon%>,<%=lat%>])),
            name: 'Sede presso <%=sedi.get(i).getVia()%>, <%=sedi.get(i).getCitta()%>\nDel ristorante <%=ristorante.getNome()%>'
          }))
  <%}%>

  for(let i=0; i<marker.length; i++) markerLayer.getSource().addFeature(marker[i]);
</script>
