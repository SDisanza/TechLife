package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import model.ProdottoBean;
import model.DriverManagerConnectionPool;
import model.DSConnectionPool;

public class ProdottoDAO {

    public void doSave(ProdottoBean prodotto) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO Prodotto (Nome, Categoria, Prezzo, Foto, Descrizione) VALUES (?, ?, ?, ?, ?)";

        try {
        	connection = model.DSConnectionPool.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getCategoria());
            ps.setDouble(3, prodotto.getPrezzo());
            ps.setString(4, prodotto.getFoto());
            ps.setString(5, prodotto.getDescrizione());
            
            ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
    }

    public void doDelete(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        String sql = "DELETE FROM Prodotto WHERE ID = ?";

        try {
            connection = DSConnectionPool.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            
            ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
    }
    
    public ProdottoBean doRetrieveByKey(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProdottoBean bean = null;

        String sql = "SELECT * FROM Prodotto WHERE ID = ?";

        try {
            connection = DriverManagerConnectionPool.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                bean = new ProdottoBean();
                bean.setId(rs.getInt("ID"));
                bean.setNome(rs.getString("Nome"));
                bean.setCategoria(rs.getString("Categoria"));
                bean.setPrezzo(rs.getDouble("Prezzo"));
                bean.setFoto(rs.getString("Foto"));
                bean.setDescrizione(rs.getString("Descrizione"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
        return bean;
    }

    //Recupera tutti i prodotti
    public Collection<ProdottoBean> doRetrieveAll() throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection<ProdottoBean> prodotti = new ArrayList<>();

        String sql = "SELECT * FROM Prodotto";

        try {
        	connection = DSConnectionPool.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProdottoBean bean = new ProdottoBean();
                bean.setId(rs.getInt("ID"));
                bean.setNome(rs.getString("Nome"));
                bean.setCategoria(rs.getString("Categoria"));
                bean.setPrezzo(rs.getDouble("Prezzo"));
                bean.setFoto(rs.getString("Foto"));
                bean.setDescrizione(rs.getString("Descrizione"));
                prodotti.add(bean);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
        return prodotti;
    }
}