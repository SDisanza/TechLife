package model;

import java.io.Serializable;

public class SpedizioneBean implements Serializable{
	public static final long serialVersionUID = 1L;
	
	private int id;
	private int idUtenteAzienda; 
    private String email;
    private String pec;
    private String indirizzo;
    private String cap;
    private String comune;
    private String provincia;
    private String note;
    
    public SpedizioneBean() {}
    
    //getters
	public int getId() {return id;}
	public int getIdUtenteAzienda() {return idUtenteAzienda;}
	public String getEmail() {return email;}
	public String getPec() {return pec;}
	public String getIndirizzo() {return indirizzo;}
	public String getCap() {return cap;}
	public String getComune() {return comune;}
	public String getProvincia() {return provincia;}
	public String getNote() {return note;}
	
	//setters
	public void setId(int id) {this.id = id;}
	public void setIdUtenteAzienda(int idUtenteAzienda) {this.idUtenteAzienda = idUtenteAzienda;}
	public void setEmail(String email) {this.email = email;}
	public void setPec(String pec) {this.pec = pec;}
	public void setIndirizzo(String indirizzo) {this.indirizzo = indirizzo;}
	public void setCap(String cap) {this.cap = cap;}
	public void setComune(String comune) {this.comune = comune;}
	public void setProvincia(String provincia) {this.provincia = provincia;}
	public void setNote(String note) {this.note = note;}
}
