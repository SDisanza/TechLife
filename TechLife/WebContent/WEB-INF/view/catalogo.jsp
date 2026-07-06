<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page import = " model.Carrello " %>
<!DOCTYPE html>
<html lang="it">
	<head>
	    <meta charset="UTF-8">
	    <title>Catalogo</title>
	    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
	</head>
	<body>
	<%if (session != null && session.getAttribute("utente") != null) 
	{
        response.sendRedirect("ALogin/catalogologin.jsp");
        return;
    }%>
    
    <%
        int badgeCount = 0;
        Carrello cartOspite = (session != null) ? (Carrello) session.getAttribute("carrello") : null;
        if (cartOspite != null) {
            badgeCount = cartOspite.getQuantitaTotale();
        }
    %>
    
	<nav class="navbar-top">
	    <div class="nav-left">
	        <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=home" class="nav-link-home">&larr; Torna alla Home</a>
	    </div>
	    
	    <div class="nav-right">
			<a href="${pageContext.request.contextPath}/NavigazioneServlet?page=carrello" class="nav-cart-block">
	            <svg xmlns="http://w3.org" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="navbar-cart-icon">
	                <circle cx="9" cy="21" r="1"></circle>
	                <circle cx="20" cy="21" r="1"></circle>
	                <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
	            </svg>
	            <span class="cart-label">Carrello</span>
	            <span class="cart-badge"><%= badgeCount %></span>
	        </a>
	    </div>
	</nav>

    <h1>Catalogo TechLife</h1>
    
    <div id="catalog-container">
        <%
	        try {
	            dao.ProdottoDAO prodottoDAO = new dao.ProdottoDAO();
	            java.util.Collection<model.ProdottoBean> prodotti = prodottoDAO.doRetrieveAll();
	            
	            if (prodotti == null || prodotti.isEmpty()) {
		%>
				<div class="no-products">
				    <p>Nessun prodotto disponibile al momento. Stiamo aggiornando il catalogo.</p>
				</div>
		<%
				} else {
					for (model.ProdottoBean p : prodotti) {
					    int id = p.getId();
					    String nome = p.getNome();
					    double prezzo = p.getPrezzo();
					    String foto = p.getFoto();
					    String descrizione = p.getDescrizione();
		%>

					<div class="product-card">
					    <a href="dettaglioProdotto.jsp?id=<%= id %>" class="product-details-link">
						   <div class="product-image-wrapper">
						       <img src="${pageContext.request.contextPath}/<%= foto %>" alt="<%= nome %>" class="product-image">
						   </div>
						   <h3 class="product-title"><%= nome %></h3>
						   <p class="product-description"><%= descrizione %></p>
						</a>
						<div class="product-footer">
						    <span class="product-price">€ <%= String.format(Locale.US, "%,.2f", prezzo) %></span>
						   <form action="${pageContext.request.contextPath}/CarrelloServlet" method="POST" class="product-cart-form">
						       <input type="hidden" name="idProdotto" value="<%= id %>">
						       <input type="hidden" name="azione" value="aggiungi">
						       <button type="submit" class="btn-cart">Aggiungi</button>
						   </form>
					    </div>
					</div>
		<%
					} 
				} 
			} catch (Exception e) {
		%>
			<div class="no-products">
			    <p style="color: #ff3b30;">Errore nel caricamento dei prodotti: <%= e.getMessage() %></p>
			</div>
		<%
			}
		%>
	</div>

	</body>
</html>