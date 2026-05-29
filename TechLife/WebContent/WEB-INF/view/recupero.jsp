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
            
            <% if ("success".equals(request.getAttribute("status"))) { %>
                <p class="auth-success-text">🔒 Password aggiornata con successo!</p>
                <p class="sub-message auth-sub-message">Ora puoi effettuare l'accesso con le tue nuove credenziali.</p>
                <div class="button-group">
                    <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=login" class="btn-register btn-block text-center-link">Vai al Login</a>
                </div>
            
            <% } else if (request.getAttribute("emailVerificata") != null) { %>
                <p class="sub-message auth-sub-message">Email verificata! Inserisci la tua nuova password.</p>
                
                <form action="${pageContext.request.contextPath}/RecuperoPasswordServlet" method="post">
                    <input type="hidden" name="azione" value="aggiornaPassword">
                    <input type="hidden" name="email" value="<%= request.getAttribute("emailVerificata") %>">
                    <input type="hidden" name="tipoUtente" value="<%= request.getAttribute("tipoUtente") %>">
                    
                    <div class="input-group">
                        <label for="password">Nuova Password</label>
                        <input type="password" id="password" name="nuovaPassword" required placeholder="Inserisci una password sicura" oninput="validaCoincidenzaPassword()">
                        
                        <div id="box-requisiti-password" style="margin-top: 8px; font-size: 0.85em; line-height: 1.5;">
                            <p style="margin: 0 0 5px 0; font-weight: bold; color: #555;">La password deve contenere:</p>
                            <div id="req-lunghezza" class="requisito-invalido" data-testo="Almeno 8 caratteri"> ❌ Almeno 8 caratteri</div>
                            <div id="req-maiuscola" class="requisito-invalido" data-testo="Una letterai maiuscola"> ❌ Una lettera maiuscola</div>
                            <div id="req-minuscola" class="requisito-invalido" data-testo="Una lettera minuscola"> ❌ Una lettera minuscola</div>
                            <div id="req-numero" class="requisito-invalido" data-testo="Un numero"> ❌ Un numero</div>
                            <div id="req-speciale" class="requisito-invalido" data-testo="Un carattere speciale (@$!%*?&!#_)"> ❌ Un carattere speciale (@$!%*?&!#_)</div>
                        </div>
                    </div>
                    
                    <div class="input-group">
                        <label for="confermaPassword">Conferma Password</label>
                        <input type="password" id="confermaPassword" name="confermaPassword" required oninput="validaCoincidenzaPassword()" placeholder="Conferma la tua password">
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
    <script src="${pageContext.request.contextPath}/script/validation.js"></script>
</body>
</html>