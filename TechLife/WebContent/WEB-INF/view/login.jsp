<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Login - Tech Life</title>
    <link type="text/css" rel="stylesheet" href="style.css">
</head>
<body>

    <%
        if (session != null && session.getAttribute("utente") != null) {
            response.sendRedirect("ALogin/homelogin.jsp");
            return;
        }
    %>

    <div class="nav-back">
        <a href="index.jsp">&larr; Torna alla Home</a>
    </div>
    
    <div class="login-container-page">
        <div class="login-card">
            <h2>Accedi a Tech Life</h2>
            
            <% if (request.getAttribute("errorMessage") != null) { %>
                <p class="auth-error-text">Errore d'Autenticazione</p>
            <% } %>
            
            <form action="LoginServlet" method="post">
                
                <div class="input-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required placeholder="Inserisci la tua email">
                </div>
                
                <div class="input-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required placeholder="••••••••">
                	<div class="forgot-password-link">
        				<a href="recupero.jsp">Hai dimenticato la password?</a>
    				</div>
                </div>

                <div class="button-group">
                    <button type="submit" class="btn-register">Accedi</button>
                </div>
            </form>
        </div>
    </div>

</body>
</html>