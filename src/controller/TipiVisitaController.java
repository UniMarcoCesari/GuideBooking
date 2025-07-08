package controller;

import model.TipoVisita;
import model.Volontario;
import service.PersistentDataManager;
import costants.Costants;
import java.util.ArrayList;
import java.util.List;

public class TipiVisitaController {
    private List<TipoVisita> tipiVisita;
    private PersistentDataManager dataManager;

    public TipiVisitaController(PersistentDataManager dataManager) {
        this.dataManager = dataManager;
        tipiVisita = dataManager.caricaDati(Costants.file_tipi_visita);
    }

    public List<TipoVisita> getTipiVisita() {
        return tipiVisita;
    }

    public void rimuoviTipoVisita(TipoVisita tipoVisita) {
        tipiVisita.remove(tipoVisita);
        salvaDati();
    }

    public TipoVisita getTipoVisitaFromTitle(String titolo) {

        for(TipoVisita tipoVisita: tipiVisita) {
            if(tipoVisita.getTitolo().equals(titolo)) {
                return tipoVisita;
            }
        }

        return null;
    }

    public void aggiungiVisita(TipoVisita visita) {
        tipiVisita.add(visita);
        salvaDati();
    }

    public void rimuoviVisita(TipoVisita visita) {
        tipiVisita.remove(visita);
        salvaDati();
    }

    public void salvaDati() {
        dataManager.salvaDati(tipiVisita, Costants.file_tipi_visita);
    }

    public boolean titoloEsiste(String titolo) {
        return tipiVisita.stream().anyMatch(visita -> visita.getTitolo().equalsIgnoreCase(titolo));
    }


    public List<TipoVisita> getTipiVisitaPerVolontario(String username) {
        List<TipoVisita> tipiVisitaPerVolontario = new ArrayList<>();
        for (TipoVisita tipoVisita : tipiVisita) {
            if (tipoVisita.getVolontari().stream().anyMatch(volontario -> volontario.getNome().equalsIgnoreCase(username))) {
                tipiVisitaPerVolontario.add(tipoVisita);
            }
        }
        return tipiVisitaPerVolontario;
    }

    public void modificaTipoVisita(TipoVisita nuovaVisita) {
        for (TipoVisita tipoVisita : tipiVisita) {
            if (tipoVisita.getTitolo().equals(nuovaVisita.getTitolo())) {
                tipoVisita.modifica(nuovaVisita);
                break;
            }
        }
        salvaDati();

    }

    public void rimuoviVolonatario(Volontario volontario) {
        List<TipoVisita> tipiVisitaDaRimuovere = new ArrayList<>();
        for (TipoVisita tipoVisita : tipiVisita) {
            tipoVisita.rimuoviVolontario(volontario);
            if (tipoVisita.getVolontari().isEmpty()) {
                tipiVisitaDaRimuovere.add(tipoVisita);
            }
        }
        tipiVisita.removeAll(tipiVisitaDaRimuovere);
        salvaDati();
    }

    public void salvaOAggiornaTipoVisita(String titolo, String descrizione, String puntoIncontro, java.time.LocalDate dataInizio, java.time.LocalDate dataFine, java.util.Set<java.time.DayOfWeek> giorniSettimana, java.time.LocalTime oraInizio, int durata, boolean richiedeBiglietto, int minPartecipanti, int maxPartecipanti, ArrayList<Volontario> volontari, boolean isModifica) throws Exception {
        if (titolo.isEmpty() || descrizione.isEmpty() || puntoIncontro.isEmpty()) {
            throw new Exception("Tutti i campi devono essere compilati!");
        }
        if (volontari.isEmpty()) {
            throw new Exception("Seleziona almeno un volontario!");
        }
        if (giorniSettimana.isEmpty()) {
            throw new Exception("Seleziona almeno un giorno della settimana!");
        }
        if (dataInizio.isAfter(dataFine)) {
            throw new Exception("La data di inizio non può essere dopo la data di fine!");
        }
        if (!isModifica && titoloEsiste(titolo)) {
            throw new Exception("Esiste già una visita con questo titolo!");
        }
        if (minPartecipanti > maxPartecipanti) {
            throw new Exception("Il numero minimo di partecipanti non può essere maggiore del massimo!");
        }

        TipoVisita nuovaVisita = new TipoVisita(
                titolo,
                descrizione,
                puntoIncontro,
                dataInizio,
                dataFine,
                giorniSettimana,
                oraInizio,
                durata,
                richiedeBiglietto,
                minPartecipanti,
                maxPartecipanti,
                volontari
        );

        if (isModifica) {
            modificaTipoVisita(nuovaVisita);
        } else {
            aggiungiVisita(nuovaVisita);
        }
    }
}
