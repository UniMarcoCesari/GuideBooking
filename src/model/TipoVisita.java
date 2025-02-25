package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TipoVisita implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titolo;
    private String descrizione;
    private String puntoIncontro;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private List<String> giorniSettimana;
    private LocalTime oraInizio;
    private int durataMinuti;
    private boolean bigliettoNecessario;
    private int minPartecipanti;
    private int maxPartecipanti;



    public TipoVisita(String titolo, String descrizione, String puntoIncontro,
                      LocalDate dataInizio, LocalDate dataFine, List<String> giorniSettimana,
                      LocalTime oraInizio, int durataMinuti, boolean bigliettoNecessario,
                      int minPartecipanti, int maxPartecipanti) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.puntoIncontro = puntoIncontro;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.giorniSettimana = giorniSettimana;
        this.oraInizio = oraInizio;
        this.durataMinuti = durataMinuti;
        this.bigliettoNecessario = bigliettoNecessario;
        this.minPartecipanti = minPartecipanti;
        this.maxPartecipanti = maxPartecipanti;
    }

    // Getters e setters
    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPuntoIncontro() {
        return puntoIncontro;
    }

    public void setPuntoIncontro(String puntoIncontro) {
        this.puntoIncontro = puntoIncontro;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public List<String> getGiorniSettimana() {
        return giorniSettimana;
    }

    public void setGiorniSettimana(List<String> giorniSettimana) {
        this.giorniSettimana = giorniSettimana;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public int getDurataMinuti() {
        return durataMinuti;
    }

    public void setDurataMinuti(int durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    public boolean isBigliettoNecessario() {
        return bigliettoNecessario;
    }

    public void setBigliettoNecessario(boolean bigliettoNecessario) {
        this.bigliettoNecessario = bigliettoNecessario;
    }

    public int getMinPartecipanti() {
        return minPartecipanti;
    }

    public void setMinPartecipanti(int minPartecipanti) {
        this.minPartecipanti = minPartecipanti;
    }

    public int getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public void setMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }
}
