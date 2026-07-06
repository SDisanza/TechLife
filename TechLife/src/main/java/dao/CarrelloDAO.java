package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import model.Carrello;
import model.ProdottoBean;
import model.DSConnectionPool;

public class CarrelloDAO {

    // Sincronizzazione
    public void salvaCarrello(int idUtente, Carrello carrello) throws SQLException {
        Connection connection = null;
        PreparedStatement psDelete = null;
        PreparedStatement psInsert = null;

        try {
            connection = DSConnectionPool.getConnection();
            connection.setAutoCommit(false);
            
            // 1. Puliamo il vecchio carrello salvato per evitare duplicati
            String sqlDelete = "DELETE FROM Carrello_Salvato WHERE ID_Utente = ?";
            psDelete = connection.prepareStatement(sqlDelete);
            psDelete.setInt(1, idUtente);
            psDelete.executeUpdate();

            // 2. Se carrello è not empty, li salviamo nel database
            if (carrello != null && !carrello.getElementi().isEmpty()) {
                String sqlInsert = "INSERT INTO Carrello_Salvato (ID_Utente, ID_Prodotto, Quantita) VALUES (?, ?, ?)";
                psInsert = connection.prepareStatement(sqlInsert);

                for (Map.Entry<ProdottoBean, Integer> entry : carrello.getElementi().entrySet()) {
                    psInsert.setInt(1, idUtente);
                    psInsert.setInt(2, entry.getKey().getId());
                    psInsert.setInt(3, entry.getValue());
                    psInsert.addBatch();
                }
                psInsert.executeBatch();
            }
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (psDelete != null) psDelete.close();
            if (psInsert != null) psInsert.close();
            if (connection != null) connection.close();
        }
    }

    // Recupero carrello
    public Carrello recuperaCarrello(int idUtente) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Carrello carrello = new Carrello();
        ProdottoDAO prodottoDAO = new ProdottoDAO();

        String sql = "SELECT ID_Prodotto, Quantita FROM Carrello_Salvato WHERE ID_Utente = ?";

        try {
            connection = DSConnectionPool.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idUtente);
            rs = ps.executeQuery();

            while (rs.next()) {
                int idProdotto = rs.getInt("ID_Prodotto");
                int quantita = rs.getInt("Quantita");
                
                ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(idProdotto);
                if (prodotto != null) {
                    carrello.aggiungiProdotto(prodotto, quantita);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
        return carrello;
    }
}