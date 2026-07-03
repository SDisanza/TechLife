package controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.UtenteBean;
import model.UtenteModel;
import model.DriverManagerConnectionPool;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        UtenteModel model = new UtenteModel();

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String hashedInput = hashPassword(password);
        
        boolean isAziendaUser = false; 
        UtenteBean user = null;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();

            System.out.println("DEBUG LOGIN: Avvio tentativo di ricerca per Email: " + email);

            // TENTATIVO 1: Cerchiamo nella tabella dei Privati
            ps = con.prepareStatement("SELECT ID, Email, Password, Nome, Cognome, Codice_Fiscale, Data_di_Nascita, Luogo_di_Nascita, Comune_Residenza, Indirizzo_Residenza, CAP_Residenza, Comune_Domicilio, Indirizzo_Domicilio, CAP_Domicilio FROM anagraficautente WHERE Email = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new UtenteBean();
                user.setId(rs.getInt("ID"));
                user.setEmail(rs.getString("Email"));
                user.setPwd(rs.getString("Password"));
                user.setNome(rs.getString("Nome"));
                user.setCognome(rs.getString("Cognome"));
                user.setcodiceFiscale(rs.getString("Codice_Fiscale"));
                user.setDataNascita(rs.getDate("Data_di_Nascita"));
                user.setLuogoNascita(rs.getString("Luogo_di_Nascita"));
                user.setComune(rs.getString("Comune_Residenza"));
                user.setIndirizzo(rs.getString("Indirizzo_Residenza"));
                user.setCap(rs.getString("CAP_Residenza"));
                user.setComuneDomicilio(rs.getString("Comune_Domicilio"));
                user.setIndirizzoDomicilio(rs.getString("Indirizzo_Domicilio"));
                user.setCapDomicilio(rs.getString("CAP_Domicilio"));
                
                isAziendaUser = false;
                System.out.println("DEBUG LOGIN: Trovato account PRIVATO nel DB. ID: " + user.getId());
            } else {
                // Chiudiamo i vecchi oggetti prima di riutilizzarli per l'azienda
                rs.close();
                ps.close();

                // TENTATIVO 2: Cerchiamo nella tabella delle Aziende con i tuoi campi reali
                ps = con.prepareStatement("SELECT ID, Email, Password, NomeAzienda, Partita_IVA, Comune_Legale, Indirizzo_Legale, CAP_Legale, PEC FROM AnagraficaPIVA WHERE Email = ?");
                ps.setString(1, email);
                rs = ps.executeQuery();

                if (rs.next()) {
                    user = new UtenteBean();
                    user.setId(rs.getInt("ID"));
                    user.setEmail(rs.getString("Email"));
                    user.setPwd(rs.getString("Password"));
                    user.setNome(rs.getString("NomeAzienda"));
                    user.setCognome("Azienda / P.IVA");
                    user.setcodiceFiscale(rs.getString("Partita_IVA"));
                    user.setComune(rs.getString("Comune_Legale"));
                    user.setIndirizzo(rs.getString("Indirizzo_Legale"));
                    user.setCap(rs.getString("CAP_Legale"));
                    user.setPec(rs.getString("PEC"));
                    
                    isAziendaUser = true;
                    System.out.println("DEBUG LOGIN: Trovato account AZIENDA nel DB. ID: " + user.getId());
                }
            }

            // Verifica della password
            if (user != null) {
                if (user.getPwd().equals(hashedInput)) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("utente", user);
                    session.setAttribute("tipo", isAziendaUser ? "azienda" : "privato");
                    
                    try {
                        model.CarrelloModel carrelloModel = new model.CarrelloModel();
                        // Recuperiamo il carrello nel DB
                        model.Carrello carrelloSalvato = carrelloModel.recuperaCarrello(user.getId());
                        session.setAttribute("carrello", carrelloSalvato);
                        System.out.println("DEBUG LOGIN DB: Carrello ripristinato dal DB per l'utente ID #" + user.getId());
                    } catch (java.sql.SQLException e) {
                        System.out.println("DEBUG LOGIN DB: Errore nel ripristino del carrello dal DB. Creo un carrello vuoto.");
                        e.printStackTrace();
                        // Se errore allora nuovo carrello
                        session.setAttribute("carrello", new model.Carrello());
                    }

                    System.out.println("DEBUG LOGIN: Login autorizzato con successo per " + email + " | Tipo: " + (isAziendaUser ? "azienda" : "privato"));
                    request.getRequestDispatcher("/WEB-INF/view/ALogin/homelogin.jsp").forward(request, response);
                    return;
                } else {
                    System.out.println("DEBUG LOGIN: La password non coincide per " + email);
                    request.setAttribute("errorMessage", "Password errata.");
                    request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
                }
            } else {
                System.out.println("DEBUG LOGIN: Nessun utente trovato con questa email.");
                request.setAttribute("errorMessage", "Utente non trovato.");
                request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            System.out.println("--- ERRORE CRITICO SQL DURANTE IL LOGIN ---");
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore di connessione al database.");
            response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=login");
            return;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) DriverManagerConnectionPool.releaseConnection(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return java.util.Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isPasswordSicura(String password) {
        if (password == null) return false;
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[.,@$!%*?&!#_])[A-Za-z\\d.,@$!%*?&!#_]{8,}$";
        return password.matches(regex);
    }
}