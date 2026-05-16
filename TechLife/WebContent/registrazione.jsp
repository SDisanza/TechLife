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
		<script src = "usage.js"></script>
		<div class="nav-back">
			<a href="index.html">&larr; Torna alla Home</a>
		</div>

		<h1>Crea il tuo Account</h1>
		<p>Registrati su Tech Life per acquistare i dispositivi salvavita adatti alle tue esigenze.</p>

		<div id="register-container">
			<form action="" method="POST" class="register-card">
				
				<div class="form-group">
					<label for="nome">Nome Completo / Ragione Sociale</label>
					<input type="text" id="nome" name="nome" placeholder="Inserisci il tuo nome o azienda" required>
				</div>

				<div class="form-group">
					<label for="email">Indirizzo Email</label>
					<input type="email" id="email" name="email" placeholder="esempio@email.com" required>
				</div>

				<div class="form-group">
					<label for="tipo_utente">Tipo di Profilo</label>
					<select id="tipo_utente" name="tipo_utente" required>
						<option value="privato">Privato / Cittadino</option>
						<option value="azienda">Azienda / P.IVA</option>
						<option value="medico">Struttura Sanitaria / Medico</option>
					</select>
				</div>

				<div class="form-group">
					<div class="password-label-row">
						<label for="password">Password</label>
						<button type="button" id="btn-generate" onclick="generaPasswordSicura()">Suggerisci password</button>
					</div>
					<div class="password-input-wrapper">
						<input type="password" id="password" name="password" placeholder="Crea o genera una password" autocomplete="new-password" required>
						<button type="button" id="btn-toggle-password" onclick="togglePasswordVisibilita()">👁️</button>
					</div>
				</div>

				<button type="submit" class="btn-register">Registrati Ora</button>
				<div class="form-footer">
					Hai già un account? <a href="login.jsp">Accedi qui</a>
				</div>
			</form>
		</div>
	</body>
</html>
    