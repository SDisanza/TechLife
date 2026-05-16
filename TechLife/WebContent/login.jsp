<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Login</title>
	<link type="text/css" rel="stylesheet" href="style.css">
</head>
<body>
	<div class="nav-back">
			<a href="index.html">&larr; Torna alla Home</a>
	</div>
	
	<div class="login-container-page">
		<div class="login-card">
			<h2>Accedi a Tech Life</h2>
			
			<form action="LoginServlet" method="post">
				
				<div class="input-group">
					<label>Email</label>
					<input type="email" name="email" required placeholder="Inserisci la tua email">
				</div>
				
				<div class="input-group">
					<label>Password</label>
					<input type="password" name="password" required placeholder="••••••••">
				</div>

				<div class="button-group">
					<input type="submit" value="Accedi" class="btn-submit">
					<input type="reset" value="Annulla" class="btn-reset">
				</div>
				
				<div class="divider"></div>
				
				<p class="register-text">
					Se non sei ancora nostro cliente, <a href="registrazione.jsp">REGISTRATI</a>
				</p>
			</form>
		</div>
	</div>
</body>
</html>