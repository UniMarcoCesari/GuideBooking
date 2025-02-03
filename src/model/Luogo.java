package model;

import java.io.Serializable;

public class Luogo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String descrizione;
    private String posizione;

    public Luogo(String nome, String descrizione, String posizione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.posizione = posizione;
    }

    public String getNome() { return nome; }
    public String getDescrizione() { return descrizione; }
    public String getPosizione() { return posizione; }
}
