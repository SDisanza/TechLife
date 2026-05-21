<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
	<head>
		<meta charset="UTF-8">
		<title>Registrazione - Tech Life</title>
		<link type="text/css" rel="stylesheet" href="style.css">
	</head>
	<body>
		<div class="nav-back">
			<a href="index.jsp">&larr; Torna alla Home</a>
		</div>

		<h1>Crea il tuo Account</h1>
		<p>Registrati su Tech Life per acquistare i dispositivi salvavita adatti alle tue esigenze.</p>

		<div id="register-container">
			<div class="registration-toggle">
				<button type="button" onclick="cambiaScheda('privato')">Registrazione Privato</button>
				<button type="button" onclick="cambiaScheda('azienda')">Registrazione Azienda / P.IVA</button>
			</div>
			
			<% if (request.getAttribute("errorMessage") != null) { %>
	    		<% if (request.getAttribute("errorMessage") != null) { %>
	    			<p class="auth-error-text registration-error"><%= request.getAttribute("errorMessage") %></p>
				<% } %>
			<% } %>
			
			<form action="RegistrazioneServlet" method="POST" class="register-card">
				<input type="hidden" id="tipo_utente_nascosto" name="tipo_utente" value="privato">
				
				<div id="sezione-privato">
					<div class="form-group">
						<label for="nome">Nome</label>
						<input type="text" id="nome" name="nome" placeholder="Inserisci il tuo nome">
					</div>
					<div class="form-group">
						<label for="cognome">Cognome</label>
						<input type="text" id="cognome" name="cognome" placeholder="Inserisci il tuo cognome">
					</div>
					<div class="form-group">
						<label for="codice_fiscale">Codice Fiscale</label>
						<input type="text" id="cf" name="codice_fiscale" placeholder="Inserisci il tuo Codice Fiscale">
					</div>
					<div class="form-group">
						<label for="luogo_nascita">Luogo di Nascita</label>
						<input type="text" id="luogo_nascita" name="luogo_nascita" placeholder="Comune di nascita">
					</div>
					<div class="form-group">
						<label for="data_nascita">Data di Nascita</label>
						<input type="date" id="data_nascita" name="data_nascita">
					</div>
					<div class="form-group">
						<label for="comune_res">Comune di Residenza</label>
						<input type="text" id="comune_res" name="comune_res" placeholder="Comune di residenza">
					</div>
					<div class="form-group">
						<label for="ind_res">Indirizzo di Residenza</label>
						<input type="text" id="ind_res" name="ind_res" placeholder="Via, Piazza, Civico">
					</div>
					<div class="form-group">
						<label for="cap_res">CAP Residenza</label>
						<input type="text" id="cap_res" name="cap_res" placeholder="Es: 84084" maxlength="5">
					</div>
					<div class="form-group-checkbox">
						<input type="checkbox" id="domicilio_diverso" name="domicilio_diverso" onchange="gestisciDomicilio()">
						<label for="domicilio_diverso">Il mio domicilio è diverso dalla residenza</label>
					</div>
					<div id="campi-domicilio" style="display: none;">
						<div class="form-group">
							<label for="comune_dom">Comune di Domicilio (Opzionale)</label>
							<input type="text" id="comune_dom" name="comune_dom" placeholder="Se diverso da residenza">
						</div>
						<div class="form-group">
							<label for="ind_dom">Indirizzo di Domicilio (Opzionale)</label>
							<input type="text" id="ind_dom" name="ind_dom" placeholder="Se diverso da residenza">
						</div>
						<div class="form-group">
							<label for="cap_dom">CAP Domicilio (Opzionale)</label>
							<input type="text" id="cap_dom" name="cap_dom" placeholder="Se diverso" maxlength="5">
						</div>
					</div>
				</div>
				
				<div id="sezione-azienda" style="display: none;">
					<div class="form-group">
						<label for="nome_azienda">Nome Azienda / Ragione Sociale</label>
						<input type="text" id="nome_azienda" name="nome_azienda" placeholder="Ragione sociale completa">
					</div>
					<div class="form-group">
						<label for="p_iva">Partita IVA</label>
						<input type="text" id="p_iva" name="p_iva" placeholder="Inserisci la P.IVA a 11 cifre">
					</div>
					<div class="form-group">
						<label for="comune_leg">Comune Sede Legale</label>
						<input type="text" id="comune_leg" name="comune_leg" placeholder="Comune sede legale">
					</div>
					<div class="form-group">
						<label for="ind_leg">Indirizzo Sede Legale</label>
						<input type="text" id="ind_leg" name="ind_leg" placeholder="Via, Piazza, Civico legale">
					</div>
					<div class="form-group">
						<label for="cap_leg">CAP Sede Legale</label>
						<input type="text" id="cap_leg" name="cap_leg" placeholder="CAP sede legale" maxlength="5">
					</div>
					<div class="form-group">
						<label for="pec">Indirizzo PEC</label>
						<input type="email" id="pec" name="pec" placeholder="esempio@legalmail.it">
					</div>
				</div>
				
				<div class="form-group">
					<label for="email">Indirizzo Email</label>
					<input type="email" id="email" name="email" placeholder="esempio@email.com" required>
				</div>
		
				<div class="form-group">
					<div class="password-label-row">
						<label for="password">Password</label>
						<button type="button" id="btn-generate" onclick="generaPasswordSicura()">Suggerisci password</button>
					</div>
					<div class="password-input-wrapper">
						<input type="password" id="password" name="password" placeholder="Crea la tua password" autocomplete="new-password" required>
						<button type="button" id="btn-toggle-password" onclick="togglePasswordVisibilita()">👁️</button>
					</div>
				</div>

				<button type="submit" class="btn-register">Registrati Ora</button>
				<div class="form-footer">
					Hai già un account? <a href="login.jsp">Accedi qui</a>
				</div>
			</form>
		</div>
		<script src = "script/usage.js"></script>
	</body>
</html>
    