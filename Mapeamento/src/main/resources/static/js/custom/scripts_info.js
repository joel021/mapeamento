var lat = -12.7341215010;
var lon = -39.1847991943;
var selected = false;

function select(item){
	if(item == 1){
            $("#endereco0").css("display","block");
            $("#endereco1").css("display","block");
            $("#geolocalizacao").css("display", "none");

            $("#b1").css("background", "#eceeef");
            $("#b0").css("background", "#fff");

            $("#d").css("max-width", "100%");
	}else{
            $("#d").css("max-width", "500px");
            $("#endereco0").css("display", "none");
            $("#endereco1").css("display", "none");
            $("#geolocalizacao").css("display", "block");

            $("#b1").css("background", "#fff");
            $("#b0").css("background", "#eceeef");
	}
}

function main(){
    window.location.href="/";
}

function selectLocation(){
    $("#mapDIV").css("display", "table");
}
function requestLocationForUser() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(getLocationFromUser);
    
  } else { 
    doInMap(lat, lon);
  }
}

function getLocationFromUser(position) {    
    doInMap(position.coords.latitude,position.coords.longitude);
}
function onclickMap(e){
    
    lat = e.latlng.lat;
    lon = e.latlng.lng;
    
    $("#mapDIV").css("display", "none");
    $("#local").html("<p><b>Selecionado</b></p><p>Latitude: "+lat+"</p><p>Longitude: "+lon+"</p>");
    selected = true;
}

function getInfosFromCoord(lat, lon){
    
    $.ajax({
        url: 'https://nominatim.openstreetmap.org/reverse',
        data: {
            format: 'jsonv2',
            lat: lat,
            lon: lon
        },
        cache: false,
        type: "GET",
        success: function(r){
            addRegistry(r);
        },
        error: function(e){
        	$("#progress").css("display", "none");
            document.getElementById("button_inf").disabled = true;
            $("#messege").html('<div class="alert alert-danger" role="alert">Não foi possível identificar o local!</div>');
        }
    });
}

function defineSelectCitys(){
    new dgCidadesEstados({
        cidade: document.getElementById('cidades'),
        estado: document.getElementById('estados')
    });
}
function setPoint(map, x,y, bind){
    
    var earthquakeMarker = L.circleMarker([x, y], 
        {
            radius: 4
        });
    earthquakeMarker.bindPopup(bind);
    earthquakeMarker.addTo(map);
}

function doInMap(x,y){
    
    var map = L.map(document.getElementById('mapDIV'), {
        center: [x, y],
        zoom: 5
        });
    var basemap = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
        });
        basemap.addTo(map);
        
    setPoint(map, x,y,"Sua localização");
    
    var coordDIV = document.createElement('div');
    coordDIV.id = 'mapCoordDIV';
    coordDIV.style.position = 'absolute';
    coordDIV.style.bottom = '0';
    coordDIV.style.left = '0';
    coordDIV.style.zIndex = '900';

    document.getElementById('mapDIV').appendChild(coordDIV);

    map.on('mousemove', function(e){
          var lat = e.latlng.lat.toFixed(3);
          var lon = e.latlng.lng.toFixed(3);
          document.getElementById('mapCoordDIV').innerHTML = lat + ' , ' + lon;
           });
    coordDIV.style.bottom = '1px';
    coordDIV.style.left = '150px';

    map.on('click', onclickMap);
}

function addRegistry(r){
	
	if(r.address == null){
		$("#progress").css("display", "none");
		document.getElementById("button_inf").disabled = false;
		$("#messege").html('<div class="alert alert-danger" role="alert">Não foi possível identificar o local!</div>');
		return;
	}
	var info = $("#info").val();
	var situation = $("#situation").val();
	var types = $("#types").val();
	
	$.ajax({
        url: '/private/registry_info/',
        type: 'post',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({
        	lat: lat,
    		lon: lon,
    		city: (r.address.city_district != null) ? r.address.city_district:  r.address.town,
    		state: r.address.state,
    		hamlet: (r.address.hamlet != null) ? r.address.hamlet: r.address.suburb,
    		country: r.address.country,
    		postcode: r.address.postcode,
    		info: info,
    		situation: situation,
    		types: types
        	}),
        success: function(r){
            console.log("Success "+r);
            $("#progress").css("display", "none");
            $("#messege").html('<div class="alert alert-success" role="alert">Cadastrado com sucesso!</div>');
            document.getElementById("button_inf").disabled = false;
        },
        error: function(e){
            console.log("erro "+e);
            $("#progress").css("display", "none");
            $("#messege").html('<div class="alert alert-danger" role="alert">Não foi possível, tente novamente!</div>');
            document.getElementById("button_inf").disabled = false;
        }
    });
    
}


window.onload = function() {
	requestLocationForUser();
};

$("#form_info").submit(function() {
	
	$("#progress").css("display", "table");
	document.getElementById("button_inf").disabled = true;
	
	if(selected){
		getInfosFromCoord(lat, lon);
	}else{
		doInMap(la,lon);
		document.getElementById("button_inf").disabled = false;
		$("#messege").html("<p style='color:red'>Toque no mapa!</p>");
	}
	
	return false;
});

