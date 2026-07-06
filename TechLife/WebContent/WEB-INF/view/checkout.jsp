<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello, model.ProdottoBean, model.UtenteBean, java.util.Map, java.util.Locale" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Checkout - Tech Life</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body>

    <nav class="navbar-top">
        <div class="nav-left">
            <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=carrello" class="nav-link-home">&larr; Torna al Carrello</a>
        </div>
    </nav>

    <div class="checkout-page-container">
        <h1>Completamento Ordine</h1>

        <%
            Carrello carrello = (session != null) ? (Carrello) session.getAttribute("carrello") : null;
            UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utente") : null;

            if (carrello == null || carrello.getElementi().isEmpty() || utente == null) {
        %>
            <div class="empty-cart-block">
                <p class="empty-message">Impossibile procedere con il checkout.</p>
                <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=catalogologin" class="btn-register text-center-link">Torna al Catalogo</a>
            </div>
        <%
            } else {
        %>
            <div class="checkout-grid">
                
                <!-- SEZIONE SINISTRA: FORM DI ACQUISTO -->
                <div class="checkout-form-section">
                    <form action="${pageContext.request.contextPath}/OrdineServlet" method="POST">
                        
                        <h3>1. Indirizzo di Spedizione</h3>
                        <div class="indirizzo-item">
                            <%
                            dao.SpedizioneDAO spedModel = new dao.SpedizioneDAO();
                                                                                        String tipoUtente = (String) session.getAttribute("tipo");
                                                                                        java.util.Collection<model.SpedizioneBean> indirizziSalvati = null;
                                                                                        
                                                                                        if ("azienda".equals(tipoUtente)) {
                                                                                            indirizziSalvati = spedModel.getIndirizziAzienda(utente.getId());
                                                                                        } else {
                                                                                            indirizziSalvati = spedModel.getIndirizziPrivato(utente.getId());
                                                                                        }
                            %>

                            <% if (indirizziSalvati != null && !indirizziSalvati.isEmpty()) { %>
                                <span class="checkout-label-info">
                                    Seleziona l'indirizzo per la consegna di questo ordine:
                                </span>
                                <select name="id_indirizzo_selezionato" class="checkout-input-select">
                                    <option value="residenza_default">Residenza Principale: <%= utente.getIndirizzo() %>, <%= utente.getCap() %> <%= utente.getComune() %></option>
                                    <% 
                                        int count = 1;
                                        for (model.SpedizioneBean sped : indirizziSalvati) { 
                                            String visualizza = sped.getIndirizzo() + ", " + sped.getCap() + " " + sped.getComune() + " (" + sped.getProvincia().toUpperCase() + ")";
                                    %>
                                            <option value="<%= sped.getId() %>">Indirizzo Aggiuntivo #<%= count++ %>: <%= visualizza %></option>
                                    <% } %>
                                </select>
                            <% } else { %>
                                <input type="hidden" name="id_indirizzo_selezionato" value="residenza_default">
                                <span class="checkout-text-fallback">
                                    ℹ️ Non hai configurato indirizzi aggiuntivi nel tuo profilo. L'ordine viaggerà verso la tua **residenza registrata**:
                                    <br><strong class="final-price"><%= utente.getIndirizzo() %>, <%= utente.getCap() %> <%= utente.getComune() %></strong>
                                </span>
                            <% } %>
                            
                            <p class="checkout-link-wrapper">
                                Hai bisogno di spedire altrove? 
                                <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=profilo" class="link-profilo">Aggiungi un nuovo indirizzo dalla tua area personale</a>
                            </p>
                        </div>

                        <h3>2. Metodo di Pagamento</h3>
                        <div class="metodo-pagamento-block">
                            <div class="payment-radio-group">
                                <input type="radio" name="metodoPagamento" value="carta" checked class="checkout-radio">
                                <label for="carta">Carta di Credito / Debito / Postepay</label>
                            </div>
                            <div class="card-inputs-inline">
                                <input type="text" disabled class="checkout-input-disabled" value="•••• •••• •••• 4321">
                                <input type="text" disabled class="checkout-input-disabled short-input" value="12/29">
                            </div>
                        </div>

                        <button type="submit" class="btn-conferma-ordine">
                            Conferma e Acquista
                        </button>
                    </form>
                </div>

                <!-- SEZIONE DESTRA: RIEPILOGO COSTI -->
                <div class="checkout-summary-section">
                    <h3>Riepilogo Ordine</h3>
                    
                    <div class="summary-products-list">
                        <% for (Map.Entry<ProdottoBean, Integer> entry : carrello.getElementi().entrySet()) { %>
                            <div class="summary-row product-item-row">
                                <span><%= entry.getKey().getNome() %> <span class="summary-qty-label">x<%= entry.getValue() %></span></span>
                                <span class="summary-item-price">€ <%= String.format(Locale.US, "%,.2f", entry.getKey().getPrezzo() * entry.getValue()) %></span>
                            </div>
                        <% } %>
                    </div>

                    <div class="summary-row">
                        <span class="summary-muted-label">Dispositivi Totali:</span>
                        <strong class="summary-bold-value"><%= carrello.getQuantitaTotale() %></strong>
                    </div>
                    <div class="summary-row">
                        <span class="summary-muted-label">Spedizione:</span>
                        <strong class="final-price summary-bold-value">Gratis</strong>
                    </div>
                    
                    <div class="summary-row total-row">
                        <span>Totale:</span>
                        <span class="final-price">€ <%= String.format(Locale.US, "%,.2f", carrello.getPrezzoTotale()) %></span>
                    </div>
                </div>

            </div>
        <%
            }
        %>
    </div>
</body>
</html>