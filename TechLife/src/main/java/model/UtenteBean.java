package model;

import java.io.Serializable;
import java.sql.Date;

public class UtenteBean implements Serializable {
	    private static final long serialVersionUID = 1L;

	    private String nome;
	    private String cognome;
	    private String email;
	    private String pwd;
	    private String codiceFiscale;
	    private Date dataNascita;

	    public UtenteBean() {}
	    //setters
	    public void setNome(String nome) { this.nome = nome;}
	    public void setCognome(String cognome) {this.cognome = cognome; }
	    public void setEmail(String email) {this.email = email;}
	    public void setPwd(String pwd) {this.pwd = pwd;}
	    public void setcodiceFiscale(String codiceFiscale) {this.codiceFiscale = codiceFiscale;}
	    public void setDataNascita(Date dataNascita) {this.dataNascita = dataNascita;}
	    //getters
	    public String getNome() { return nome; }
	    public String getCognome() { return cognome; }
	    public String getEmail() { return email; }
	    public String getPwd() { return pwd; }
	    public String getcodiceFiscale() { return codiceFiscale; }
	    public Date getDataNascita() { return dataNascita; }
	}