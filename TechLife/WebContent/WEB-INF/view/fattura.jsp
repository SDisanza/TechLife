<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello, model.ProdottoBean, model.UtenteBean, java.util.Map, java.util.Locale" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Fattura Elettronica - Tech Life</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<nav class="navbar-top">
        <div class="nav-left">
            <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=homelogin" class="nav-link-home">&larr; Torna alla Home</a>
        </div>
    </nav>

    <%-- MESSAGGIO DI CONFERMA ORDINE--%>
    <div class="invoice-box" style="border-color: #4cd964; margin-bottom: 0; padding-bottom: 15px; text-align: center;">
        <h2 style="color: #4cd964; margin: 0;">🎉 Grazie! Il tuo ordine è andato a buon fine.</h2>
        <p style="color: #ccc; margin: 5px 0 0 0; font-size: 0.95em;">L'acquisto è stato registrato nei nostri sistemi. Trovi il riepilogo e i dettagli fiscali qui sotto.</p>
    </div>

    <%
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        Carrello carrello = (Carrello) request.getAttribute("fatturaCarrello");
        String indirizzoSpedizione = (String) request.getAttribute("indirizzoSpedizione");
        String numeroFattura = (String) request.getAttribute("numeroFattura");
        String tipoUtente = (String) session.getAttribute("tipo");

        if (utente != null && carrello != null) {
    %>
        <div class="invoice-box">
            <div class="invoice-header">
                <div>
                    <h2>TechLife S.r.l.</h2>
                    <p class="cart-prod-cat">
                        Via dell'Innovazione, 42<br>
                        84044 Battipaglia (SA)<br>
                        P.IVA: 01234567890<br>
                        PEC: techlife@legalmail.it
                    </p>
                </div>
                <div style="text-align: right;">
                    <h1 class="final-price">FATTURA/RICEVUTA</h1>
                    <p><strong>Numero:</strong> <%= numeroFattura %></p>
                    <p><strong>Data:</strong> <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()) %></p>
                </div>
            </div>

            <div class="invoice-details">
                <div>
                    <h4 class="final-price">Intestatario Fattura:</h4>
                    <strong><%= utente.getNome() %> <%= "azienda".equals(tipoUtente) ? "" : utente.getCognome() %></strong><br>
                    Identificativo Fiscale: <%= utente.getcodiceFiscale() %><br>
                    Email: <%= utente.getEmail() %>
                    <% if("azienda".equals(tipoUtente) && utente.getPec() != null) { %>
                        <br>PEC: <%= utente.getPec() %>
                    <% } %>
                </div>
                <div>
                    <h4 class="final-price">Destinazione Spedizione:</h4>
                    <p><%= indirizzoSpedizione %></p>
                </div>
            </div>

            <table class="invoice-table">
                <thead>
                    <tr>
                        <th>Dispositivo</th>
                        <th>Categoria</th>
                        <th style="text-align: center;">Quantità</th>
                        <th style="text-align: right;">Prezzo Singolo</th>
                        <th style="text-align: right;">Totale</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        for (Map.Entry<ProdottoBean, Integer> entry : carrello.getElementi().entrySet()) { 
                            ProdottoBean prodotto = entry.getKey();
                            int qta = entry.getValue();
                    %>
                        <tr>
                            <td><%= prodotto.getNome() %></td>
                            <td><%= prodotto.getCategoria() %></td>
                            <td style="text-align: center;"><%= qta %></td>
                            <td style="text-align: right;">€ <%= String.format(Locale.US, "%,.2f", prodotto.getPrezzo()) %></td>
                            <td style="text-align: right;"><strong>€ <%= String.format(Locale.US, "%,.2f", prodotto.getPrezzo() * qta) %></strong></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>

            <div class="invoice-total-block">
                <p>Spedizione: <strong class="final-price">GRATUITA</strong></p>
                <h2>Totale Complessivo: <span class="final-price">€ <%= String.format(Locale.US, "%,.2f", carrello.getPrezzoTotale()) %></span></h2>
            </div>

            <div style="text-align: center; margin-top: 40px; border-top: 1px dashed #2e2e4e; padding-top: 20px;">
                <p class="cart-prod-cat">Grazie per aver scelto TechLife. Dispositivo medico salvavita conforme alle direttive CE.</p>
                <button onclick="window.print();" class="no-print-btn">Stampa o Salva in PDF</button>
            </div>
        </div>
    <% 
        } else { 
    %>
        <div class="empty-cart-block">
            <h3>Impossibile recuperare i dati della fattura.</h3>
            <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=homelogin" class="no-print-btn">Torna alla Home</a>
        </div>
    <% 
        } 
    %>

</body>
</html>