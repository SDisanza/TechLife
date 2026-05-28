package model;

import java.io.Serializable;
import java.sql.Date;

public class UtenteBean implements Serializable {
	    private static final long serialVersionUID = 1L;
	    
	    private int id;
	    private String nome;
	    private String cognome;
	    private String email;
	    private String pwd;
	    private String codiceFiscale;
	    private Date dataNascita;
	    private String luogoNascita;
	    private String comune;
	    private String indirizzo;
	    private String cap;
	    private String pec;
	    private String comuneDomicilio;
	    private String indirizzoDomicilio;
	    private String capDomicilio;

		public UtenteBean() {}
	    //setters
		public void setId(int id) { this.id = id;}
	    public void setNome(String nome) { this.nome = nome;}
	    public void setCognome(String cognome) {this.cognome = cognome; }
	    public void setEmail(String email) {this.email = email;}
	    public void setPwd(String pwd) {this.pwd = pwd;}
	    public void setcodiceFiscale(String codiceFiscale) {this.codiceFiscale = codiceFiscale;}
	    public void setDataNascita(Date dataNascita) {this.dataNascita = dataNascita;}
	    public void setLuogoNascita(String luogoNascita) { this.luogoNascita = luogoNascita; }
	    public void setComune(String comune) { this.comune = comune; }
	    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
	    public void setCap(String cap) { this.cap = cap; }
	    public void setPec(String pec) { this.pec = pec; }
	    public void setComuneDomicilio(String comuneDomicilio) {this.comuneDomicilio = comuneDomicilio;}
	    public void setIndirizzoDomicilio(String indirizzoDomicilio) {this.indirizzoDomicilio = indirizzoDomicilio;}
	    public void setCapDomicilio(String capDomicilio) {this.capDomicilio = capDomicilio;}
	    //getters
	    public int getId() { return id;}
	    public String getNome() { return nome; }
	    public String getCognome() { return cognome; }
	    public String getEmail() { return email; }
	    public String getPwd() { return pwd; }
	    public String getcodiceFiscale() { return codiceFiscale; }
	    public Date getDataNascita() { return dataNascita; }
	    public String getLuogoNascita() { return luogoNascita; }
	    public String getComune() { return comune; }
	    public String getIndirizzo() { return indirizzo; }
	    public String getCap() { return cap; }
	    public String getPec() { return pec; }
	    public String getComuneDomicilio() {return comuneDomicilio;}
	    public String getCapDomicilio() {return capDomicilio;}
	    public String getIndirizzoDomicilio() {return indirizzoDomicilio;}
	}