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

@WebServlet("/ValidaAjaxServlet")
public class ValidaAjaxServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Riceve il tipo di controllo (email, cf, p_iva) e il valore inserito dall'utente
        String tipo = request.getParameter("tipo");
        String valore = request.getParameter("valore");
        boolean esiste = false;

        if (tipo != null && valore != null && !valore.trim().isEmpty()) {
            valore = valore.trim();
            
            Connection connection = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            ResultSet rs1 = null;
            ResultSet rs2 = null;

            try {
                connection = DriverManagerConnectionPool.getConnection();

                switch (tipo) {
                    case "email":
                        // L'email va cercata in entrambe le tabelle anagrafiche
                        String qEmailPrivato = "SELECT Email FROM AnagraficaUtente WHERE Email = ?";
                        ps1 = connection.prepareStatement(qEmailPrivato);
                        ps1.setString(1, valore);
                        rs1 = ps1.executeQuery();

                        if (rs1.next()) {
                            esiste = true;
                        } else {
                            String qEmailAzienda = "SELECT Email FROM AnagraficaPIVA WHERE Email = ?";
                            ps2 = connection.prepareStatement(qEmailAzienda);
                            ps2.setString(1, valore);
                            rs2 = ps2.executeQuery();
                            if (rs2.next()) {
                                esiste = true;
                            }
                        }
                        break;

                    case "cf":
                        // Il Codice Fiscale va cercato solo nella tabella dei privati
                        String qCF = "SELECT Codice_Fiscale FROM AnagraficaUtente WHERE Codice_Fiscale = ?";
                        ps1 = connection.prepareStatement(qCF);
                        ps1.setString(1, valore);
                        rs1 = ps1.executeQuery();
                        
                        if (rs1.next()) {
                            esiste = true;
                        }
                        break;

                    case "p_iva":
                        // La Partita IVA va cercata solo nella tabella delle aziende
                        String qPiva = "SELECT Partita_IVA FROM AnagraficaPIVA WHERE Partita_IVA = ?";
                        ps1 = connection.prepareStatement(qPiva);
                        ps1.setString(1, valore);
                        rs1 = ps1.executeQuery();
                        
                        if (rs1.next()) {
                            esiste = true;
                        }
                        break;
                        
                    default:
                        // Se viene passato un tipo non censito, non fa nulla
                        break;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Chiusura sicura di tutte le risorse utilizzate nella richiesta
                try {
                    if (rs1 != null) rs1.close();
                    if (ps1 != null) ps1.close();
                    if (rs2 != null) rs2.close();
                    if (ps2 != null) ps2.close();
                    if (connection != null) DriverManagerConnectionPool.releaseConnection(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Imposta la risposta HTTP come JSON puro
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Restituisce l'oggetto JSON leggero al browser
        response.getWriter().write("{\"esiste\": " + esiste + "}");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Blocca eventuali tentativi di accesso diretto tramite URL
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}