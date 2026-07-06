package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.DSConnectionPool;
import model.UtenteBean;

public class UtenteDAO {

	public UtenteBean doRetrieveByKey(String email) throws SQLException {
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    UtenteBean bean = null;

	    try {
	        connection = DSConnectionPool.getConnection();

	        String sqlUtente = "SELECT * FROM AnagraficaUtente WHERE Email = ?";
	        ps = connection.prepareStatement(sqlUtente);
	        ps.setString(1, email);
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            bean = new UtenteBean();
	            bean.setId(rs.getInt("ID"));
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
	public void doSavePrivato(UtenteBean bean) throws SQLException {
	    Connection connection = null;
	    PreparedStatement ps = null;

	    try {
	        connection = DSConnectionPool.getConnection();
	        connection.setAutoCommit(false);
	        
	        String sql = "INSERT INTO AnagraficaUtente (Nome, Cognome, Codice_Fiscale, Luogo_di_Nascita, Data_di_Nascita, Comune_Residenza, Indirizzo_Residenza, CAP_Residenza, Comune_Domicilio, Indirizzo_Domicilio, CAP_Domicilio, Email, Password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        
	        ps = connection.prepareStatement(sql);
	        ps.setString(1, bean.getNome());
	        ps.setString(2, bean.getCognome());
	        ps.setString(3, bean.getcodiceFiscale());
	        ps.setString(4, bean.getLuogoNascita());
	        ps.setDate(5, bean.getDataNascita());
	        ps.setString(6, bean.getComune());
	        ps.setString(7, bean.getIndirizzo());
	        ps.setString(8, bean.getCap());
	        ps.setString(9, bean.getComuneDomicilio());
	        ps.setString(10, bean.getIndirizzoDomicilio());
	        ps.setString(11, bean.getCapDomicilio());
	        ps.setString(12, bean.getEmail());
	        ps.setString(13, bean.getPwd());


	        ps.executeUpdate();
	        connection.commit();
	        System.out.println("DEBUG MODEL: Commit eseguito sul Database con successo!");

	    } catch (SQLException e) {
	        if (connection != null) {
	            try {
	                connection.rollback();
	                System.out.println("DEBUG MODEL: Errore SQL, eseguito Rollback.");
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        throw e; 
	    } finally {
	        if (ps != null) {
	            try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
	        }
	        if (connection != null) {
	        	try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
	        	connection.close();
	        }
	    }
	}

	public void doSaveAzienda(UtenteBean bean) throws SQLException {
	    Connection connection = null;
	    PreparedStatement ps = null;

	    try {
	        connection = DSConnectionPool.getConnection();
	        connection.setAutoCommit(false);
	        
	        String sql = "INSERT INTO AnagraficaPIVA (NomeAzienda, Partita_IVA, Comune_Legale, Indirizzo_Legale, CAP_Legale, PEC, Email, Password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	        
	        ps = connection.prepareStatement(sql);
	        ps.setString(1, bean.getNome()); 
	        ps.setString(2, bean.getcodiceFiscale());
	        ps.setString(3, bean.getComune());
	        ps.setString(4, bean.getIndirizzo());
	        ps.setString(5, bean.getCap());
	        ps.setString(6, bean.getPec());
	        ps.setString(7, bean.getEmail());
	        ps.setString(8, bean.getPwd());

	        ps.executeUpdate();
	        connection.commit();
	        System.out.println("DEBUG MODEL: Commit Azienda eseguito con successo!");

	    } catch (SQLException e) {
	        if (connection != null) {
	            try {
	                connection.rollback();
	                System.out.println("DEBUG MODEL: Errore SQL in doSaveAzienda, eseguito Rollback.");
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        throw e;
	    } finally {
	        if (ps != null) {
	            try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
	        }
	        if (connection != null) {
	        	try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
	            connection.close();
	        }
	    }
	}
}