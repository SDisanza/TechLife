package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Carrello implements Serializable {
    private static final long serialVersionUID = 1L;

    // Associa il ProdottoBean alla sua quantità quantitativa
    private Map<ProdottoBean, Integer> elementi;

    public Carrello() {
        this.elementi = new HashMap<>();
    }

    public Map<ProdottoBean, Integer> getElementi() {
        return elementi;
    }

    // Aggiunge un prodotto o ne incrementa la quantità
    public void aggiungiProdotto(ProdottoBean prodotto, int quantita) {
        if (elementi.containsKey(prodotto)) {
            elementi.put(prodotto, elementi.get(prodotto) + quantita);
        } else {
            elementi.put(prodotto, quantita);
        }
    }

    // Rimuove l'articolo dal carrello
    public void rimuoviProdotto(ProdottoBean prodotto) {
        elementi.remove(prodotto);
    }

    // Modifica il numero di pezzi (es. dai selettori + e - della pagina)
    public void aggiornaQuantita(ProdottoBean prodotto, int nuovaQuantita) {
        if (nuovaQuantita <= 0) {
            rimuoviProdotto(prodotto);
        } else {
            elementi.put(prodotto, nuovaQuantita);
        }
    }

    public void svuota() {
        elementi.clear();
    }

    // Per mostrare il contatore sull'icona del carrello nella navbar
    public int getQuantitaTotale() {
        int totale = 0;
        for (int q : elementi.values()) {
            totale += q;
        }
        return totale;
    }

    // Calcola il costo totale di tutti i prodotti inseriti
    public double getPrezzoTotale() {
        double totale = 0;
        for (Map.Entry<ProdottoBean, Integer> entry : elementi.entrySet()) {
            totale += entry.getKey().getPrezzo() * entry.getValue();
        }
        return totale;
    }
}