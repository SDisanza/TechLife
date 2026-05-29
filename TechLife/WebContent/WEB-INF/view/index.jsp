<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Tech Life Home</title>
		<link type="text/css" rel="stylesheet" href="style.css">
	</head>
	<body>
	<%if (session != null && session.getAttribute("utente") != null) 
	{
        response.sendRedirect("ALogin/homelogin.jsp");
        return;
    }%>
		<h1>Benvenuto in Tech Life!</h1><br>
		<p class="welcometext">Se sei nuovo in TechLife <a id="register" href="${pageContext.request.contextPath}/NavigazioneServlet?page=registrazione">REGISTRATI</a>
		<p>e scopri il nostro vasto catalogo;<br>
		oppure accedi e scopri i nostri prodotti al miglior prezzo sul mercato.<br><br>
		<div id="container">
		
			<div class="divHome">
				<a href="${pageContext.request.contextPath}/NavigazioneServlet?page=login">
				<img src="img/index/profiloblu.png" alt="Login">
				<span>Login</span></a>
			</div>
			
			<div class="divHome">
				<a href="${pageContext.request.contextPath}/NavigazioneServlet?page=catalogologin">
				<img src="img/index/catalogoblu.png" alt="Catalogo">
				<span>Catalogo</span></a>
			</div>
			
			<div class="divHome">
				<a href="${pageContext.request.contextPath}/NavigazioneServlet?page=chisiamo">
				<img src="img/index/chisiamoblu.png" alt="Chi Siamo">
				<span>Chi Siamo</span></a>
			</div>
			
			<div class="divHome">
				<a href="${pageContext.request.contextPath}/NavigazioneServlet?page=dovesiamo">
				<img src="img/index/mappablu.png" alt="Dove Siamo">
				<span>Dove Siamo</span></a>
			</div>
		</div>
	</body>
</html>