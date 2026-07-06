package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DSConnectionPool;
import model.DriverManagerConnectionPool;

@WebServlet("/RecuperoPasswordServlet")
public class RecuperoPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String azione = request.getParameter("azione");
        String email = request.getParameter("email");

        // DEBUG INIZIALE
        System.out.println("=== REQ: RecuperoPasswordServlet ===");
        System.out.println("-> Azione ricevuta: " + azione);
        System.out.println("-> Email ricevuta: " + email);

        if (azione == null) {
            System.out.println("-> Errore: Azione nulla. Reindirizzamento in corso.");
            response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=recupero");
            return;
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = DSConnectionPool.getConnection();

            if ("verificaEmail".equals(azione)) {
                System.out.println("-> Fase: Verifica Email");
                if (email == null || email.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Il campo email è obbligatorio.");
                    request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
                    return;
                }

                String queryPrivato = "SELECT Email FROM AnagraficaUtente WHERE Email = ?";
                ps = connection.prepareStatement(queryPrivato);
                ps.setString(1, email);
                rs = ps.executeQuery();

                if (rs.next()) {
                    System.out.println("-> Trovato in AnagraficaUtente (Privato)");
                    request.setAttribute("emailVerificata", email);
                    request.setAttribute("tipoUtente", "privato");
                    request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
                    return;
                }
                
                rs.close();
                ps.close();
                
                String queryAzienda = "SELECT Email FROM AnagraficaPIVA WHERE Email = ?";
                ps = connection.prepareStatement(queryAzienda);
                ps.setString(1, email);
                rs = ps.executeQuery();

                if (rs.next()) {
                    System.out.println("-> Trovato in AnagraficaPIVA (Azienda)");
                    request.setAttribute("emailVerificata", email);
                    request.setAttribute("tipoUtente", "azienda");
                    request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
                } else {
                    System.out.println("-> Email NON trovata in nessuna tabella");
                    request.setAttribute("errorMessage", "Nessun account associato a questo indirizzo email.");
                    request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
                }
            }
            
            else if ("aggiornaPassword".equals(azione)) {
                String nuovaPassword = request.getParameter("nuovaPassword");
                String confermaPassword = request.getParameter("confermaPassword");
                String tipoUtente = request.getParameter("tipoUtente");
                
                System.out.println("-> Fase: Aggiorna Password");
                System.out.println("-> tipoUtente: " + tipoUtente);
                System.out.println("-> nuovaPassword ricevuta (lunghezza): " + (nuovaPassword != null ? nuovaPassword.length() : "null"));

                if (nuovaPassword == null || nuovaPassword.trim().isEmpty() || !LoginServlet.isPasswordSicura(nuovaPassword)) {
                    System.out.println("-> Errore: Requisiti di complessità falliti lato server.");
                    request.setAttribute("errorMessage", "La nuova password non rispetta i requisiti di sicurezza richiesti.");
                    request.setAttribute("emailVerificata", email);
                    request.setAttribute("tipoUtente", tipoUtente);
                    request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
                    return;
                }

                if (confermaPassword == null || confermaPassword.isEmpty() || !nuovaPassword.equals(confermaPassword)) {
                    System.out.println("-> Errore: Le due password non coincidono.");
                    request.setAttribute("errorMessage", "Le password inserite non corrispondono.");
                    request.setAttribute("emailVerificata", email);
                    request.setAttribute("tipoUtente", tipoUtente);
                    request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
                    return;
                }

                String passwordCriptata = controller.LoginServlet.hashPassword(nuovaPassword);
                String updateQuery = "azienda".equals(tipoUtente) ? 
                    "UPDATE AnagraficaPIVA SET Password = ? WHERE Email = ?" : 
                    "UPDATE AnagraficaUtente SET Password = ? WHERE Email = ?";

                System.out.println("-> Esecuzione UPDATE SQL su tabella per " + tipoUtente);
                ps = connection.prepareStatement(updateQuery);
                ps.setString(1, passwordCriptata);
                ps.setString(2, email);
                int rows = ps.executeUpdate();
                System.out.println("-> Righe modificate nel DB: " + rows);

                if (!connection.getAutoCommit()) {
                    connection.commit();
                }

                System.out.println("-> Fine processo con SUCCESSO.");
                request.setAttribute("status", "success");
                request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
            } else {
                System.out.println("-> Errore critico: Nessuna azione riconosciuta. Ricevuto: " + azione);
                // Fallback di sicurezza se l'azione si disallinea
                request.setAttribute("errorMessage", "Azione non riconosciuta dal server.");
                request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("-> Eccezione SQL generata!");
            request.setAttribute("errorMessage", "Errore del database durante l'elaborazione.");
            request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/recupero.jsp").forward(request, response);
    }
}