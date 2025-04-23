package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import model.TipoVisita;
import model.Visita;
import model.Volontario;
import service.DataManager;

public class VisiteController {

    private final CalendarioController calendarioController;
    private final TipiVisitaController tipiVisitaController;
    private final VolontariController volontariController;
    private final LuoghiController luoghiController;


    private List<Visita> visite;
    
    public VisiteController(CalendarioController calendarioController,
            LuoghiController luoghiController, TipiVisitaController tipiVisitaController,VolontariController volontariController) {
        this.calendarioController = calendarioController;
        this.tipiVisitaController = tipiVisitaController;
        this.luoghiController = luoghiController;
        this.volontariController = volontariController;
        this.visite = DataManager.leggiDatiVisite();
    }

    public void generaVisite() {
        LocalDate oggi = calendarioController.getDatacDateCorrenteLocalDate();
    
        if (!calendarioController.isGiornoDiGenerazioneVisite()) {
            throw new IllegalStateException("Non è il giorno corretto per generare le visite.");
        }
    
        // Mese target: il prossimo
        LocalDate meseTarget = oggi.plusMonths(1).withDayOfMonth(1);
    
        List<TipoVisita> tipiVisita = new ArrayList<>();
        luoghiController.getLuoghi().forEach(l -> tipiVisita.addAll(l.getTipiVisita()));
        List<Visita> nuoveVisite = new ArrayList<>();
    
        LocalDate data = meseTarget;
        while (data.getMonth() == meseTarget.getMonth()) {
    
            // Salta giorni preclusi
            if (calendarioController.isDataPreclusa(data)) {
                data = data.plusDays(1);
                continue;
            }
    
            Set<String> volontariOccupati = new HashSet<>();
            Set<String> tipiVisitatiQuelGiorno = new HashSet<>();
    
            for (TipoVisita tipo : tipiVisita) {
                if (!tipo.getGiorniSettimana().contains(data.getDayOfWeek())) continue;
    
                for (Volontario v : tipo.getVolontari()) {
                    if (!volontariController.isDisponibile(v, data)) continue;
                    if (volontariOccupati.contains(v.getNome())) continue;
    
                    String idTipo = tipo.getTitolo(); // oppure un ID se ne hai uno univoco
                    if (tipiVisitatiQuelGiorno.contains(idTipo)) continue;
    
                    // Crea la visita e registra la guida e il tipo
                    Visita visita = new Visita(tipo, data, v);
                    nuoveVisite.add(visita);
                    volontariOccupati.add(v.getNome());
                    tipiVisitatiQuelGiorno.add(idTipo);
                    break; // vai al prossimo tipo
                }
            }
    
            data = data.plusDays(1);
        }
    
        // Aggiungi le nuove visite alla lista esistente ed effettua il salvataggio
        visite.addAll(nuoveVisite);
        DataManager.scriviDatiVisite(visite);
    
        System.out.println("✅ Generazione completata: " + nuoveVisite.size() + " visite create per " +
                           meseTarget.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN));
    }
    
    
    public List<Visita> getVisite() {
        return visite;
    }


    public List<Visita> leggiDatiVisite() throws IOException, ClassNotFoundException {
        return DataManager.leggiDatiVisite();
    }
}
