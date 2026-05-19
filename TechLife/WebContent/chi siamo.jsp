<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.UtenteBean" %>
<% UtenteBean utenteChiSiamo = (UtenteBean) session.getAttribute("utente");
    String homeDestinazione = "index.jsp"; // Default per utenti non registrati
    if (utenteChiSiamo != null) {
        homeDestinazione = "ALogin/homelogin.jsp"; // Se è loggato, torna alla home privata
    }%>
<!DOCTYPE html>
<html lang="it">
	<head>
		<meta charset="UTF-8">
		<title>Chi Siamo - Tech Life</title>
		<link type="text/css" rel="stylesheet" href="style.css">
	</head>
	<body>
		
		<div class="nav-back">
			<a href="<%= homeDestinazione %>">&larr; Torna alla Home</a>
		</div>

		<h1>La Nostra Missione</h1>
		
		<div id="about-container">
			
			<div class="about-card">
				<h2>Chi Siamo</h2>
				<p class="about-text">
					<strong>Tech Life</strong> nasce con un obiettivo chiaro e ambizioso: essere il primo sito di e-commerce dove poter acquistare dispositivi salvavita in base alle proprie reali esigenze. 
				</p>
				<p class="about-text">
					Abbiamo deciso di rompere gli schemi del mercato tradizionale: con noi, <strong>niente piÃ¹ cataloghi complessi o scontistiche vincolate a pacchetti aggiuntivi</strong> obbligatori. Crediamo che la sicurezza e la protezione della vita non debbano essere ostacolate da burocrazia commerciale o costi nascosti.
				</p>
				<p class="about-text">
					La nostra missione Ã¨ abbattere le barriere d'accesso alla cardioprotezione, permettendo a privati, aziende, scuole e palestre di comprare i dispositivi salvavita a seconda delle proprie necessitÃ , in modo <strong>facile, trasparente e veloce</strong>. 
				</p>
			</div>

			
			<div class="values-grid">
				<div class="value-item">
					<h3>Trasparenza</h3>
					<p>Prezzi chiari fin da subito. Nessun preventivo personalizzato o venditore insistente.</p>
				</div>
				<div class="value-item">
					<h3>AccessibilitÃ </h3>
					<p>Un processo guidato e rapido per permettere a chiunque di salvare una vita.</p>
				</div>
				<div class="value-item">
					<h3>Su Misura</h3>
					<p>Scegli solo il defibrillatore adatto al tuo ambiente, senza accessori inutili.</p>
				</div>
			</div>
		</div>
		
	</body>
</html>
