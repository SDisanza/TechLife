<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello, model.ProdottoBean, model.UtenteBean, java.util.Map, java.util.Locale" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Fattura Elettronica - Tech Life</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body>

    <div class="nav-back">
        <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=homelogin">&larr; Torna alla Home</a>
    </div>

    <%
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        // Leggiamo i dati congelati che abbiamo passato dalla Servlet
        Map<ProdottoBean, Integer> vociFattura = (Map<ProdottoBean, Integer>) request.getAttribute("vociFattura");
        Double totaleFattura = (Double) request.getAttribute("totaleFattura");
        String indirizzoSpedizione = (String) request.getAttribute("indirizzoSpedizione");
        String numeroFattura = (String) request.getAttribute("numeroFattura");
        String tipoUtente = (String) session.getAttribute("tipo");

        if (utente != null && vociFattura != null) {
    %>
    <div class="cart-page-container">

        <div class="about-card">
            <h2 class="final-price">🎉 Grazie! Il tuo ordine è andato a buon fine.</h2>
            <p class="cart-prod-cat">L'acquisto è stato registrato nei nostri sistemi. Trovi il riepilogo e i dettagli fiscali qui sotto.</p>
        </div>

        <div class="about-card">
            
            <div class="profile-header">
                <h2>FATTURA / RICEVUTA</h2>
                <p><strong>Numero:</strong> <%= numeroFattura %></p>
                <p><strong>Data:</strong> <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()) %></p>
            </div>

            <div class="summary-row">
                <div class="admin-info-text text-left">
                    <span class="info-label">TechLife S.r.l.</span>
                    <span class="cart-prod-cat text-left">
                        Via dell'Innovazione, 42<br>
                        84091 Battipaglia (SA)<br>
                        P.IVA: 01234567890<br>
                        PEC: techlife@legalmail.it
                    </span>
                </div>
            </div>

            <br><br>

            <div class="summary-row">
                <div class="admin-info-text text-left">
                    <span class="info-label">Intestatario Fattura:</span>
                    <span class="cart-prod-cat text-left">
                        <strong><%= utente.getNome() %> <%= "azienda".equals(tipoUtente) ? "" : utente.getCognome() %></strong><br>
                        Identificativo Fiscale: <%= utente.getcodiceFiscale() %><br>
                        Email: <%= utente.getEmail() %>
                        <% if("azienda".equals(tipoUtente) && utente.getPec() != null) { %>
                            <br>PEC: <%= utente.getPec() %>
                        <% } %>
                    </span>
                </div>
                
                <div class="admin-info-text text-left">
                    <span class="info-label">Destinazione Spedizione:</span>
                    <span class="cart-prod-cat text-left">
                        <%= indirizzoSpedizione %>
                    </span>
                </div>
            </div>

            <br>

            <table class="cart-table">
                <thead>
                    <tr>
                        <th>Dispositivo</th>
                        <th>Categoria</th>
                        <th>Quantità</th>
                        <th>Prezzo Singolo</th>
                        <th>Totale</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        for (Map.Entry<ProdottoBean, Integer> entry : vociFattura.entrySet()) { 
                            ProdottoBean prodotto = entry.getKey();
                            int qta = entry.getValue();
                    %>
                        <tr>
                            <td><strong><%= prodotto.getNome() %></strong></td>
                            <td class="cart-prod-cat text-left"><%= prodotto.getCategoria() %></td>
                            <td><%= qta %></td>
                            <td>€ <%= String.format(Locale.US, "%,.2f", prodotto.getPrezzo()) %></td>
                            <td class="final-price">€ <%= String.format(Locale.US, "%,.2f", prodotto.getPrezzo() * qta) %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>

            <div class="summary-row total-row">
                <span>Spedizione: <strong class="final-price">GRATUITA</strong></span>
                <span>Totale Complessivo: <strong class="final-price">€ <%= String.format(Locale.US, "%,.2f", totaleFattura) %></strong></span>
            </div>

            <div class="logout-container">
                <p class="cart-prod-cat">Grazie per aver scelto TechLife. Dispositivo medico salvavita conforme alle direttive CE.</p>
                <button onclick="window.print();" class="btn-register">Stampa</button>
            </div>
            
        </div>
    </div>
    
    <% } else { %>
    <div class="error-page-container">
        <div class="error-card">
            <h2 class="error-code-title">⚠️</h2>
            <div class="error-subtitle">
                <h2>Dati non disponibili</h2>
            </div>
            <p class="error-description">La fattura richiesta non è più disponibile in questa sessione o c'è stato un errore nel caricamento.</p>
            <div class="error-actions">
                <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=homelogin" class="btn-register text-center-link">Torna alla Home</a>
            </div>
        </div>
    </div>
    <% } %>

</body>
</html>