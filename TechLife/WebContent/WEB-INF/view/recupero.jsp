<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Recupero Password - Tech Life</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>

    <div class="nav-back">
        <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=login">&larr; Torna al Login</a>
    </div>
    
    <div class="login-container-page">
        <div class="login-card">
            <h2>Recupera Password</h2>
            
            
            <% if (request.getAttribute("errorMessage") != null) { %>
                <p class="auth-error-text"><%= request.getAttribute("errorMessage") %></p>
            <% } %>
            
            
            <% if ("success".equals(request.getParameter("status"))) { %>
                <p class="auth-success-text">🔒 Password aggiornata con successo!</p>
                <p class="sub-message auth-sub-message">Ora puoi effettuare l'accesso con le tue nuove credenziali.</p>
                <div class="button-group">
                    <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=login" class="btn-register btn-block text-center-link">Vai al Login</a>
                </div>
            
            
            <% } else if (request.getAttribute("emailVerificata") != null) { %>
                <p class="sub-message auth-sub-message">Email verificata! Inserisci la tua nuova password.</p>
                
                <form action="${pageContext.request.contextPath}/RecuperoPasswordServlet" method="post">
                    <input type="hidden" name="azione" value="aggiorna">
                    <input type="hidden" name="email" value="<%= request.getAttribute("emailVerificata") %>">
                    
                    <div class="input-group">
                        <label for="nuovaPassword">Nuova Password</label>
                        <input type="password" id="nuovaPassword" name="nuovaPassword" required placeholder="Minimo 8 caratteri">
                    </div>
                    
                    <div class="input-group">
                        <label for="confermaPassword">Conferma Password</label>
                        <input type="password" id="confermaPassword" name="confermaPassword" required placeholder="Ripeti la password">
                    </div>

                    <div class="button-group">
                        <button type="submit" class="btn-register btn-block">Modifica Password</button>
                    </div>
                </form>
            
            <% } else { %>
                <p class="sub-message auth-sub-message">Inserisci l'indirizzo email associato al tuo account per avviare il ripristino.</p>
                
                <form action="${pageContext.request.contextPath}/RecuperoPasswordServlet" method="post">
                    <input type="hidden" name="azione" value="verificaEmail">
                    
                    <div class="input-group">
                        <label for="email">Email dell'account</label>
                        <input type="email" id="email" name="email" required placeholder="esempio@email.com">
                    </div>

                    <div class="button-group">
                        <button type="submit" class="btn-register btn-block">Verifica Account</button>
                    </div>
                </form>
            <% } %>
            
        </div>
    </div>

</body>
</html>