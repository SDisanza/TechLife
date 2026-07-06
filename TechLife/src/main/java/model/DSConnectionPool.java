package model;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DSConnectionPool {

    private static DataSource ds;

    static {
        try {
            InitialContext initContext = new InitialContext();
            ds = (DataSource) initContext.lookup("java:comp/env/jdbc/TechLifeDB");
            System.out.println("DEBUG DATASOURCE: Inizializzato con successo per TechLife!");
        } catch (NamingException e) {
            System.err.println("ERRORE CRITICO DATASOURCE JNDI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            throw new SQLException("DataSource non configurato o non inizializzato correttamente.");
        }
        return ds.getConnection();
    }
}