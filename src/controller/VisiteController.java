package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import model.Luogo;
import model.TipoVisita;
import model.Visita;
import model.Visita.STATO_VISITA;
import model.Volontario;
import service.DataManager;

public class VisiteController {

    private final CalendarioController calendarioController;
    private final TipiVisitaController tipiVisitaController; // Potrebbe non essere più necessario se otteniamo i tipi dai luoghi
    private final VolontariController volontariController;
    private final LuoghiController luoghiController;

    private List<Visita> visite;

    // Helper record per rappresentare un intervallo di tempo
    private record TimeSlot(LocalTime start, LocalTime end) {
        boolean overlaps(TimeSlot other) {
            boolean result = this.start.isBefore(other.end) && other.start.isBefore(this.end);
            System.out.println("[" + this.start + "," + this.end + ") overlaps with [" + other.start + "," + other.end + ")? " + result);
            return result;
        }
    }

    public VisiteController(CalendarioController calendarioController,
            LuoghiController luoghiController, TipiVisitaController tipiVisitaController, VolontariController volontariController) {
        this.calendarioController = calendarioController;
        this.tipiVisitaController = tipiVisitaController; // Mantenuto per ora, potrebbe essere rimosso/modificato
        this.luoghiController = luoghiController;
        this.volontariController = volontariController;
        this.visite = DataManager.leggiDatiVisite();
        if (this.visite == null) {
            this.visite = new ArrayList<>(); // Inizializza se il file non esiste o è vuoto
        }
    }

    public void generaVisite() {
        LocalDate oggi = calendarioController.getDatacDateCorrenteLocalDate();
        System.out.println("Inizio generazione visite per il mese di " + oggi.plusMonths(1).getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN));

        if (!calendarioController.isGiornoDiGenerazioneVisite()) {
            // In un'applicazione reale, gestire l'eccezione o loggare un avviso
             System.out.println("AVVISO: Non è il giorno corretto per generare le visite, ma procedo comunque per test.");
            // throw new IllegalStateException("Non è il giorno corretto per generare le visite.");
        }

        // Mese target: il prossimo
        LocalDate meseTarget = oggi.plusMonths(1).withDayOfMonth(1);
        List<Visita> nuoveVisite = new ArrayList<>();
        List<Luogo> luoghi = luoghiController.getLuoghi();


        // Check if visits already exist for the target month
        boolean visiteEsistenti = false;
        for (Visita v : visite) {
            if (v.getData().getMonth() == meseTarget.getMonth() && 
                v.getData().getYear() == meseTarget.getYear()) {
                visiteEsistenti = true;
                break;
            }
        }
        
