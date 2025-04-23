package model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Set;

public class TipoVisita implements Serializable {

    private String titolo;
    private String descrizione;
    private String puntoIncontro;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private Set<DayOfWeek> giorniSettimana;
    private LocalTime oraInizio;
    private int durataMinuti;
    private boolean bigliettoNecessario;
    private int minPartecipanti;
    private int maxPartecipanti;
    private ArrayList<Volontario> volontari;



    public TipoVisita(String titolo, String descrizione, String puntoIncontro,
                      LocalDate dataInizio, LocalDate dataFine, Set<DayOfWeek> giorniSettimana,
                      LocalTime oraInizio, int durataMinuti, boolean bigliettoNecessario,
                      int minPartecipanti, int maxPartecipanti, ArrayList<Volontario> volontari) {
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
        this.volontari = volontari;
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

    public Set<DayOfWeek> getGiorniSettimana() {
        return giorniSettimana;
    }

    public void setGiorniSettimana(Set<DayOfWeek> giorniSettimana) {
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

    public ArrayList<Volontario> getVolontari() {
        return volontari;
    }

    
    public void setVolontari(ArrayList<Volontario> volontari) {
        this.volontari = volontari;
    }

    public void rimuoviVolontario(Volontario volontario) {
        volontari.removeIf(v -> v.getNome().equals(volontario.getNome()));
    }

    public String getNome() {
        return titolo;
    }
}
