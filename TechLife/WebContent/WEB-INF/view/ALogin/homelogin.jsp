<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.UtenteBean" %>
<%UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("utente");%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Tech Life Home</title>
		<link type="text/css" rel="stylesheet" href="../style.css">
	</head>
	<body>
		<% if (utenteLoggato != null) { %>
	            <h1>Ciao, <%= utenteLoggato.getNome() %>!</h1>
	        <% } else { %>
	            <h1>Ciao Utente!</h1>
	        <% } %>
		<p class="welcometext">
		<p>visita il nostro vasto catalogo e inizia a fare acquisti;<br>
		al miglior prezzo sul mercato.<br><br>
		
		<div class="logout-container">
		    <a href="../LogoutServlet" class="btn-logout">
		        <i class="fas fa-sign-out-alt"></i> Esci / Logout
		    </a>
		</div>
		<div id="container">
		
			<div class="divHome">
				<a href="utente.jsp">
				<img src="../img/index/profiloblu.png" alt="Login">
				<span>Utente</span></a>
			</div>
			
			<div class="divHome">
				<a href="catalogologin.jsp">
				<img src="../img/index/catalogoblu.png" alt="Catalogo">
				<span>Catalogo</span></a>
			</div>
			
			<div class="divHome">
				<a href="../chi siamo.jsp">
				<img src="../img/index/chisiamoblu.png" alt="Chi Siamo">
				<span>Chi Siamo</span></a>
			</div>
			
			<div class="divHome">
				<a href="../dove siamo.jsp">
				<img src="../img/index/mappablu.png" alt="Dove Siamo">
				<span>Dove Siamo</span></a>
			</div>
		</div>
	</body>
</html>