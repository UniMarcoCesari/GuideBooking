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
}
