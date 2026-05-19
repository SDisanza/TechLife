package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteModel {

	public UtenteBean doRetrieveByKey(String email) throws SQLException {
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    UtenteBean bean = null;

	    try {
	        connection = DriverManagerConnectionPool.getConnection();

	        String sqlUtente = "SELECT * FROM AnagraficaUtente WHERE Email = ?";
	        ps = connection.prepareStatement(sqlUtente);
	        ps.setString(1, email);
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            bean = new UtenteBean();
	            bean.setNome(rs.getString("Nome"));
	            bean.setCognome(rs.getString("Cognome"));
	            bean.setEmail(rs.getString("Email"));
	            bean.setPwd(rs.getString("Password"));
	            bean.setcodiceFiscale(rs.getString("Codice_Fiscale")); 
	            bean.setDataNascita(rs.getDate("Data_di_Nascita"));
	            return bean;
	        }
	        
	        rs.close();
	        ps.close();


	        String sqlAzienda = "SELECT * FROM AnagraficaPIVA WHERE Email = ?";
	        ps = connection.prepareStatement(sqlAzienda);
	        ps.setString(1, email);
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            bean = new UtenteBean();
	            bean.setNome(rs.getString("NomeAzienda")); 
	            bean.setCognome("Azienda / P.IVA");       
	            bean.setEmail(rs.getString("Email"));
	            bean.setPwd(rs.getString("Password"));
	            bean.setcodiceFiscale(rs.getString("Partita_IVA")); 
	            return bean;
	        }

	    } finally {
	        if (rs != null) rs.close();
	        if (ps != null) ps.close();
	        if (connection != null) connection.close();
	    }
	    return null; 
	}
}