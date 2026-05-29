package controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteBean;
import model.UtenteModel;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

        request.setCharacterEncoding("UTF-8");
        
        String tipoUtente = request.getParameter("tipo_utente");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!LoginServlet.isPasswordSicura(password)) {
            request.setAttribute("errorMessage", "La password non rispetta i requisiti di sicurezza (minimo 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale).");
            request.getRequestDispatcher("/WEB-INF/view/registrazione.jsp").forward(request, response);
            return;
        }        

        String passwordCifrata = LoginServlet.hashPassword(password);
        
        UtenteModel model = new UtenteModel();
        UtenteBean bean = new UtenteBean();
        bean.setEmail(email);
        bean.setPwd(passwordCifrata);


        boolean isRegistrato = false;

        try {
            if ("privato".equals(tipoUtente)) {
                bean.setNome(request.getParameter("nome"));
                bean.setCognome(request.getParameter("cognome"));
                bean.setcodiceFiscale(request.getParameter("codice_fiscale"));
                bean.setLuogoNascita(request.getParameter("luogo_nascita"));
                
                String dataNascString = request.getParameter("data_nascita");
                if (dataNascString != null && !dataNascString.isEmpty()) {
                    bean.setDataNascita(Date.valueOf(dataNascString));
                }

                String comuneRes = request.getParameter("comune_res");
                String indRes = request.getParameter("ind_res");
                String capRes = request.getParameter("cap_res");
                
                bean.setComune(comuneRes);
                bean.setIndirizzo(indRes);
                bean.setCap(capRes);
                
                String domDiverso = request.getParameter("domicilio_diverso");
                
                if (domDiverso != null) {
                    bean.setComuneDomicilio(request.getParameter("comune_dom"));
                    bean.setIndirizzoDomicilio(request.getParameter("ind_dom"));
                    bean.setCapDomicilio(request.getParameter("cap_dom"));
                } else {
                    bean.setComuneDomicilio(comuneRes);
                    bean.setIndirizzoDomicilio(indRes);
                    bean.setCapDomicilio(capRes);
                }
                
                model.doSavePrivato(bean);
                bean = model.doRetrieveByKey(email);
                System.out.println("DEBUG: Eseguito doSavePrivato con successo!");
                isRegistrato = true;
                
            } else if ("azienda".equals(tipoUtente)) {
                bean.setNome(request.getParameter("nome_azienda"));
                bean.setcodiceFiscale(request.getParameter("p_iva"));
                bean.setComune(request.getParameter("comune_leg"));
                bean.setIndirizzo(request.getParameter("ind_leg"));
                bean.setCap(request.getParameter("cap_leg"));
                bean.setPec(request.getParameter("pec"));
                
                model.doSaveAzienda(bean);
                bean = model.doRetrieveByKey(email);
                System.out.println("DEBUG: Eseguito doSaveAzienda con successo!");
                isRegistrato = true;
                
            } else {
                System.out.println("ERROR DEBUG: Il tipo utente ricevuto non è valido! Valore: '" + tipoUtente + "'");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante la registrazione. Email o Codice Fiscale/P.IVA già esistenti.");
            request.getRequestDispatcher("/WEB-INF/view/registrazione.jsp").forward(request, response);
            return;
        }


        if (isRegistrato) {
            System.out.println("DEBUG: Creo la sessione per l'utente registrato.");
            

            HttpSession session = request.getSession();
            session.setAttribute("utente", bean);
            session.setAttribute("tipo", tipoUtente); 

            System.out.println("DEBUG: Reindirizzamento a /ALogin/homelogin.jsp");
            

            request.getRequestDispatcher("/WEB-INF/view/ALogin/homelogin.jsp").forward(request, response);
        } else {
            response.sendRedirect("registrazione.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.getRequestDispatcher("/WEB-INF/view/registrazione.jsp").forward(request, response);
    }
}