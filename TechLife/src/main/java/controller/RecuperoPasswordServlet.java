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
import model.DriverManagerConnectionPool;

@WebServlet("/RecuperoPasswordServlet")
public class RecuperoPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String azione = request.getParameter("azione");
        String email = request.getParameter("email");

        if (azione == null) {
            response.sendRedirect("recupero.jsp");
            return;
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = DriverManagerConnectionPool.getConnection();

            // ====================================================================
            // STEP 1: VERIFICA SE L'EMAIL ESISTE E IDENTIFICA LA TIPOLOGIA ACCOUNT
            // ====================================================================
            if ("verificaEmail".equals(azione)) {
                if (email == null || email.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Il campo email è obbligatorio.");
                    request.getRequestDispatcher("recupero.jsp").forward(request, response);
                    return;
                }

                // 1. Controllo nella tabella dei clienti privati
                String queryPrivato = "SELECT Email FROM AnagraficaUtente WHERE Email = ?";
                ps = connection.prepareStatement(queryPrivato);
                ps.setString(1, email);
                rs = ps.executeQuery();

                if (rs.next()) {
                    request.setAttribute("emailVerificata", email);
                    request.setAttribute("tipoUtente", "privato");
                    request.getRequestDispatcher("recupero.jsp").forward(request, response);
                    return;
                }
                
                // Chiudo le risorse temporanee per riutilizzare i puntatori
                rs.close();
                ps.close();
                
                // 2. Controllo nella tabella delle aziende / P.IVA
                String queryAzienda = "SELECT Email FROM AnagraficaPIVA WHERE Email = ?";
                ps = connection.prepareStatement(queryAzienda);
                ps.setString(1, email);
                rs = ps.executeQuery();

                if (rs.next()) {
                    request.setAttribute("emailVerificata", email);
                    request.setAttribute("tipoUtente", "azienda");
                    request.getRequestDispatcher("recupero.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Nessun account associato a questo indirizzo email.");
                    request.getRequestDispatcher("recupero.jsp").forward(request, response);
                }
            }
            
            // ====================================================================
            // STEP 2: VALIDAZIONE ED ESECUZIONE DEL RESET DELLA PASSWORD
            // ====================================================================
            else if ("aggiorna".equals(azione)) {
                String nuovaPassword = request.getParameter("nuovaPassword");
                String confermaPassword = request.getParameter("confermaPassword");
                String tipoUtente = request.getParameter("tipoUtente"); 

                if (nuovaPassword == null || nuovaPassword.isEmpty() || confermaPassword == null || confermaPassword.isEmpty()) {
                    request.setAttribute("errorMessage", "Entrambi i campi password sono obbligatori.");
                    request.setAttribute("emailVerificata", email);
                    request.setAttribute("tipoUtente", tipoUtente);
                    request.getRequestDispatcher("recupero.jsp").forward(request, response);
                    return;
                }

                if (!nuovaPassword.equals(confermaPassword)) {
                    request.setAttribute("errorMessage", "Le password inserite non corrispondono.");
                    request.setAttribute("emailVerificata", email);
                    request.setAttribute("tipoUtente", tipoUtente);
                    request.getRequestDispatcher("recupero.jsp").forward(request, response);
                    return;
                }

                // VALORE AGGIUNTO (PILASTRO 1): Riutilizzo del metodo di hashing centralizzato in LoginServlet
                String passwordCriptata = controller.LoginServlet.hashPassword(nuovaPassword);
                String updateQuery = "";

                // Selezione mirata della tabella corretta in base all'esito dello Step 1
                if ("azienda".equals(tipoUtente)) {
                    updateQuery = "UPDATE AnagraficaPIVA SET Password = ? WHERE Email = ?";
                } else {
                    updateQuery = "UPDATE AnagraficaUtente SET Password = ? WHERE Email = ?";
                }

                ps = connection.prepareStatement(updateQuery);
                ps.setString(1, passwordCriptata);
                ps.setString(2, email);
                ps.executeUpdate();

                // Gestione transazione in caso di auto-commit disattivato nel pool
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }

                // Reset completato con successo
                response.sendRedirect("recupero.jsp?status=success");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore del database durante l'elaborazione.");
            request.getRequestDispatcher("recupero.jsp").forward(request, response);
        } finally {
            // Chiusura sicura e pulita di tutte le connessioni utilizzate
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) DriverManagerConnectionPool.releaseConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect("recupero.jsp");
    }
}