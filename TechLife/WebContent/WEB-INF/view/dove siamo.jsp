<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.UtenteBean" %>
<% UtenteBean utenteDoveSiamo = (UtenteBean) session.getAttribute("utente");
    String homeDestinazione = "index.jsp";
    if (utenteDoveSiamo != null) {
        homeDestinazione = "ALogin/homelogin.jsp";
    }
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Dove Siamo</title>
		
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
		<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.3/dist/leaflet.css"
          integrity="sha256-kLaT2GOSpHechhsozzB+flnD+zUyjE2LlfWPgU04xyI="
          crossorigin=""/>
	    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	    <script src="https://unpkg.com/leaflet@1.9.3/dist/leaflet.js"
	            integrity="sha256-WBkoXOwTeyKclOHuWtc+i2uENFpDZ9YPdf5Hf+D7ewM="
	            crossorigin=""></script>
	</head>
	<body>
		<div class="nav-back">
		    <% if (utenteDoveSiamo != null) { %>
		        <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=homelogin">&larr; Torna alla Home</a>
		    <% } else { %>
		        <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=home">&larr; Torna alla Home</a>
		    <% } %>
		</div>
		<h1>Dove Siamo</h1>				
			<div id="mapcontainer">
				<div class="map-wrapper">
					<div id="loader" class="loader">
				    	<i class="fas fa-spinner fa-spin"></i>
					</div>
							
				<div id="map">
				</div>
				</div>
			    <div id="control">
					<div id="ecommerce-message" class="ecommerce-info-box hidden-start">					
					<i class="fas fa-laptop-medical info-icon"></i>
					<h2>Tech Life È Digitale</h2>
					<p class="main-message">Dovunque. Ovunque tu ne abbia bisogno, cosi da supportarti sempre.</p>
					<div class="divider"></div>
					<p class="sub-message">
						Essendo un e-commerce specializzato, non abbiamo un negozio fisico aperto al pubblico. Questo ci permette di abbattere i costi di gestione e garantirti i migliori prodotti sul mercato a prezzi d'ingrosso, senza pacchetti aggiuntivi.
					</p>
					<p class="sub-message">
						Spediamo in tutta Italia in 24/48 ore con corriere espresso assicurato.
					</p>
				</div>
			    <p id="text"></p>
			</div>
		</div>
		<script src="${pageContext.request.contextPath}/script/mappa.js"></script>
	</body>
</html>