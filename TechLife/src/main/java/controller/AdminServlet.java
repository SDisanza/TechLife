package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.DriverManagerConnectionPool;
import model.DSConnectionPool;
import model.OrdineBean;
import model.ProdottoBean;
import model.UtenteBean;
import dao.ProdottoDAO;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

        String tipoUtente = (String) request.getSession().getAttribute("tipo");
        if (!"admin".equals(tipoUtente)) {
            response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=login");
            return;
        }

        String azione = request.getParameter("azione");
        if (azione == null) {
            azione = "dashboard";
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DSConnectionPool.getConnection();
            
            if ("dashboard".equals(azione)) {

                ProdottoDAO prodottoDAO = new ProdottoDAO();
                Collection<ProdottoBean> listaProdotti = prodottoDAO.doRetrieveAll();
                request.setAttribute("listaProdotti", listaProdotti);

                Collection<OrdineBean> tuttiOrdini = new ArrayList<>();
                String sqlOrdini = "SELECT * FROM Ordine ORDER BY Data_Ordine DESC";
                ps = con.prepareStatement(sqlOrdini);
                rs = ps.executeQuery();
                while (rs.next()) {
                    OrdineBean o = new OrdineBean();
                    o.setId(rs.getInt("ID"));
                    o.setIdCliente(rs.getInt("ID_Cliente"));
                    o.setTipoCliente(rs.getString("Tipo_Cliente"));
                    o.setDataOrdine(rs.getTimestamp("Data_Ordine"));
                    o.setTotaleOrdine(rs.getDouble("Totale_Ordine"));
                    tuttiOrdini.add(o);
                }
                request.setAttribute("listaOrdini", tuttiOrdini);
                
                request.getRequestDispatcher("/WEB-INF/view/Alogin/dashboard.jsp").forward(request, response);
            }

            // 2. VISUALIZZAZIONE REGISTRO CLIENTI (PRIVATI + AZIENDE)
            else if ("listaClienti".equals(azione)) {
                Collection<UtenteBean> listaClienti = new ArrayList<>();

                String sqlPrivati = "SELECT ID, Nome, Cognome, Email, Codice_Fiscale FROM AnagraficaUtente";
                ps = con.prepareStatement(sqlPrivati);
                rs = ps.executeQuery();
                while (rs.next()) {
                    UtenteBean u = new UtenteBean();
                    u.setId(rs.getInt("ID"));
                    u.setNome(rs.getString("Nome"));
                    u.setCognome(rs.getString("Cognome"));
                    u.setEmail(rs.getString("Email"));
                    u.setcodiceFiscale(rs.getString("Codice_Fiscale"));
                    listaClienti.add(u);
                }
                rs.close();
                ps.close();

                // Estraiamo le aziende
                String sqlAziende = "SELECT ID, NomeAzienda, Email, Partita_IVA FROM AnagraficaPIVA";
                ps = con.prepareStatement(sqlAziende);
                rs = ps.executeQuery();
                while (rs.next()) {
                    UtenteBean u = new UtenteBean();
                    u.setId(rs.getInt("ID"));
                    u.setNome(rs.getString("NomeAzienda"));
                    u.setCognome("Azienda / P.IVA");
                    u.setEmail(rs.getString("Email"));
                    u.setcodiceFiscale(rs.getString("Partita_IVA"));
                    listaClienti.add(u);
                }

                request.setAttribute("listaClienti", listaClienti);
                request.getRequestDispatcher("/WEB-INF/view/ALogin/clienti.jsp").forward(request, response);
            }

            // 3. SCENARIO: REGISTRO ORDINI FILTRATO PER DATA
            else if ("listaOrdini".equals(azione)) {
                String dataInizio = request.getParameter("dataInizio");
                String dataFine = request.getParameter("dataFine");
                Collection<OrdineBean> ordiniFiltrati = new ArrayList<>();

                String query;
                if (dataInizio != null && dataFine != null && !dataInizio.isEmpty() && !dataFine.isEmpty()) {

                    query = "SELECT * FROM Ordine WHERE Data_Ordine BETWEEN ? AND ? ORDER BY Data_Ordine DESC";
                    ps = con.prepareStatement(query);
                    ps.setString(1, dataInizio + " 00:00:00");
                    ps.setString(2, dataFine + " 23:59:59");
                } else {

                    query = "SELECT * FROM Ordine ORDER BY Data_Ordine DESC";
                    ps = con.prepareStatement(query);
                }

                rs = ps.executeQuery();
                while (rs.next()) {
                    OrdineBean o = new OrdineBean();
                    o.setId(rs.getInt("ID"));
                    o.setIdCliente(rs.getInt("ID_Cliente"));
                    o.setTipoCliente(rs.getString("Tipo_Cliente"));
                    o.setDataOrdine(rs.getTimestamp("Data_Ordine"));
                    o.setTotaleOrdine(rs.getDouble("Totale_Ordine"));
                    ordiniFiltrati.add(o);
                }

                request.setAttribute("listaOrdini", ordiniFiltrati);
                request.getRequestDispatcher("/WEB-INF/view/ALogin/ordini.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=errore");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String tipoUtente = (String) request.getSession().getAttribute("tipo");
        if (!"admin".equals(tipoUtente)) {
            response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=login");
            return;
        }

        String azione = request.getParameter("azione");
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DSConnectionPool.getConnection();

            // 4. SCENARIO: ELIMINAZIONE SINGOLO CLIENTE
            if ("eliminaCliente".equals(azione)) {
                String idClienteStr = request.getParameter("id_cliente");
                String tipoCliente = request.getParameter("tipo_cliente");

                if (idClienteStr != null && !idClienteStr.isEmpty()) {
                    int idCliente = Integer.parseInt(idClienteStr);
                    String sqlDelete;

                    if ("Azienda / P.IVA".equals(tipoCliente)) {
                        sqlDelete = "DELETE FROM AnagraficaPIVA WHERE ID = ?";
                    } else {
                        sqlDelete = "DELETE FROM AnagraficaUtente WHERE ID = ?";
                    }

                    ps = con.prepareStatement(sqlDelete);
                    ps.setInt(1, idCliente);
                    ps.executeUpdate();
                    System.out.println("DEBUG ADMIN: Cliente #" + idCliente + " eliminato con successo!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/NavigazioneServlet?page=listaClienti");
    }
}