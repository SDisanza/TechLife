<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.UtenteBean" %>
<%UtenteBean utente = (UtenteBean) session.getAttribute("utente");
    if (utente == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    boolean isAzienda = "Azienda / P.IVA".equals(utente.getCognome());
%>
<!DOCTYPE html>
<html lang="it">
	<head>
	    <meta charset="UTF-8">
	    <title>Il Mio Profilo - Tech Life</title>
	    <link type="text/css" rel="stylesheet" href="../style.css">
	</head>
	<body>
	
	    <div class="nav-back">
	        <a href="homelogin.jsp">&larr; Torna alla Home</a>
	    </div>
	
	    <h1>Il Tuo Profilo</h1>
	
	    <div id="profile-container" class="about-card">
	        
	        <div class="profile-header">
	            <h2>Riepilogo Dati Account</h2>
	            <p>Tipologia Account: <strong><%= isAzienda ? "Azienda / Professionista" : "Cliente Privato" %></strong></p>
	        </div>
	
	        <div class="profile-details">
	            <% if (!isAzienda) { %>
	                <div class="info-group">
	                    <span class="info-label">Nome:</span>
	                    <span class="info-value"><%= utente.getNome() %></span>
	                </div>
	                <div class="info-group">
	                    <span class="info-label">Cognome:</span>
	                    <span class="info-value"><%= utente.getCognome() %></span>
	                </div>
	                <div class="info-group">
	                    <span class="info-label">Codice Fiscale:</span>
	                    <span class="info-value"><%= utente.getcodiceFiscale() %></span>
	                </div>
	                <% if (utente.getDataNascita() != null) { %>
	                    <div class="info-group">
	                        <span class="info-label">Data di Nascita:</span>
	                        <span class="info-value"><%= utente.getDataNascita() %></span>
	                    </div>
	                <% } %>
	
	            <% } else { %>
	                <div class="info-group">
	                    <span class="info-label">Ragione Sociale:</span>
	                    <span class="info-value"><%= utente.getNome() %></span>
	                </div>
	                <div class="info-group">
	                    <span class="info-label">Partita IVA:</span>
	                    <span class="info-value"><%= utente.getcodiceFiscale() %></span>
	                </div>
	            <% } %>
	
	            <div class="info-group">
	                <span class="info-label">Indirizzo Email:</span>
	                <span class="info-value"><%= utente.getEmail() %></span>
	            </div>
	        </div>
	
	    </div>
	
	</body>
</html>