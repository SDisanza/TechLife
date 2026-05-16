const map = L.map('map').setView([41.9, 12.6], 6);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap'
}).addTo(map);
$('#loader').show(); 
map.locate({setView: true, maxZoom: 11, timeout: 30000});
map.on('locationfound', onLocationFound);
map.on('locationerror', onLocationError);

let redIcon = new L.Icon(
    {
        iconUrl: 'img/marker-icon-2x-red.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
});

function onLocationFound(e)
{
	$('#loader').hide();
	$('#ecommerce-message').removeClass('hidden-start').hide().fadeIn(500);
	map.invalidateSize();
	const marker = L.marker(e.latlng, {icon: redIcon}).addTo(map);
    latlngUser = e.latlng;
    radius = e.accuracy;


    marker.bindPopup("Siamo qui!").openPopup();
    L.circle(latlngUser,
        {
            color: "yellow",
            radius: radius
        }).addTo(map);
}

function onLocationError(e) {
	$('#loader').hide();
    alert(e.message);
    document.getElementById("text").textContent =
    ('Ricorda che se non autorizzi l\'accesso alla posizione non potrai utilizzare il servizio');
}

function generaPasswordSicura() {
	const inputPassword = document.getElementById("password");
	const btnToggle = document.getElementById("btn-toggle-password");
	const caratteri = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+~`|}{[]:;?><,./-=";
	const lunghezza = 14;
	let passwordGenerata = "";
	  
	for (let i = 0; i < lunghezza; i++) {
		const indiceCasuale = Math.floor(Math.random() * caratteri.length);
		passwordGenerata += caratteri.charAt(indiceCasuale);
	}
	  
	inputPassword.value = passwordGenerata;
	inputPassword.type = "text"; 
	btnToggle.textContent = "🙈"; 
}

function togglePasswordVisibilita() {
	const inputPassword = document.getElementById("password");
	const btnToggle = document.getElementById("btn-toggle-password");
	  
	if (inputPassword.type === "password") {
		inputPassword.type = "text";
		btnToggle.textContent = "🙈"; 
	} else {
		inputPassword.type = "password";
		btnToggle.textContent = "👁️"; 
	}
}

