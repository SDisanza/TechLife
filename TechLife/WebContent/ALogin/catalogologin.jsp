<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*" %>
<!DOCTYPE html>
<html lang="it">
	<head>
	    <meta charset="UTF-8">
	    <title>Catalogo</title>
	    <link type="text/css" rel="stylesheet" href="../style.css">
	    <%@ page import="model.DriverManagerConnectionPool" %> 
	</head>
	<body>
	
	<nav class="navbar-top">
	    <div class="nav-left">
	        <a href="homelogin.jsp" class="nav-link-home">&larr; Torna alla Home</a>
	    </div>
	    
	    <div class="nav-right">
	        <a href="carrello.jsp" class="nav-cart-block">
	            <svg xmlns="http://w3.org" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="navbar-cart-icon">
	                <circle cx="9" cy="21" r="1"></circle>
	                <circle cx="20" cy="21" r="1"></circle>
	                <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
	            </svg>
	            <span class="cart-label">Carrello</span>
	            <span class="cart-badge">0</span>
	        </a>
	    </div>
	</nav>

    <h1>Catalogo TechLife</h1>
    
    <div id="catalog-container">
        <%
	        Connection conn = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        
	        try {
	            conn = DriverManagerConnectionPool.getConnection();
	            String query = "SELECT ID, Nome, Prezzo, Foto, Descrizione FROM Prodotto";
	            stmt = conn.prepareStatement(query);
	            rs = stmt.executeQuery();
	            
	            if (!rs.isBeforeFirst()) {
		%>
		<div class="no-products">
		    <p>Nessun prodotto disponibile al momento. Stiamo aggiornando il catalogo.</p>
		</div>
		<%
		}
		while (rs.next()) {
		    int id = rs.getInt("ID");
		    String nome = rs.getString("Nome");
		    double prezzo = rs.getDouble("Prezzo");
		    String foto = rs.getString("Foto");
		    String descrizione = rs.getString("Descrizione");
		%>

		<div class="product-card">
		    <a href="dettaglioProdotto.jsp?id=<%= id %>" class="product-details-link">
		   <div class="product-image-wrapper">
		       <img src="../<%= foto %>" alt="<%= nome %>" class="product-image">
		   </div>
		   <h3 class="product-title"><%= nome %></h3>
		   <p class="product-description"><%= descrizione %></p>
		</a>
		<div class="product-footer">
		    <span class="product-price">€ <%= String.format(Locale.US, "%,.2f", prezzo) %></span>
		   <form action="../AggiungiAlCarrelloServlet" method="POST">
		       <input type="hidden" name="id_prodotto" value="<%= id %>">
		            <input type="hidden" name="quantita" value="1">
		            <button type="submit" class="btn-cart">Aggiungi</button>
		        </form>
		    </div>
		    
		</div>
		<%
		    }
		} catch (Exception e) {
		%>
		<div class="no-products">
		    <p style="color: #ff3b30;">Errore nel caricamento dei prodotti: <%= e.getMessage() %></p>
		</div>
		<%
		} finally {
		    if (rs != null) rs.close();
		    if (stmt != null) stmt.close();
		    if (conn != null) conn.close();
		}
		%>
		
		    </div>

	</body>
</html>
