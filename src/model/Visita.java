package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Visita implements Serializable{
    public enum STATO_VISITA {
        PROPOSTA, COMPLETA, CONFERMATA, CANCELLATA, EFFETTUATA
    }

    private TipoVisita tipo;
    private LocalDate data;
    private Volontario guidaAssegnata;
    private STATO_VISITA stato;

    public Visita(TipoVisita tipo, LocalDate data, Volontario guidaAssegnata) {
        this.tipo = tipo;
        this.data = data;
        this.guidaAssegnata = guidaAssegnata;
        this.stato = STATO_VISITA.PROPOSTA;
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
}

