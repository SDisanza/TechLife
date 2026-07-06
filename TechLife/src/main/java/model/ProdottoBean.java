package model;

import java.io.Serializable;

public class ProdottoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String descrizione;
    private double prezzo;
    private String foto;
    private String categoria;

    public ProdottoBean() {}

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescrizione() { return descrizione; }
    public double getPrezzo() { return prezzo; }
    public String getFoto() { return foto; }
    public String getCategoria() { return categoria;}

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }
    public void setFoto(String foto) { this.foto = foto; }
    public void setCategoria(String categoria) {this.categoria = categoria; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProdottoBean other = (ProdottoBean) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
    
    
}