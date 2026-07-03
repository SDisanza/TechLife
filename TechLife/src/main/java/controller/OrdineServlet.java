package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Carrello;
import model.DriverManagerConnectionPool;
import model.UtenteBean;

@WebServlet("/OrdineServlet")
public class OrdineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        String tipoUtente = (String) session.getAttribute("tipo"); // "privato" o "azienda"

        // Sicurezza protettiva
        if (utente == null || carrello == null || carrello.getElementi().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=carrello");
            return;
        }

        String indirizzoFinale = "";
        String idIndirizzoSelezionato = request.getParameter("id_indirizzo_selezionato");

        try {
            model.SpedizioneModel spedizioneModel = new model.SpedizioneModel();
            java.util.Collection<model.SpedizioneBean> listaIndirizzi;

            // Recuperiamo la lista degli indirizzi già salvati dal DB a seconda della tipologia account
            if ("azienda".equals(tipoUtente)) {
                listaIndirizzi = spedizioneModel.getIndirizziAzienda(utente.getId());
            } else {
                listaIndirizzi = spedizioneModel.getIndirizziPrivato(utente.getId());
            }

            // Se l'utente ha selezionato esplicitamente un indirizzo aggiuntivo dalla lista del database
            if (idIndirizzoSelezionato != null && !"residenza_default".equals(idIndirizzoSelezionato)) {
                int idSped = Integer.parseInt(idIndirizzoSelezionato);
                
                // Ricerchiamo l'indirizzo corrispondente all'ID della tendina nella lista estratta
                boolean trovato = false;
                if (listaIndirizzi != null) {
                    for (model.SpedizioneBean s : listaIndirizzi) {
                        if (s.getId() == idSped) {
                            String prov = (s.getProvincia() != null && !s.getProvincia().isEmpty()) ? " (" + s.getProvincia().toUpperCase() + ")" : "";
                            indirizzoFinale = s.getIndirizzo() + ", " + s.getCap() + " " + s.getComune() + prov;
                            trovato = true;
                            break;
                        }
                    }
                }
                
                // Meccanismo di sicurezza:ripieghiamo sulla residenza
                if (!trovato) {
                    indirizzoFinale = utente.getIndirizzo() + ", " + utente.getCap() + " " + utente.getComune() + " (Residenza Account)";
                }

            } 
            else {
                indirizzoFinale = utente.getIndirizzo() + ", " + utente.getCap() + " " + utente.getComune() + " (Residenza Account)";
            }

        } catch (Exception e) {
            e.printStackTrace();
            indirizzoFinale = utente.getIndirizzo() + ", " + utente.getCap() + " " + utente.getComune();
        }

        Connection connection = null;
        PreparedStatement ps = null;
        String sql = "INSERT INTO Ordine (ID_Cliente, Tipo_Cliente, Totale_Ordine) VALUES (?, ?, ?)";

        try {
            connection = DriverManagerConnectionPool.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, utente.getId());
            ps.setString(2, tipoUtente != null ? tipoUtente : "privato");
            ps.setDouble(3, carrello.getPrezzoTotale());

            ps.executeUpdate();
            connection.commit();
            System.out.println("DEBUG ORDINE: Inserimento completato con successo nel DB!");

            // Passiamo i dati alla pagina della fattura prima di svuotare il carrello
            request.setAttribute("indirizzoSpedizione", indirizzoFinale);
            request.setAttribute("fatturaCarrello", carrello);
            
            // Creiamo un numero di fattura fittizio univoco
            String numeroFattura = "FT-" + System.currentTimeMillis() % 100000;
            request.setAttribute("numeroFattura", numeroFattura);

            // Svuotiamo il carrello visto che l'acquisto è andato a buon fine
            carrello.svuota();
            model.UtenteBean utenteLoggato = (model.UtenteBean) request.getSession().getAttribute("utente");
            if (utenteLoggato != null) {
                try {
                    model.CarrelloModel carrelloModel = new model.CarrelloModel();
                    carrelloModel.salvaCarrello(utenteLoggato.getId(), carrello);
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }

            // Mandiamo l'utente sulla pagina di visualizzazione fattura
            request.getRequestDispatcher("/WEB-INF/view/fattura.jsp").forward(request, response);

        } catch (SQLException e) {
            if (connection != null) {
                try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=errore");
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) {
                try {
                    DriverManagerConnectionPool.releaseConnection(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
}