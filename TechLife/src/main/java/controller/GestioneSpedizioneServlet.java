package controller;

import java.io.IOException;
import java.sql.SQLException;

import dao.SpedizioneDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.UtenteBean;
import model.SpedizioneBean;

@WebServlet("/GestioneSpedizioneServlet")
public class GestioneSpedizioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        
        UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("utente");
        String tipoUtente = (String) session.getAttribute("tipo"); 

        if (utenteLoggato == null || tipoUtente == null) {
            response.sendRedirect(request.getContextPath() + "/errore.jsp");
            return;
        }

        String azione = request.getParameter("azione");
        SpedizioneDAO model = new SpedizioneDAO();

        try {
            System.out.println("DEBUG SPEDIZIONE: Ricevuta azione = '" + azione + "' | Tipo utente = '" + tipoUtente + "'");

            if ("aggiungi".equals(azione) || azione == null || azione.isEmpty()) {
                
                String indirizzoSpedizione = request.getParameter("indirizzo_spedizione");

                // CONTROLLO DUPLICATI: Verifichiamo se l'indirizzo esiste già nel DB per questo utente
                if (model.checkIndirizzoEsistente(utenteLoggato.getId(), indirizzoSpedizione, tipoUtente)) {
                    System.out.println("DEBUG SPEDIZIONE: Indirizzo già presente nel DB. Salto l'inserimento.");
                    // Reindirizza alla pagina utente passando un parametro di avviso per la grafica (opzionale)
                    request.setAttribute("status", "duplicato");
                    request.getRequestDispatcher("/WEB-INF/view/ALogin/utente.jsp").forward(request, response);
                    return;
                }

                if ("privato".equals(tipoUtente)) {
                    SpedizioneBean nuovaSpedizione = new SpedizioneBean();
                    nuovaSpedizione.setIdUtenteAzienda(utenteLoggato.getId()); 
                    nuovaSpedizione.setEmail(utenteLoggato.getEmail());
                    nuovaSpedizione.setIndirizzo(indirizzoSpedizione);
                    nuovaSpedizione.setCap(request.getParameter("cap_spedizione"));
                    nuovaSpedizione.setComune(request.getParameter("comune_spedizione"));
                    nuovaSpedizione.setProvincia(request.getParameter("provincia_spedizione"));
                    nuovaSpedizione.setNote(request.getParameter("note"));

                    System.out.println("DEBUG SPEDIZIONE: Chiamo model.aggiungiIndirizzoUtente...");
                    model.aggiungiIndirizzoUtente(nuovaSpedizione);
                    System.out.println("DEBUG SPEDIZIONE: Inserimento privato completato con successo!");

                } else if ("azienda".equals(tipoUtente)) {
                    SpedizioneBean nuovaSpedizione = new SpedizioneBean();
                    nuovaSpedizione.setIdUtenteAzienda(utenteLoggato.getId()); 
                    nuovaSpedizione.setEmail(utenteLoggato.getEmail());
                    nuovaSpedizione.setPec(request.getParameter("pec_azienda")); 
                    nuovaSpedizione.setIndirizzo(indirizzoSpedizione);
                    nuovaSpedizione.setCap(request.getParameter("cap_spedizione"));
                    nuovaSpedizione.setComune(request.getParameter("comune_spedizione"));
                    nuovaSpedizione.setProvincia(request.getParameter("provincia_spedizione"));
                    nuovaSpedizione.setNote(request.getParameter("note"));

                    System.out.println("DEBUG SPEDIZIONE: Chiamo model.aggiungiIndirizzoAzienda...");
                    model.aggiungiIndirizzoAzienda(nuovaSpedizione);
                    System.out.println("DEBUG SPEDIZIONE: Inserimento azienda completato con successo!");
                } else {
                    System.out.println("ERROR DEBUG: Il tipo utente '" + tipoUtente + "' non è valido.");
                }

            } else if ("rimuovi".equals(azione)) {
                String idSpedizioneStr = request.getParameter("id_spedizione");
                
                if (idSpedizioneStr != null && !idSpedizioneStr.isEmpty()) {
                    int idSpedizione = Integer.parseInt(idSpedizioneStr);
                    System.out.println("DEBUG SPEDIZIONE: Tento la rimozione dell'indirizzo con ID: " + idSpedizione);
                    
                    // Chiama il metodo di rimozione del tuo model passando anche il tipoUtente se serve nel tuo database
                    model.rimuoviIndirizzo(idSpedizione, tipoUtente);
                    
                    System.out.println("DEBUG SPEDIZIONE: Rimozione completata con successo!");
                } else {
                    System.out.println("DEBUG SPEDIZIONE WARNING: Richiesta rimozione ma 'id_spedizione' è vuoto.");
                }
            }
            
        } catch (SQLException e) {
            System.out.println("--- ERRORE CRITICO SQL NELLA SERVLET SPEDIZIONE ---");
            e.printStackTrace(); 
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.setAttribute("jakarta.servlet.error.status_code", 500);
            request.getRequestDispatcher("/WEB-INF/view/errore.jsp").forward(request, response);
            return;
        }

        // Ritorna sempre alla pagina utente aggiornata
        request.getRequestDispatcher("/WEB-INF/view/ALogin/utente.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/ALogin/utente.jsp").forward(request, response);
    }
}