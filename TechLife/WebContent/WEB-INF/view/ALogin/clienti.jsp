<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Collection, model.UtenteBean" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Clienti - Tech Life</title>
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
        <h1>Anagrafica Clienti Iscritti</h1>
        
        <table class="cart-table">
            <thead>
                <tr>
                    <th>ID Utente</th>
                    <th>Nome / Azienda</th>
                    <th>Cognome / Tipo</th>
                    <th>Email</th>
                    <th>Codice Fiscale / P.IVA</th>
                    <th>Azione</th>
                </tr>
            </thead>
            <tbody>
                <%
                    Collection<UtenteBean> clienti = (Collection<UtenteBean>) request.getAttribute("listaClienti");
                    if (clienti != null && !clienti.isEmpty()) {
                        for (UtenteBean c : clienti) {
                %>
                            <tr>
                                <td><strong>#<%= c.getId() %></strong></td>
                                <td><%= c.getNome() %></td>
                                <td>
                                    <span class="summary-qty-label admin-uppercase-label"><%= c.getCognome() %></span>
                                </td>
                                <td><%= c.getEmail() %></td>
                                <td><%= c.getcodiceFiscale() %></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/AdminServlet" method="POST" onsubmit="return confirm('Vuoi davvero eliminare questo cliente?');">
                                        <input type="hidden" name="azione" value="eliminaCliente">
                                        <input type="hidden" name="id_cliente" value="<%= c.getId() %>">
                                        <input type="hidden" name="tipo_cliente" value="<%= c.getCognome() %>">
                                        <button type="submit" class="btn-delete-cart">Elimina</button>
                                    </form>
                                </td>
                            </tr>
                <%
                        }
                    } else {
                %>
                        <tr>
                            <td colspan="6" class="admin-no-orders">
                                Nessun cliente registrato sulla piattaforma.
                            </td>
                        </tr>
                <%  } %>
            </tbody>
        </table>
    </div>

</body>
</html>