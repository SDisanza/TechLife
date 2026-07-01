<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello, model.ProdottoBean, java.util.Map, java.util.Locale" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Il tuo Carrello - Tech Life</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>

    <nav class="navbar-top">
        <div class="nav-left">
            <% if (session != null && session.getAttribute("utente") != null) { %>
                <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=catalogologin" class="nav-link-home">&larr; Torna al Catalogo</a>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=catalogo" class="nav-link-home">&larr; Torna al Catalogo</a>
            <% } %>
        </div>
    </nav>

    <div class="cart-page-container">
        <h1>Il tuo Carrello</h1>

        <%
            // Recuperiamo il carrello dalla sessione
            Carrello carrello = (session != null) ? (Carrello) session.getAttribute("carrello") : null;
            
            if (carrello == null || carrello.getElementi().isEmpty()) {
        %>
            <div class="empty-cart-block">
                <p class="empty-message">Il tuo carrello è attualmente vuoto.</p>
                <p class="sub-message">Non hai ancora aggiunto alcun dispositivo salvavita al tuo riepilogo.</p>
                <div class="button-group">
                    <% if (session != null && session.getAttribute("utente") != null) { %>
                        <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=catalogologin" class="btn-register text-center-link">Sfoglia il Catalogo</a>
                    <% } else { %>
                        <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=catalogo" class="btn-register text-center-link">Sfoglia il Catalogo</a>
                    <% } %>
                </div>
            </div>
        <%
            } else {
        %>
            <div class="cart-content-wrapper">
                
                <table class="cart-table">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Prezzo</th>
                            <th>Quantità</th>
                            <th>Totale</th>
                            <th>Azione</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            // Cicliamo tutti gli elementi della mappa (Prodotto -> Quantità)
                            for (Map.Entry<ProdottoBean, Integer> entry : carrello.getElementi().entrySet()) {
                                ProdottoBean prodotto = entry.getKey();
                                int quantita = entry.getValue();
                                double totaleRiga = prodotto.getPrezzo() * quantita;
                        %>
                            <tr>
                                <td class="cart-product-cell">
                                    <img src="${pageContext.request.contextPath}/<%= prodotto.getFoto() %>" alt="<%= prodotto.getNome() %>" class="cart-thumb">
                                    <div class="cart-prod-info">
                                        <span class="cart-prod-title"><%= prodotto.getNome() %></span>
                                        <span class="cart-prod-cat"><%= prodotto.getCategoria() %></span>
                                    </div>
                                </td>
                                <td>€ <%= String.format(Locale.US, "%,.2f", prodotto.getPrezzo()) %></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/CarrelloServlet" method="POST" class="cart-qty-form">
                                        <input type="hidden" name="azione" value="aggiornaQuantita">
                                        <input type="hidden" name="idProdotto" value="<%= prodotto.getId() %>">
                                        <input type="number" name="quantita" value="<%= quantita %>" min="1" max="99" onchange="this.form.submit()" class="cart-qty-input">
                                    </form>
                                </td>
                                <td><strong>€ <%= String.format(Locale.US, "%,.2f", totaleRiga) %></strong></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/CarrelloServlet" method="POST" style="display:inline;">
                                        <input type="hidden" name="azione" value="rimuovi">
                                        <input type="hidden" name="idProdotto" value="<%= prodotto.getId() %>">
                                        <button type="submit" class="btn-delete-cart">Elimina</button>
                                    </form>
                                </td>
                            </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>

                <div class="cart-summary-card">
                    <div class="summary-row">
                        <span>Oggetti totali:</span>
                        <strong><%= carrello.getQuantitaTotale() %></strong>
                    </div>
                    <div class="summary-row total-row">
                        <span>Totale Complessivo:</span>
                        <span class="final-price">€ <%= String.format(Locale.US, "%,.2f", carrello.getPrezzoTotale()) %></span>
                    </div>
                    
                    <div class="cart-actions-footer">
                        <form action="${pageContext.request.contextPath}/CarrelloServlet" method="POST" class="inline-form">
                            <input type="hidden" name="azione" value="svuota">
                            <button type="submit" class="btn-clear-cart">Svuota Carrello</button>
                        </form>
                        
                        <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=checkout" class="btn-register checkout-btn">Procedi al Checkout &rarr;</a>
                    </div>
                </div>

            </div>
        <%
            }
        %>
    </div>

</body>
</html>