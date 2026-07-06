package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import model.DSConnectionPool;
import model.OrdineBean;

public class OrdineDAO {

    // Recupera tutti gli ordini effettuati da un determinato cliente
    public Collection<OrdineBean> doRetrieveByCliente(int idCliente) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection<OrdineBean> ordini = new ArrayList<>();

        String sql = "SELECT * FROM Ordine WHERE ID_Cliente = ? ORDER BY Data_Ordine DESC";

        try {
            connection = DSConnectionPool.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idCliente);
            rs = ps.executeQuery();

            while (rs.next()) {
                OrdineBean bean = new OrdineBean();
                bean.setId(rs.getInt("ID"));
                bean.setIdCliente(rs.getInt("ID_Cliente"));
                bean.setTipoCliente(rs.getString("Tipo_Cliente"));
                bean.setDataOrdine(rs.getTimestamp("Data_Ordine"));
                bean.setTotaleOrdine(rs.getDouble("Totale_Ordine"));
                ordini.add(bean);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
        return ordini;
    }

    // Recupera un singolo ordine tramite il suo ID (fondamentale per rigenerare la fattura)
    public OrdineBean doRetrieveByKey(int idOrdine) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrdineBean bean = null;

        String sql = "SELECT * FROM Ordine WHERE ID = ?";

        try {
            connection = DSConnectionPool.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idOrdine);
            rs = ps.executeQuery();

            if (rs.next()) {
                bean = new OrdineBean();
                bean.setId(rs.getInt("ID"));
                bean.setIdCliente(rs.getInt("ID_Cliente"));
                bean.setTipoCliente(rs.getString("Tipo_Cliente"));
                bean.setDataOrdine(rs.getTimestamp("Data_Ordine"));
                bean.setTotaleOrdine(rs.getDouble("Totale_Ordine"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
        return bean;
    }
    
 // Recupera TUTTI gli ordini presenti nel database (Fondamentale per il pannello Admin)
    public Collection<OrdineBean> doRetrieveAll() throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection<OrdineBean> ordini = new ArrayList<>();

        String sql = "SELECT * FROM Ordine ORDER BY Data_Ordine DESC";

        try {
            connection = DSConnectionPool.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                OrdineBean bean = new OrdineBean();
                bean.setId(rs.getInt("ID"));
                bean.setIdCliente(rs.getInt("ID_Cliente"));
                bean.setTipoCliente(rs.getString("Tipo_Cliente"));
                bean.setDataOrdine(rs.getTimestamp("Data_Ordine"));
                bean.setTotaleOrdine(rs.getDouble("Totale_Ordine"));
                ordini.add(bean);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
        return ordini;
    }
}