package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import model.ProdottoBean;
import dao.ProdottoDAO;

@WebServlet("/AdminProdottoServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AdminProdottoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String ruolo = (String) session.getAttribute("tipo");
        
        if (!"admin".equals(ruolo)) {
            response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=login");
            return;
        }

        String azione = request.getParameter("azione");
        ProdottoDAO prodottoDAO = new ProdottoDAO();

        try {
            if ("aggiungi".equals(azione)) {
                String nome = request.getParameter("nome");
                String descrizione = request.getParameter("descrizione");
                double prezzo = Double.parseDouble(request.getParameter("prezzo"));
                String categoria = request.getParameter("categoria");

                // Gestione del caricamento file dal Browser
                Part filePart = request.getPart("immagine");
                String fileName = getSubmittedFileName(filePart);
                
                // Definiamo il percorso relativo standard che si aspetta il tuo catalogo
                String percorsoRelativoDB = "img/prodotti/" + fileName;

                // Troviamo il percorso di deploy reale sul server Tomcat
                String appPath = request.getServletContext().getRealPath("");
                String savePath = appPath + File.separator + "img" + File.separator + "prodotti";
                
                // Creiamo la cartella se non esiste nel deploy di Tomcat
                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }

                // Scriviamo il file fisicamente sul server
                filePart.write(savePath + File.separator + fileName);
                System.out.println("DEBUG UPLOAD: File salvato in -> " + savePath + File.separator + fileName);

                // Mappatura sul Bean
                ProdottoBean nuovoProdotto = new ProdottoBean();
                nuovoProdotto.setNome(nome);
                nuovoProdotto.setDescrizione(descrizione);
                nuovoProdotto.setPrezzo(prezzo);
                nuovoProdotto.setCategoria(categoria);
                nuovoProdotto.setFoto(percorsoRelativoDB); // Nel DB salviamo sempre la stringa "img/prodotti/nome.jpg"

                prodottoDAO.doSave(nuovoProdotto);
                System.out.println("DEBUG ADMIN DB: Prodotto salvato con successo.");

            } else if ("rimuovi".equals(azione)) {
                int idProdotto = Integer.parseInt(request.getParameter("id_prodotto"));
                prodottoDAO.doDelete(idProdotto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=admin");
    }

    // Helper method per estrarre il nome del file dall'header HTTP della Part
    private String getSubmittedFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "default.jpg";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=admin");
    }
}