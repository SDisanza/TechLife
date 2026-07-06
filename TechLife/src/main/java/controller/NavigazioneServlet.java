package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/NavigazioneServlet")
public class NavigazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pagina = request.getParameter("page");
        
        if (pagina == null || pagina.isEmpty()) {
            request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
            return;
        }

        switch (pagina) {
            case "home":
                request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
                break;
            case "login":
                request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
                break;
            case "registrazione":
                request.getRequestDispatcher("/WEB-INF/view/registrazione.jsp").forward(request, response);
                break;
            case "catalogo":
                request.getRequestDispatcher("/WEB-INF/view/catalogo.jsp").forward(request, response);
                break;
            case "recupero":
                request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
                break;
            case "chisiamo":
                request.getRequestDispatcher("/WEB-INF/view/chi siamo.jsp").forward(request, response);
                break;
            case "dovesiamo":
                request.getRequestDispatcher("/WEB-INF/view/dove siamo.jsp").forward(request, response);
                break;
            case "carrello":
                    request.getRequestDispatcher("/WEB-INF/view/carrello.jsp").forward(request, response);
                break;
            case "checkout":
                HttpSession sessionCheckout = request.getSession();
                model.UtenteBean utenteLoggato = (model.UtenteBean) sessionCheckout.getAttribute("utente");
                
                if (utenteLoggato == null) {
                    request.setAttribute("errorMessage", "Effettua l'accesso per poter procedere al checkout.");
                    request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
                    return;
                }
                
                dao.SpedizioneDAO spedizioneModel = new dao.SpedizioneDAO();
                String tipoUtente = (String) sessionCheckout.getAttribute("tipo");
                
                try {
                    java.util.Collection<model.SpedizioneBean> listaIndirizzi;
                    if ("azienda".equals(tipoUtente)) {
                        listaIndirizzi = spedizioneModel.getIndirizziAzienda(utenteLoggato.getId());
                    } else {
                        listaIndirizzi = spedizioneModel.getIndirizziPrivato(utenteLoggato.getId());
                    }
                    
                    request.setAttribute("listaIndirizzi", listaIndirizzi);
                    
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                    System.out.println("DEBUG CHECKOUT: Errore nel recupero degli indirizzi di spedizione.");
                }
                
                request.getRequestDispatcher("/WEB-INF/view/checkout.jsp").forward(request, response);
                break;
                
            // Pagine dell'area riservata (sotto il controllo del filtro)
            case "homelogin":
                request.getRequestDispatcher("/WEB-INF/view/ALogin/homelogin.jsp").forward(request, response);
                break;
            case "catalogologin":
                request.getRequestDispatcher("/WEB-INF/view/ALogin/catalogologin.jsp").forward(request, response);
                break;
            case "profilo":
            case "utente":
                HttpSession sessionProfilo = request.getSession();
                model.UtenteBean ut = (model.UtenteBean) sessionProfilo.getAttribute("utente");
                
                if (ut != null) {
                    try {
                        dao.OrdineDAO ordineModel = new dao.OrdineDAO();
                        java.util.Collection<model.OrdineBean> storicoOrdini = ordineModel.doRetrieveByCliente(ut.getId());
                        request.setAttribute("storicoOrdini", storicoOrdini);
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                    }
                }
                request.getRequestDispatcher("/WEB-INF/view/ALogin/utente.jsp").forward(request, response);
                break;

            case "vediFattura":
                HttpSession sessionFattura = request.getSession();
                model.UtenteBean utenteFattura = (model.UtenteBean) sessionFattura.getAttribute("utente");
                String idOrdString = request.getParameter("id");

                if (utenteFattura == null || idOrdString == null) {
                    request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
                    return;
                }

                try {
                    int idOrdine = Integer.parseInt(idOrdString);
                    dao.OrdineDAO oModel = new dao.OrdineDAO();
                    model.OrdineBean ordine = oModel.doRetrieveByKey(idOrdine);

                    // Controllo di sicurezza: l'ordine deve appartenere all'utente loggato
                    if (ordine != null && ordine.getIdCliente() == utenteFattura.getId()) {
                        
                        // 1. Settaggio ContentType
                        response.setContentType("application/pdf");
                        response.setHeader("Content-Disposition", "attachment; filename=Fattura_TechLife_Order_" + ordine.getId() + ".pdf");

                        // 2. Creazione documento
                        com.lowagie.text.Document document = new com.lowagie.text.Document();
                        com.lowagie.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());

                        // 3. Apertura del documento
                        document.open();

                        com.lowagie.text.Paragraph intestazione = new com.lowagie.text.Paragraph();
                        intestazione.add("TechLife S.r.l.\n");
                        intestazione.add("Via dell'Innovazione, 42 - 84044 Battipaglia (SA)\n");
                        intestazione.add("P.IVA: 01234567890\n\n");
                        document.add(intestazione);

                        com.lowagie.text.Paragraph titolo = new com.lowagie.text.Paragraph(
                            "FATTURA ORDINE", 
                            com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 18)
                        );
                        titolo.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                        document.add(titolo);
                        document.add(new com.lowagie.text.Paragraph("\n"));

                        java.text.SimpleDateFormat sdfPdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                        com.lowagie.text.Paragraph dettagli = new com.lowagie.text.Paragraph();
                        dettagli.add("Fattura Numero: FT-" + ordine.getId() * 123 + "\n");
                        dettagli.add("Data Emissione: " + sdfPdf.format(ordine.getDataOrdine()) + "\n");
                        dettagli.add("Intestatario: " + utenteFattura.getNome() + " " + utenteFattura.getCognome() + "\n");
                        dettagli.add("Identificativo Fiscale: " + utenteFattura.getcodiceFiscale() + "\n");
                        dettagli.add("Stato Pagamento: Corrisposto\n\n");
                        document.add(dettagli);

                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(3);
                        table.setWidthPercentage(100);
                        
                        table.addCell("Descrizione Hardware/Servizio");
                        table.addCell("Quantità");
                        table.addCell("Prezzo Totale");

                        table.addCell("Fornitura ed evasione Dispositivi TechLife - Ordine #" + ordine.getId());
                        table.addCell("1");
                        table.addCell("EUR " + String.format(java.util.Locale.US, "%,.2f", ordine.getTotaleOrdine()));
                        document.add(table);

                        document.add(new com.lowagie.text.Paragraph("\n"));

                        com.lowagie.text.Paragraph totaleBlock = new com.lowagie.text.Paragraph(
                            "TOTALE COMPLESSIVO: EUR " + String.format(java.util.Locale.US, "%,.2f", ordine.getTotaleOrdine()),
                            com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 14)
                        );
                        totaleBlock.setAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
                        document.add(totaleBlock);

                        // 4. Chiudiamo il documento
                        document.close();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                request.getRequestDispatcher("/WEB-INF/view/errore.jsp").forward(request, response);
                break;

            default:
                request.getRequestDispatcher("/WEB-INF/view/errore.jsp").forward(request, response);
                break;
            case "admin":
                HttpSession sessionAdmin = request.getSession();
                String ruoloAdmin = (String) sessionAdmin.getAttribute("tipo");
                
                // 1. Controllo di sicurezza
                if (!"admin".equals(ruoloAdmin)) {
                    response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=login");
                    return;
                }
                
                try {
                    dao.ProdottoDAO prodottoDAO = new dao.ProdottoDAO();
                    dao.OrdineDAO ordineModel = new dao.OrdineDAO();
                    

                    java.util.Collection<model.ProdottoBean> listaProdotti = prodottoDAO.doRetrieveAll();
                    java.util.Collection<model.OrdineBean> listaOrdini = ordineModel.doRetrieveAll(); 
                    
                    request.setAttribute("listaProdotti", listaProdotti);
                    request.setAttribute("listaOrdini", listaOrdini);
                    
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                    System.out.println("DEBUG ADMIN: Errore nel caricamento dei dati della dashboard.");
                }
                
                request.getRequestDispatcher("/WEB-INF/view/ALogin/dashboard.jsp").forward(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}