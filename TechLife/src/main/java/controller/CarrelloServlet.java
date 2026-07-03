package controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Carrello;
import model.ProdottoBean;
import model.ProdottoModel;

@WebServlet("/CarrelloServlet")
public class CarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // 1. Recuperiamo il carrello dalla sessione. Se non c'è, lo creiamo.
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        if (carrello == null) {
            carrello = new Carrello();
            session.setAttribute("carrello", carrello);
        }

        String azione = request.getParameter("azione");
        String idString = request.getParameter("idProdotto");
        
        System.out.println("DEBUG CARRELLO: Azione = " + azione + ", ID Prodotto = " + idString);

        ProdottoModel model = new ProdottoModel();

        try {
            if ("aggiungi".equals(azione)) {
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    // Prendiamo il prodotto aggiornato dal DB
                    ProdottoBean prodotto = model.doRetrieveByKey(id);
                    if (prodotto != null) {
                        carrello.aggiungiProdotto(prodotto, 1);
                        System.out.println("DEBUG CARRELLO: Prodotto " + prodotto.getNome() + " aggiunto.");
                    }
                }
            } 
            
            else if ("rimuovi".equals(azione)) {
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    ProdottoBean prodotto = model.doRetrieveByKey(id);
                    if (prodotto != null) {
                        carrello.rimuoviProdotto(prodotto);
                        System.out.println("DEBUG CARRELLO: Prodotto rimosso.");
                    }
                }
            } 
            
            else if ("aggiornaQuantita".equals(azione)) {
                String quantitaString = request.getParameter("quantita");
                if (idString != null && quantitaString != null) {
                    int id = Integer.parseInt(idString);
                    int nuovaQuantita = Integer.parseInt(quantitaString);
                    ProdottoBean producto = model.doRetrieveByKey(id);
                    if (producto != null) {
                        carrello.aggiornaQuantita(producto, nuovaQuantita);
                        System.out.println("DEBUG CARRELLO: Quantità aggiornata a " + nuovaQuantita);
                    }
                }
            } 
            
            else if ("svuota".equals(azione)) {
                carrello.svuota();
                System.out.println("DEBUG CARRELLO: Carrello svuotato.");
            }
            //Qualsiasi operazione si faccia si sincronizza il DB
            model.UtenteBean utenteLoggato = (model.UtenteBean) session.getAttribute("utente");
            if (utenteLoggato != null) {
                try {
                    model.CarrelloModel carrelloModel = new model.CarrelloModel();
                    carrelloModel.salvaCarrello(utenteLoggato.getId(), carrello);
                    System.out.println("DEBUG CARRELLO DB: Carrello salvato su database per utente #" + utenteLoggato.getId());
                } catch (java.sql.SQLException e) {
                    System.out.println("DEBUG CARRELLO DB: Errore durante il salvataggio su database.");
                    e.printStackTrace();
                }
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=carrello");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
}