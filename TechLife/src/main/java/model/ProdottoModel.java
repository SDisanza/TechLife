package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ProdottoModel {

    // 1. Recupera un singolo prodotto tramite il suo ID
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
            if (connection != null) DriverManagerConnectionPool.releaseConnection(connection);
        }
        return bean;
    }

    // 2. Recupera tutti i prodotti (Fondamentale per la pagina del catalogo)
    public Collection<ProdottoBean> doRetrieveAll() throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection<ProdottoBean> prodotti = new ArrayList<>();

        String sql = "SELECT * FROM Prodotto";

        try {
            connection = DriverManagerConnectionPool.getConnection();
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
            if (connection != null) DriverManagerConnectionPool.releaseConnection(connection);
        }
        return prodotti;
    }
}