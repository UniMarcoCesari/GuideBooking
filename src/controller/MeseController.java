package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Mese;
import model.TipoVisita;
import model.Volontario;

public class MeseController {
    private final TipiVisitaController tipiVisitaController;
    private final VolontariController volontariController;
    private final CalendarioController calendarioController;
    private Map<LocalDate, TipoVisita> pianoVisite; // Date con il tipo di visita
    private Map<LocalDate, List<Volontario>> disponibilitaVolontari; // Volontari disponibili per giorno

    public MeseController(TipiVisitaController tipiVisitaController, VolontariController volontariController, CalendarioController calendarioController) {
        this.tipiVisitaController = tipiVisitaController;
        this.volontariController = volontariController;
        this.calendarioController = calendarioController;
        this.pianoVisite = new HashMap<>();
        this.disponibilitaVolontari = new HashMap<>();
    }

    public Map<LocalDate, TipoVisita> getPianoVisite() {
        return pianoVisite;
    }

    public void setPianoVisite(Map<LocalDate, TipoVisita> pianoVisite) {
        this.pianoVisite = pianoVisite;
    }

    public Map<LocalDate, List<Volontario>> getDisponibilitaVolontari() {
        return disponibilitaVolontari;
    }

    public void setDisponibilitaVolontari(Map<LocalDate, List<Volontario>> disponibilitaVolontari) {
        this.disponibilitaVolontari = disponibilitaVolontari;
    }

    // Metodo per raccogliere le date precluse per un mese
    public Set<LocalDate> raccogliDatePrecluse(Mese mese) {
        return calendarioController.getDatePrecluse(mese.getNumeroMese(), mese.getAnno());
    }

    // Metodo per raccogliere la disponibilità dei volontari e le date precluse
    public void raccogliDisponibilitaVolontari(Mese mese) {
        // Ottieni tutte le date nel mese
        Set<LocalDate> dateMese = getDateDelMese(mese);
        
        // Filtra le date precluse per il mese
        Set<LocalDate> datePrecluse = calendarioController.getDatePrecluse(mese.getNumeroMese(), mese.getAnno());

        // Raccogli la disponibilità dei volontari per ogni giorno del mese
        for (LocalDate data : dateMese) {
            // Se la data non è preclusa
            if (!datePrecluse.contains(data)) {
                List<Volontario> volontariDisponibili = volontariController.getDisponibilitaVolontari().getOrDefault(data, new ArrayList<>());
                disponibilitaVolontari.put(data, volontariDisponibili);
            }
        }

        // Stampa per verificare
        System.out.println("Disponibilità dei volontari per il mese " + mese.getNumeroMese() + "/" + mese.getAnno());
        for (Map.Entry<LocalDate, List<Volontario>> entry : disponibilitaVolontari.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().size() + " volontari disponibili");
        }
    }

    // Metodo per ottenere tutte le date di un mese
    private Set<LocalDate> getDateDelMese(Mese mese) {
        Set<LocalDate> dateMese = new HashSet<>();
        LocalDate primoGiornoMese = LocalDate.of(mese.getAnno(), mese.getNumeroMese(), 1);
        LocalDate ultimoGiornoMese = primoGiornoMese.withDayOfMonth(primoGiornoMese.lengthOfMonth());

        for (LocalDate data = primoGiornoMese; !data.isAfter(ultimoGiornoMese); data = data.plusDays(1)) {
            dateMese.add(data);
        }

        return dateMese;
    }

    // Metodo A - Produzione del piano delle visite
    public void produzionePianoVisite(Mese mese) {
        List<TipoVisita> visiteDisponibili = tipiVisitaController.getTipiVisita();

        if (visiteDisponibili.isEmpty()) {
            System.out.println("Nessuna visita disponibile per il mese " + mese.getNumeroMese());
            return;
        }

       // Map<LocalDate, TipoVisita> pianoVisite = mese.getPianoVisite();

        //??????
    }
}