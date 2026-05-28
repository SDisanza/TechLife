package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/NavigazioneServlet")
public class NavigazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pagina = request.getParameter("page");
        
        // Se l'utente non specifica la pagina, lo mandiamo alla Index (Home pubblica)
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
                
            // Pagine dell'area riservata (sotto il controllo del filtro)
            case "homelogin":
                request.getRequestDispatcher("/WEB-INF/view/ALogin/homelogin.jsp").forward(request, response);
                break;
            case "catalogologin":
                request.getRequestDispatcher("/WEB-INF/view/ALogin/catalogologin.jsp").forward(request, response);
                break;
            case "profilo":
                request.getRequestDispatcher("/WEB-INF/view/ALogin/utente.jsp").forward(request, response);
                break;
                
            default:
                request.getRequestDispatcher("/WEB-INF/view/errore.jsp").forward(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}