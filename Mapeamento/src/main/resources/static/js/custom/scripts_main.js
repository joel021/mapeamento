var registers = null;
var selected = false;
var lat = -15.786740;
var lon = -47.887570;
var map = null;

function requestLocationForUser() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(getLocationFromUser);
  }
}

function createGraphics(listRegisters){
    console.log("q: "+listRegisters.length);
    
    var labels = ["Suspeito", "Confirmado", "Solucionado"];
    var seriesConfirmed = 0;
    var seriesSuspect = 0;
    var seriesSolved = 0;
    var i = 0;
    
    for(i = 0; i < listRegisters.length; i++){
    	    	
    	if(listRegisters[i].situation.indexOf("Confirmado") > -1){
			seriesConfirmed +=1;
		}else if(listRegisters[i].situation.indexOf("Suspeito") > -1){
			
			seriesSuspect +=1;
			
		}else if(listRegisters[i].situation.indexOf("Solucionado") > -1){
			seriesSolved += 1;
		}
    	
    }
    if(i == 0){
    	$("#char1").html("<p> Nenhuma estatística</p>");
    	$("#char2").html("<p> Nenhuma estatística</p>");
    	return;
    }
    
    var data = {
        labels: labels,
        series: [ [seriesConfirmed, seriesSuspect, seriesSolved] ]
    };
        
    var options = { 
    	high: Math.max.apply(null,[seriesConfirmed,seriesSuspect, seriesSolved])+1,
        showPoint: false,
        lineSmooth: true,
        fullWidth: true,
        showArea: true,
        charPadding: {
            right:20
        },
        axisX: {
            showGrid:true,
            showLabel: true
        },
        axisY: {
            onlyInteger: true,
            offset: 20
        }
    };
    
    
    
    new Chartist.Bar('#char1', data, options);
    
    var s = seriesSolved+seriesSuspect+seriesConfirmed;
    
    var data2 = {
    	labels: ["Suspeito (%)", "Confirmado (%)", "Solucionado (%)"],
    	series: [[(seriesSuspect/s)*100, (seriesConfirmed/s)*100,(seriesSolved/s)*100]]
    }
    
    var options2 = { 
        	high: 100,
            showPoint: false,
            lineSmooth: true,
            fullWidth: false,
            charPadding: {
                right:0,
                left: 50
            },
            axisX: {
                showGrid:true,
                showLabel: true
            },
            axisY: {
                onlyInteger: false,
                offset: 20,
                labelInterpolationFnc: function(value){
                	return value + '%'
                },
                scaleMinSpace: 15
            }
        };
    
    new Chartist.Bar('#char2', data2, options2);
}

function getLocationFromUser(position) {  
    lat = position.coords.latitude;
    lon = position.coords.longitude;
    
    map = createMap(lat, lon);
}

function onclickMap(e){
	$("#progress").css("display", "block");
	
    lat = e.latlng.lat.toFixed(10);
    lon = e.latlng.lng.toFixed(10);
    selected = true;
    // mudou o estado, cidade ou país.
    map.removeLayer(this);
    getInfosFromCoord(lat, lon);
}

// funcção principal
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
        	console.log("state: "+r.address.state+" city: "+r.address.city);
            resultInfosFromCoord(r);
        },
        error: function(e){
        	console.log("getinfosFromCood don't k");
        	$("#load").css("display", "none");
        	$("#progress").css("display", "none");
        }
    });
}

function setHeatmap(map, X){
    var heat = L.heatLayer(X, 
    {radius: 25}
     ).addTo(map);
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


function createMap(x,y){
    
    var map = L.map(document.getElementById('mapDIV'), {
        center: [x, y],
        zoom: 5
        });
    var basemap = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
        });
        basemap.addTo(map);
        
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
        
    console.log("mapa criado");
    getInfosFromCoord(x, y);
    return map;
}

function getRegisters(x,y){
	
	// buscar lista de pontos lis<register>
	var data = null;
	// saber o nivel
	if(selected){
		//usuário selecionou região
		getInfosFromCoord(lat, lon);
	}else{
		$("#alert").css("display", "block");
	}
}

function setAllRegisters(r){
	var points = [];
	
	for(i = 0; i < r.length; i++){
		console.log("("+r[i].lat+","+r[i].lon+", "+1/r.length);
		points[i] = [r[i].lat+i/10, r[i].lon, 0.5];
	}
	
	var markPoint = true;
	
	if($("#type_registry").val().indexOf("COVID-19") > -1){
		markPoint = false;
	}
	
	if(points.length > 0)
		setHeatmap(map, [[lat, lon, 0.5*points.length]]);
	
	if(points.length > 100){
		
		if(markPoint)
			setHeatmap(map,points);
		else
			setHeatmap(map, [[lat, lon, 0.5*points.length]]);
		
	}else{
		
		if(markPoint){
			for(i = 0; i < points.length; i++){
				var x = points[i];
				setPoint(map, x[0],x[1], r[i].info);
			}
		}else{
			
			if(points.length > 0){
				var p = $("#type_registry").val();
				setPoint(map, lat,lon, r.length+" problemas de "+p+" cadastrados em "+r[0].state+", "+r[0].city);
			}
		}
	}
	
	
	$("#load").css("display", "none");
	$("#progress").css("display", "none");
}
//de getregisters
function resultInfosFromCoord(r){
	
	$.ajax({
        url: '/public/get_registers/',
        type: 'post',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({
        	state: r.address.state,
    		city: r.address.city,
    		country: r.address.country,
    		precision: $("#precision").val(),
    		type: $("#type_registry").val()
        }),
        success: function(r){
        	createGraphics(r);
        	setAllRegisters(r);
        },
        error: function(e){
            $("#progress").css("display", "none");
            $("#load").css("display", "none");
        }
    });
}

function filter(){
	console.log("filter");
	map.removeLayer(this);
	$("#load").css("display", "table");
	$("#progress").css("display", "block");
	getInfosFromCoord(lat, lon);
}

window.onload = function(){
	  $("#load").css("display", "table");
	  
	  requestLocationForUser();
}

