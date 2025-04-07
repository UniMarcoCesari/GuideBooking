package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Luogo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String descrizione;
    private String posizione;
    private ArrayList<TipoVisita> tipiVisita;

    public Luogo(String nome, String descrizione, String posizione, ArrayList<TipoVisita> tipiVisita) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.posizione = posizione;
        this.tipiVisita = tipiVisita;
    }

    public String getNome() { return nome; }
    public String getDescrizione() { return descrizione; }
    public String getPosizione() { return posizione; }
    public ArrayList<TipoVisita> getTipiVisita() { return tipiVisita; }

    public void setNome(String nome) { this.nome = nome; }
    public void setDescrizione(String descrizione) {this.descrizione = descrizione; }
    public void setPosizione (String posizione) { this.posizione = posizione; }
    public void setTipiVisita(ArrayList<TipoVisita> tipiVisita) { this.tipiVisita = tipiVisita; }
}
