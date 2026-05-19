package controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteBean;
import model.UtenteModel;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        UtenteModel model = new UtenteModel();

        String email = request.getParameter("email");
        String password = request.getParameter("password");        
        try {
            UtenteBean user = model.doRetrieveByKey(email); 

            if (user != null) {
                String hashedInput = hashPassword(password);

                if (user.getPwd().equals(hashedInput)) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("utente", user);

                    response.sendRedirect("ALogin/homelogin.jsp");
                } else {
                    request.setAttribute("errorMessage", "Password errata.");

                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Utente non trovato.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante il login.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}