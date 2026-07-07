<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Collection, model.OrdineBean, java.text.SimpleDateFormat, java.util.Locale" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Registro Ordini - Tech Life</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body class="admin-dashboard-layout">

    <nav class="navbar-top">
        <div class="nav-left">
            <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=admin" class="nav-link-home">Dashboard</a>
            <a href="${pageContext.request.contextPath}/AdminServlet?azione=listaClienti" class="nav-link-home">Gestione Clienti</a>
            <a href="${pageContext.request.contextPath}/AdminServlet?azione=listaOrdini" class="nav-link-home">Visualizza Ordini</a>
        </div>
        <div class="nav-right">
            <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn-logout admin-logout-fix">Logout</a>
        </div>
    </nav>

    <div class="cart-page-container">
        <h1>Registro Ordini Complessivi</h1>

        <div class="about-card">
            <form action="${pageContext.request.contextPath}/AdminServlet" method="GET" class="profile-form address-form-container">
                <input type="hidden" name="azione" value="listaOrdini">
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="dataInizio">Dalla data:</label>
                        <input type="date" id="dataInizio" name="dataInizio" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="dataFine">Alla data:</label>
                        <input type="date" id="dataFine" name="dataFine" required>
                    </div>
                </div>

                <button type="submit" class="btn-register">Applica Filtro Date</button>
            </form>
        </div>

        <div class="about-card">
            <table class="cart-table">
                <thead>
                    <tr>
                        <th>ID Ordine</th>
                        <th>ID Cliente</th>
                        <th>Tipo Account</th>
                        <th>Data e Ora</th>
                        <th>Totale Corrisposto</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        Collection<OrdineBean> ordini = (Collection<OrdineBean>) request.getAttribute("listaOrdini");
                        if (ordini != null && !ordini.isEmpty()) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            for (OrdineBean o : ordini) {
                    %>
                                <tr>
                                    <td><strong>#<%= o.getId() %></strong></td>
                                    <td>#<%= o.getIdCliente() %></td>
                                    <td>
                                        <span class="summary-qty-label admin-uppercase-label"><%= o.getTipoCliente() %></span>
                                    </td>
                                    <td><%= sdf.format(o.getDataOrdine()) %></td>
                                    <td>
                                        <strong class="final-price">€ <%= String.format(Locale.US, "%,.2f", o.getTotaleOrdine()) %></strong>
                                    </td>
                                </tr>
                    <%
                            }
                        } else {
                    %>
                            <tr>
                                <td colspan="5" class="admin-no-orders">
                                    Nessun ordine trovato per i criteri o il periodo selezionato.
                                </td>
                            </tr>
                    <%  } %>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>