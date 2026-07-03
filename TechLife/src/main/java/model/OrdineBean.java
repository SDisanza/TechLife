package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrdineBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int idCliente;
    private String tipoCliente;
    private Timestamp dataOrdine;
    private double totaleOrdine;

    public OrdineBean() {}

    // Getters
    public int getId() { return id; }
    public int getIdCliente() { return idCliente; }
    public String getTipoCliente() { return tipoCliente; }
    public Timestamp getDataOrdine() { return dataOrdine; }
    public double getTotaleOrdine() { return totaleOrdine; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
    public void setDataOrdine(Timestamp dataOrdine) { this.dataOrdine = dataOrdine; }
    public void setTotaleOrdine(double totaleOrdine) { this.totaleOrdine = totaleOrdine; }
}