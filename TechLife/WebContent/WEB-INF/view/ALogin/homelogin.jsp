<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.UtenteBean" %>
<%
	UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("utente");
	if (utenteLoggato == null) {
    	response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=login");
        return;
    }
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Tech Life Home</title>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
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
		    <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn-logout">Logout</a>
		        <i class="fas fa-sign-out-alt"></i>
		    </a>
		</div>
		<div id="container">
		
			<div class="divHome">
				<a href="${pageContext.request.contextPath}/NavigazioneServlet?page=profilo">
				<img src="${pageContext.request.contextPath}/img/index/profiloblu.png" alt="Profilo">
				<span>Utente</span></a>
			</div>
			
			<div class="divHome">
				<a href="${pageContext.request.contextPath}/NavigazioneServlet?page=catalogologin">
				<img src="${pageContext.request.contextPath}/img/index/catalogoblu.png" alt="Catalogo">
				<span>Catalogo</span></a>
			</div>
			
			<div class="divHome">
				<a href="${pageContext.request.contextPath}/NavigazioneServlet?page=chisiamo">
				<img src="${pageContext.request.contextPath}/img/index/chisiamoblu.png" alt="Chi Siamo">
				<span>Chi Siamo</span></a>
			</div>
			
			<div class="divHome">
				<a href="${pageContext.request.contextPath}/NavigazioneServlet?page=dovesiamo">
				<img src="${pageContext.request.contextPath}/img/index/mappablu.png" alt="Dove Siamo">
				<span>Dove Siamo</span></a>
			</div>
		</div>
	</body>
</html>