        if (visiteEsistenti) {
            String message = "ATTENZIONE: Visite già generate per il mese di " + 
                            meseTarget.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN) + 
                            ". Operazione annullata.";
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Attenzione", JOptionPane.WARNING_MESSAGE);
            return; // Exit method if visits already exist
        }

        LocalDate data = meseTarget;
        while (data.getMonth() == meseTarget.getMonth()) {
            System.out.println("\n--- Data: " + data.format(DateTimeFormatter.ofPattern("d MMMM yyyy")) + " (" + data.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALIAN) + ") ---");

            // Salta giorni preclusi
            if (calendarioController.isDataPreclusa(data)) {
                System.out.println("Salto giorno precluso: " + data.format(DateTimeFormatter.ISO_DATE));
                data = data.plusDays(1);
                continue;
            }

            // Strutture dati per tracciare le risorse occupate *in questo giorno*
            Set<String> volontariOccupatiDelGiorno = new HashSet<>();
            Map<String, List<TimeSlot>> orariOccupatiPerLuogo = new HashMap<>(); // Key: Nome Luogo

            // Itera sui luoghi
            for (Luogo luogo : luoghi) {
                String nomeLuogo = luogo.getNome();
                orariOccupatiPerLuogo.putIfAbsent(nomeLuogo, new ArrayList<>()); // Inizializza la lista per il luogo se non esiste
                List<TimeSlot> orariOccupatiLuogoCorrente = orariOccupatiPerLuogo.get(nomeLuogo);

                System.out.println("  Luogo: " + nomeLuogo);

                // Itera sui tipi di visita associati a questo luogo
                for (TipoVisita tipo : luogo.getTipiVisita()) {
                    System.out.println("    Tipo Visita: " + tipo.getTitolo());

                    // 1. Controlla se il tipo di visita è attivo in questa data e giorno della settimana
                    System.out.print("      Giorni settimana tipo visita: ");
                    tipo.getGiorniSettimana().forEach(giorno -> System.out.print(giorno.getDisplayName(TextStyle.FULL, Locale.ITALIAN) + " "));
                    System.out.println();
                    if (!tipo.getGiorniSettimana().contains(data.getDayOfWeek())) {
                        System.out.println("      Giorno settimana non valido.");
                        continue;
                    }
                    if (data.isBefore(tipo.getDataInizio()) || data.isAfter(tipo.getDataFine())) {
                         System.out.println("      Data fuori intervallo validità tipo visita.");
                        continue;
                    }

                    // 2. Calcola l'intervallo di tempo per questa visita
                    LocalTime oraInizio = tipo.getOraInizio();
                    LocalTime oraFine = oraInizio.plusMinutes(tipo.getDurataMinuti());
                    TimeSlot potenzialeSlot = new TimeSlot(oraInizio, oraFine);
                    System.out.println("      Orario potenziale: " + oraInizio + " - " + oraFine);


                    // 3. Controlla sovrapposizioni con altre visite *nello stesso luogo* in questo giorno
                    boolean sovrapposizione = false;
                    for (TimeSlot slotOccupato : orariOccupatiLuogoCorrente) {
                        if (potenzialeSlot.overlaps(slotOccupato)) {
                            sovrapposizione = true;
                            System.out.println("      ATTENZIONE: Sovrapposizione rilevata con slot " + slotOccupato.start + " - " + slotOccupato.end);
                            break;
                        }
                    }
                    if (sovrapposizione) {
                        System.out.println("      Impossibile programmare per sovrapposizione oraria.");
                        continue; // Passa al prossimo tipo di visita per questo luogo
                    }

                    // 4. Trova un volontario disponibile per questo tipo e data, che non sia già occupato oggi
                    Volontario volontarioAssegnato = null;
                    for (Volontario v : tipo.getVolontari()) {
                         System.out.println("        Tentativo volontario: " + v.getNome());
                        if (!volontariController.isDisponibile(v, data)) {
                             System.out.println("          Non disponibile per calendario.");
                            continue;
                        }
                        if (volontariOccupatiDelGiorno.contains(v.getNome())) {
                             System.out.println("          Già occupato oggi in altra visita.");
                            continue;
                        }

                        // Se arriviamo qui, il volontario è valido
                        volontarioAssegnato = v;
                        System.out.println("        Volontario trovato: " + v.getNome());
                        break; // Trovato un volontario, esci dal loop dei volontari per questo tipo
                    }

                    // 5. Se è stato trovato un volontario e non ci sono sovrapposizioni, crea la visita
                    if (volontarioAssegnato != null) {
                        Visita visita = new Visita(tipo, data, volontarioAssegnato);
                        nuoveVisite.add(visita);

                        // Aggiorna le strutture di tracciamento per questo giorno
                        volontariOccupatiDelGiorno.add(volontarioAssegnato.getNome());
                        orariOccupatiLuogoCorrente.add(potenzialeSlot); // Aggiungi lo slot appena occupato

                        System.out.println("      ✅ Visita creata: " + visita.getTipo().getTitolo() + " con " + volontarioAssegnato.getNome() + " alle " + visita.getTipo().getOraInizio());
                        // Non fare break qui, potremmo voler programmare *altri tipi* di visita nello stesso luogo oggi
                    } else {
                         System.out.println("      Nessun volontario disponibile trovato per questo tipo/data/orario.");
                    }
                } // Fine loop TipiVisita per questo Luogo
            } // Fine loop Luoghi per questo Giorno

            data = data.plusDays(1); // Passa al giorno successivo
        } // Fine loop Giorni del Mese

        // Aggiungi le nuove visite alla lista esistente ed effettua il salvataggio
        // Potrebbe essere utile rimuovere prima le visite esistenti per il mese target per evitare duplicati se si rigenera
        // Esempio (da valutare attentamente):
        // visite.removeIf(v -> v.getData().getMonth() == meseTarget.getMonth() && v.getData().getYear() == meseTarget.getYear());
        visite.addAll(nuoveVisite);
        DataManager.scriviDatiVisite(visite);
        

        System.out.println("\n✅ Generazione completata: " + nuoveVisite.size() + " nuove visite create per " +
                           meseTarget.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN) + " " + meseTarget.getYear());
    }


    public List<Visita> getVisite(LocalDate meseTarget) {
        List<Visita> visiteFiltrate = new ArrayList<>();
        for (Visita v : visite) {
            if (v.getData().getMonthValue() == meseTarget.getMonthValue() && v.getData().getYear() == meseTarget.getYear()) {
                visiteFiltrate.add(v);
            }
        }
        return visiteFiltrate;
    }

    public List<Visita> getVisiteDisponibili(LocalDate meseTarget) {
        List<Visita> visiteFiltrate = new ArrayList<>();
        for (Visita v : visite) {
            if (v.getData().getMonthValue() == meseTarget.getMonthValue() && v.getData().getYear() == meseTarget.getYear()) {
                if (v.getStato() == STATO_VISITA.PROPOSTA || v.getStato() == STATO_VISITA.CONFERMATA) {
                    
                }
            }
        }
        return visiteFiltrate;
    }

    public void eliminaVisiteConVolontario(Volontario volontario) {
        List<Visita> visiteDaRimuovere = new ArrayList<>();
        for (Visita v : visite) {
            if (v.getStato() == STATO_VISITA.PROPOSTA || v.getStato() == STATO_VISITA.COMPLETA) {
                if (v.getTipo().getVolontari().stream().anyMatch(vol -> vol.getNome().equals(volontario.getNome()))) {
                    visiteDaRimuovere.add(v);
                }
            }
        }
        System.out.println("Eliminate " + visiteDaRimuovere.size() + " visite con volontario " + volontario.getNome());
        visite.removeAll(visiteDaRimuovere);
        DataManager.scriviDatiVisite(visite);
    }

    public void eliminaVisiteConTipoVisita(TipoVisita tipoVisita) {
        List<Visita> visiteDaRimuovere = new ArrayList<>();
        for (Visita v : visite) {
            if (v.getStato() == STATO_VISITA.PROPOSTA || v.getStato() == STATO_VISITA.COMPLETA) {
                if (v.getTipo().getTitolo().equals(tipoVisita.getTitolo())) {
                    visiteDaRimuovere.add(v);
                }
            }
        }
        System.out.println("Eliminate " + visiteDaRimuovere.size() + " visite con tipo " + tipoVisita.getTitolo());
        visite.removeAll(visiteDaRimuovere);
        DataManager.scriviDatiVisite(visite);
    }

    // public void eliminaVisiteConLuogo(Luogo luogo) {
    //     List<Visita> visiteDaRimuovere = new ArrayList<>();
    //     for (Visita v : visite) {
    //         if (v.getStato() == STATO_VISITA.PROPOSTA || v.getStato() == STATO_VISITA.COMPLETA) {
    //             if (v.get().getTitolo().equals(luogo.getNome())) {
    //                 visiteDaRimuovere.add(v);
    //             }
    //         }
    //     }
    //     System.out.println("Eliminate " + visiteDaRimuovere.size() + " visite nel luogo " + luogo.getNome());
    //     visite.removeAll(visiteDaRimuovere);
    //     DataManager.scriviDatiVisite(visite);
    // }

    public void confermaVisitaOdierna(LocalDate data) {
        for (Visita visita : visite) {
            if (visita.getData().equals(data) && visita.getStato() == STATO_VISITA.PROPOSTA) {
                System.out.println("Visita confermata: " + visita.getTipo().getTitolo() + " con " + visita.getGuidaAssegnata().getNome() + " alle " + visita.getTipo().getOraInizio());
                visita.setStato(STATO_VISITA.CONFERMATA);
            }
        }
        DataManager.scriviDatiVisite(visite);
    }


    public void controlla3Giorni(LocalDate data) 
    {
        for (Visita visita : visite) {
            if (visita.getData().equals(data.plusDays(3)) && visita.getStato() == STATO_VISITA.PROPOSTA) {
                System.out.println("Manca 3 giorni alla visita: " + visita.getTipo().getTitolo() + " con " + visita.getGuidaAssegnata().getNome() + " alle " + visita.getTipo().getOraInizio());
                //TODO controlla numero partecipanti effettivi e min e max e in caso 
                //positivo --> CONFERMATA
                //negativo --> CANCELLATA
            }
        }
    }
}
