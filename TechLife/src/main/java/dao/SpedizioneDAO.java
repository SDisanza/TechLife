package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import model.DSConnectionPool;
import model.SpedizioneBean;

public class SpedizioneDAO {

    public void aggiungiIndirizzoUtente(SpedizioneBean bean) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO Spedizione_Utente (ID_Utente, Email_Utente, Indirizzo_Spedizione, CAP_Spedizione, Comune_Spedizione, Provincia_Spedizione, Note) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = DSConnectionPool.getConnection();
            connection.setAutoCommit(false);
            
            ps = connection.prepareStatement(sql);
            ps.setInt(1, bean.getIdUtenteAzienda());
            ps.setString(2, bean.getEmail());
            ps.setString(3, bean.getIndirizzo());
            ps.setString(4, bean.getCap());
            ps.setString(5, bean.getComune());
            ps.setString(6, bean.getProvincia());
            ps.setString(7, bean.getNote());

            ps.executeUpdate();
            
            System.out.println("DEBUG SPEDIZIONE: aggiungiIndirizzoUtente completato con commit!");
        } finally {
            if (ps != null) ps.close();
            if (connection != null) {
            	connection.setAutoCommit(true);
            	connection.close();
            }
        }
    }
	
    public void aggiungiIndirizzoAzienda(SpedizioneBean bean) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO Spedizione_Azienda (ID_Azienda, Email_Azienda, Pec_Azienda, Indirizzo_Spedizione, CAP_Spedizione, Comune_Spedizione, Provincia_Spedizione, Note) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = DSConnectionPool.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            ps.setInt(1, bean.getIdUtenteAzienda());
            ps.setString(2, bean.getEmail());
            ps.setString(3, bean.getPec());
            ps.setString(4, bean.getIndirizzo());
            ps.setString(5, bean.getCap());
            ps.setString(6, bean.getComune());
            ps.setString(7, bean.getProvincia());
            ps.setString(8, bean.getNote());

            ps.executeUpdate();
            connection.commit();
            System.out.println("DEBUG SPEDIZIONE: aggiungiIndirizzoAzienda completato con commit!");
        } finally {
            if (ps != null) ps.close();
            if (connection != null) {
            	connection.setAutoCommit(true);
            	connection.close();
            }
        }
    }
	
    public Collection<SpedizioneBean> getIndirizziPrivato(int idUtente) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        Collection<SpedizioneBean> lista = new ArrayList<>();

        String sql = "SELECT * FROM Spedizione_Utente WHERE ID_Utente = ?";

        try {
            connection = DSConnectionPool.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idUtente);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SpedizioneBean bean = new SpedizioneBean();
                bean.setId(rs.getInt("ID"));
                bean.setIdUtenteAzienda(rs.getInt("ID_Utente"));
                bean.setEmail(rs.getString("Email_Utente"));
                bean.setIndirizzo(rs.getString("Indirizzo_Spedizione"));
                bean.setCap(rs.getString("CAP_Spedizione"));
                bean.setComune(rs.getString("Comune_Spedizione"));
                bean.setProvincia(rs.getString("Provincia_Spedizione"));
                bean.setNote(rs.getString("Note"));
                lista.add(bean);
            }
        } finally {
            if (ps != null) ps.close();
            if (connection != null) {
            	connection.setAutoCommit(true);
            	connection.close();
            }
        }
        return lista;
    }
	
    public Collection<SpedizioneBean> getIndirizziAzienda(int idAzienda) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        Collection<SpedizioneBean> lista = new ArrayList<>();

        String sql = "SELECT * FROM Spedizione_Azienda WHERE ID_Azienda = ?";

        try {
            connection = DSConnectionPool.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SpedizioneBean bean = new SpedizioneBean();
                bean.setId(rs.getInt("ID"));
                bean.setIdUtenteAzienda(rs.getInt("ID_Azienda"));
                bean.setEmail(rs.getString("Email_Azienda"));
                bean.setPec(rs.getString("Pec_Azienda"));
                bean.setIndirizzo(rs.getString("Indirizzo_Spedizione"));
                bean.setCap(rs.getString("CAP_Spedizione"));
                bean.setComune(rs.getString("Comune_Spedizione"));
                bean.setProvincia(rs.getString("Provincia_Spedizione"));
                bean.setNote(rs.getString("Note"));
                lista.add(bean);
            }
        } finally {
            if (ps != null) ps.close();
            if (connection != null) {
            		connection.setAutoCommit(true);
            		connection.close();            	
            }
        }
        return lista;
    }

    public void rimuoviIndirizzo(int idSpedizione, String tipoUtente) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        String tabella = "privato".equals(tipoUtente) ? "Spedizione_Utente" : "Spedizione_Azienda";
        String sql = "DELETE FROM " + tabella + " WHERE ID = ?";

        try {
            connection = DSConnectionPool.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idSpedizione);

            ps.executeUpdate();
            connection.commit();
            System.out.println("DEBUG SPEDIZIONE: rimuoviIndirizzo eseguito con successo su " + tabella);
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (ps != null) ps.close();
            if (connection != null) {
            	connection.setAutoCommit(true);
            	connection.close();
            }
        }
    }

    public boolean checkIndirizzoEsistente(int idUtenteAzienda, String indirizzo, String tipoUtente) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean esiste = false;

        try {
            con = DSConnectionPool.getConnection();
            
            String query;
            if ("privato".equals(tipoUtente)) {
                // CORRETTO: la colonna reale sul DB è Indirizzo_Spedizione
                query = "SELECT COUNT(*) FROM Spedizione_Utente WHERE ID_Utente = ? AND Indirizzo_Spedizione = ?";
            } else {
                // CORRETTO: la colonna reale sul DB è Indirizzo_Spedizione
                query = "SELECT COUNT(*) FROM Spedizione_Azienda WHERE ID_Azienda = ? AND Indirizzo_Spedizione = ?";
            }

            ps = con.prepareStatement(query);
            ps.setInt(1, idUtenteAzienda);
            ps.setString(2, indirizzo);
            rs = ps.executeQuery();

            if (rs.next()) {
                esiste = rs.getInt(1) > 0;
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return esiste;
    }
}