<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.SpedizioneModel, model.SpedizioneBean, model.UtenteBean, java.util.Collection" %>
<%
    UtenteBean utente = (UtenteBean) session.getAttribute("utente");
    if (utente == null) {
    	response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=login");
        return;
    }
    
    String tipoUtente = (String) session.getAttribute("tipo");
    if (tipoUtente == null) {
        tipoUtente = ("Azienda / P.IVA".equals(utente.getCognome())) ? "azienda" : "privato";
    }
    
    boolean isAzienda = "azienda".equals(tipoUtente);
    String status = request.getParameter("status");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Il Mio Profilo - Tech Life</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>

    <div class="nav-back">
        <a href="${pageContext.request.contextPath}/NavigazioneServlet?page=homelogin">&larr; Torna alla Home</a>
    </div>

    <h1>Il Tuo Profilo</h1>

    <div id="profile-container" class="about-card">
        <div class="profile-header">
            <h2>Riepilogo Dati Account</h2>
            <p>Tipologia Account: <strong><%= isAzienda ? "Azienda / Professionista" : "Cliente Privato" %></strong></p>
        </div>

        <div class="profile-details">
            <% if (!isAzienda) { %>
                <div class="info-group">
                    <span class="info-label">Nome:</span>
                    <span class="info-value"><%= utente.getNome() %></span>
                </div>
                <div class="info-group">
                    <span class="info-label">Cognome:</span>
                    <span class="info-value"><%= utente.getCognome() %></span>
                </div>
                <div class="info-group">
                    <span class="info-label">Codice Fiscale:</span>
                    <span class="info-value"><%= utente.getcodiceFiscale() %></span>
                </div>
                <% if (utente.getDataNascita() != null) { %>
                    <div class="info-group">
                        <span class="info-label">Data di Nascita:</span>
                        <span class="info-value"><%= utente.getDataNascita() %></span>
                    </div>
                <% } %>
            <% } else { %>
                <div class="info-group">
                    <span class="info-label">Ragione Sociale:</span>
                    <span class="info-value"><%= utente.getNome() %></span>
                </div>
                <div class="info-group">
                    <span class="info-label">Partita IVA:</span>
                    <span class="info-value"><%= utente.getcodiceFiscale() %></span>
                </div>
            <% } %>

            <div class="info-group">
                <span class="info-label">Indirizzo Email:</span>
                <span class="info-value"><%= utente.getEmail() %></span>
            </div>
        </div>
    </div>
    
    <%
        SpedizioneModel spedizioneModel = new SpedizioneModel();
        Collection<SpedizioneBean> listaIndirizzi = null;
        
        if (utente != null && tipoUtente != null) {
            if ("privato".equals(tipoUtente)) {
                listaIndirizzi = spedizioneModel.getIndirizziPrivato(utente.getId());
            } else if ("azienda".equals(tipoUtente)) {
                listaIndirizzi = spedizioneModel.getIndirizziAzienda(utente.getId());
            }
        }
    %>

    <div id="profile-container" class="about-card" style="margin-top: 30px;">
        <div class="profile-header">
            <h2>I Tuoi Indirizzi di Spedizione</h2>
            <p>Elenco dei luoghi di consegna associati al tuo account</p>
        </div>

        <% if ("duplicato".equals(status)) { %>
            <div class="alert-warning-box">
                ⚠️ Questo indirizzo è già presente nel tuo elenco.
            </div>
        <% } %>

        <div class="profile-details">
            <% 
                if (listaIndirizzi != null && !listaIndirizzi.isEmpty()) {
                    for (SpedizioneBean indirizzo : listaIndirizzi) {
            %>
                        <div class="info-group address-align-box">
                            <div class="info-value text-left">
                                <strong><%= indirizzo.getIndirizzo() %></strong>, 
                                <%= indirizzo.getComune() %> 
                                <% if (indirizzo.getProvincia() != null && !indirizzo.getProvincia().isEmpty()) { %>
                                    (<%= indirizzo.getProvincia().toUpperCase() %>)
                                <% } %>
                                 - <%= indirizzo.getCap() %>
                                <% if (indirizzo.getNote() != null && !indirizzo.getNote().isEmpty()) { %>
                                    <br><small class="address-sub-note">Note: <%= indirizzo.getNote() %></small>
                                <% } %>
                            </div>
                            
                            <form action="${pageContext.request.contextPath}/GestioneSpedizioneServlet" method="post" style="margin:0;">
                                <input type="hidden" name="azione" value="rimuovi">
                                <input type="hidden" name="id_spedizione" value="<%= indirizzo.getId() %>">
                                <button type="submit" class="btn-logout btn-delete-inline">Elimina</button>
                            </form>
                        </div>
            <%
                    }
                } else {
            %>
                <p class="no-addresses-text">Non hai ancora salvato nessun indirizzo di spedizione.</p>
            <%
                }
            %>
        </div>

        <div class="profile-header address-form-header">
            <h3>Aggiungi un nuovo indirizzo</h3>
        </div>
        
        <form action="${pageContext.request.contextPath}/GestioneSpedizioneServlet" method="post" class="profile-form address-form-container">
            <input type="hidden" name="azione" value="aggiungi">
            
            <% if ("azienda".equals(tipoUtente)) { %>
                <input type="hidden" name="pec_azienda" value="<%= (utente.getPec() != null) ? utente.getPec() : "" %>">
            <% } %>

            <div class="form-group text-left">
                <label for="indirizzo_spedizione">Indirizzo (Via/Piazza e Civico)</label>
                <input type="text" id="indirizzo_spedizione" name="indirizzo_spedizione" placeholder="Es: Via Roma 15" required>
            </div>

            <div class="form-row">
                <div class="form-group text-left" style="flex: 2;">
                    <label for="comune_spedizione">Comune</label>
                    <input type="text" id="comune_spedizione" name="comune_spedizione" placeholder="Es: Milano" required>
                </div>
                
                <div class="form-group text-left" style="flex: 1;">
                    <label for="provincia_spedizione">Provincia</label>
                    <input type="text" id="provincia_spedizione" name="provincia_spedizione" placeholder="Es: MI" maxlength="2" required>
                </div>

                <div class="form-group text-left" style="flex: 1;">
                    <label for="cap_spedizione">CAP</label>
                    <input type="text" id="cap_spedizione" name="cap_spedizione" placeholder="Es: 20100" maxlength="5" pattern="[0-9]{5}" required>
                </div>
            </div>

            <div class="form-group text-left">
                <label for="note">Note per il corriere (Opzionale)</label>
                <input type="text" id="note" name="note" placeholder="Es: Citofonare Rossi, interno 4...">
            </div>

            <button type="submit" class="btn-register btn-submit-address">Salva Indirizzo</button>
        </form>
    </div>

    <div class="logout-container" style="margin-top: 30px; margin-bottom: 50px;">
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn-logout" style="padding: 10px 25px;">Disconnetti</a>
    </div>

</body>
</html>