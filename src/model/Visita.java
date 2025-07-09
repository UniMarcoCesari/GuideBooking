package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Visita implements Serializable{
    private static final long serialVersionUID = 1L;
    public enum STATO_VISITA {
        PROPOSTA, COMPLETA, CONFERMATA, CANCELLATA, EFFETTUATA
    }

    private TipoVisita tipo;
    private LocalDate data;
    private Volontario guidaAssegnata;
    private STATO_VISITA stato;
    private List<Iscrizione> iscrizioni;;


    public Visita(TipoVisita tipo, LocalDate data, Volontario guidaAssegnata) {
        this.tipo = tipo;
        this.data = data;
        this.guidaAssegnata = guidaAssegnata;
        this.stato = STATO_VISITA.PROPOSTA;
        this.iscrizioni = new ArrayList<>();
    }

    public TipoVisita getTipo() {
        return tipo;
    }

    public void setTipo(TipoVisita tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Volontario getGuidaAssegnata() {
        return guidaAssegnata;
    }

    public void setGuidaAssegnata(Volontario guidaAssegnata) {
        this.guidaAssegnata = guidaAssegnata;
    }

    public STATO_VISITA getStato() {
        return stato;
    }

    public void setStato(STATO_VISITA stato) {
        this.stato = stato;
    }

    public List<Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    public void setIscrizioni(List<Iscrizione> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }

    public boolean aggiungiIscrizione(Iscrizione i) {
        int totale = getTotaleIscritti() + i.getNumeroPersone();
        if (totale <= tipo.getMaxPartecipanti()) {
            iscrizioni.add(i);
            return true;
        }
        return false;
    }
    
    public int getTotaleIscritti() {
        return iscrizioni.stream().mapToInt(Iscrizione::getNumeroPersone).sum();
    }

    public static String generaCodicePrenotazione() {
        return UUID.randomUUID().toString(); // oppure qualcosa di più leggibile se vuoi
    }

    
}
