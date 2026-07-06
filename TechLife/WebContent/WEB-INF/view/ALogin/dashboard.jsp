<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Collection, model.ProdottoBean, model.OrdineBean, java.util.Locale" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Pannello Amministratore - Tech Life</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body class="admin-dashboard-layout">

    <nav class="navbar-top">
        <div class="nav-left">
            <span class="nav-link-home">Pannello Controllo Admin</span>
        </div>
        <div class="logout-container admin-logout-fix">
            <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn-logout">Logout</a>
        </div>
    </nav>

    <div class="checkout-page-container">
        <h1>Gestione Piattaforma</h1>

        <div class="checkout-grid">
            
            <div class="checkout-form-section">
                <h3>Aggiungi Nuovo Prodotto nel Catalogo</h3>
                <form action="${pageContext.request.contextPath}/AdminProdottoServlet" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="azione" value="aggiungi">
                    
                    <div class="form-group">
                        <label>Nome Prodotto</label>
                        <input type="text" name="nome" placeholder="Es: Defibrillatore Philips FRx" required>
                    </div>

                    <div class="form-group">
                        <label>Descrizione</label>
                        <input type="text" name="descrizione" placeholder="Inserisci una breve descrizione..." required>
                    </div>

                    <div class="card-inputs-inline">
                        <div class="form-group flex-item">
                            <label>Prezzo (€)</label>
                            <input type="number" step="0.01" name="prezzo" placeholder="Es: 199.99" required>
                        </div>
                        <div class="form-group flex-item">
                            <label>Categoria</label>
                            <select name="categoria" required>
                                <option value="Defibrillatori">Defibrillatori</option>
                                <option value="Accessori">Accessori</option>
                                <option value="Aspiratori">Aspiratori</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
				        <label>Carica Foto Prodotto</label>
				        <input type="file" name="immagine" accept="image/*" required>
				    </div>

                    <button type="submit" class="btn-conferma-ordine">Salva Prodotto nel Catalogo</button>
                </form>
            </div>

            <div class="checkout-summary-section">
                <h3>Elimina Prodotti</h3>
                <div class="summary-products-list admin-scroll-list">
                    <%
                        Collection<ProdottoBean> prodotti = (Collection<ProdottoBean>) request.getAttribute("listaProdotti");
                        if (prodotti != null && !prodotti.isEmpty()) {
                            for (ProdottoBean p : prodotti) {
                    %>
                                <div class="summary-row product-item-row admin-row-padding">
                                    <div class="admin-info-text">
                                        <strong><%= p.getNome() %></strong>
                                        <span class="admin-sub-id">ID: <%= p.getId() %> | € <%= p.getPrezzo() %></span>
                                    </div>
                                    <form action="${pageContext.request.contextPath}/AdminProdottoServlet" method="POST" class="product-cart-form">
                                        <input type="hidden" name="azione" value="rimuovi">
                                        <input type="hidden" name="id_prodotto" value="<%= p.getId() %>">
                                        <button type="submit" class="btn-delete-cart admin-btn-small">Elimina</button>
                                    </form>
                                </div>
                    <%
                            }
                        } else {
                    %>
                            <p class="admin-empty-msg">Nessun prodotto disponibile.</p>
                    <%  } %>
                </div>
            </div>
        </div>

        <div class="checkout-form-section admin-table-section">
            <h3>Storico Ordini Ricevuti dai Clienti</h3>
            
            <table class="cart-table admin-table-reset">
                <thead>
                    <tr>
                        <th>ID Ordine</th>
                        <th>ID Cliente</th>
                        <th>Tipo Account</th>
                        <th>Data e Ora</th>
                        <th>Totale Incassato</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        Collection<model.OrdineBean> ordini = (Collection<model.OrdineBean>) request.getAttribute("listaOrdini");
                        if (ordini != null && !ordini.isEmpty()) {
                            for (model.OrdineBean o : ordini) {
                    %>
                                <tr>
                                    <td><strong>#<%= o.getId() %></strong></td>
                                    <td><%= o.getIdCliente() %></td>
                                    <td><span class="summary-qty-label admin-uppercase-label"><%= o.getTipoCliente() %></span></td>
                                    <td><%= o.getDataOrdine() %></td>
                                    <td><strong class="final-price">€ <%= String.format(Locale.US, "%,.2f", o.getTotaleOrdine()) %></strong></td>
                                </tr>
                    <%
                            }
                        } else {
                    %>
                            <tr>
                                <td colspan="5" class="admin-no-orders">
                                    Nessun ordine effettuato sulla piattaforma fino a questo momento.
                                </td>
                            </tr>
                    <%  } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>