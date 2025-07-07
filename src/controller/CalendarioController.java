package controller;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import costants.Costants;
import model.Calendario;
import model.CorpoDati;
import service.PersistentDataManager;

public class CalendarioController {
    private PersistentDataManager dataManager;
    private final Calendario calendario;
    private final List<LocalDate> datePrecluse;
    private static final String CALENDARIO_FILE = "src/data/calendario.dat";

    public CalendarioController(PersistentDataManager dataManager) {
        this.dataManager = dataManager;
        Calendario loadedCalendario = dataManager.caricaDati(CALENDARIO_FILE);
        this.calendario = (Calendario) (loadedCalendario != null ? loadedCalendario : new Calendario());
        this.datePrecluse = this.dataManager.caricaDatePrecluse(Costants.file_date);
    }

    private void salvaDati() {
        dataManager.salvaDati(calendario, CALENDARIO_FILE);
        dataManager.salvaDati(datePrecluse, Costants.file_date);
    }

    public String getDataCorrente() {
        return calendario.getDataString();
    }

    public LocalDate getDatacDateCorrenteLocalDate() {
        return calendario.getData();
    }

    public void avantiUnGiorno() {
        calendario.avantiUnGiorno();
        salvaDati();
    }

    public void indietroUnGiorno() {
        calendario.indietroUnGiorno();
        salvaDati();
    }

    public String getNomeMese() {
        return calendario.getData().getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
    }

    public String getNomeMesePiu(int mesi) {
        return calendario.getData().plusMonths(mesi).getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
    }


    // questo metodo restituisce il PRIMO mese UTILE su cui configuratore puo fare modifiche
    public LocalDate getNomeMesePrimoCheSiPuoModificare() {
        LocalDate data = calendario.getData();
        if (data.getDayOfMonth() > 15 && !isGiornoCancellato(data)) {
            CorpoDati corpoDati = PersistentDataManager.caricaCorpoDati(Costants.file_corpo);
            if (!corpoDati.getIsAlreadyStart()) {
                corpoDati.setIsAlreadyStart(true);
                PersistentDataManager.salvaCorpoDati(corpoDati, Costants.file_corpo);
            }
            data = data.plusMonths(1);
        }
        return data.withDayOfMonth(1);
    }

    public boolean isButtonLocked() {
        CorpoDati corpoDati = PersistentDataManager.caricaCorpoDati(Costants.file_corpo);
        return !corpoDati.getIsAlreadyStart();
    }


    private boolean isGiornoCancellato(LocalDate data)
    {
        for (LocalDate dataPreclusa : datePrecluse)
        {
            if (data.equals(dataPreclusa))
            {
                return true;  //data festiva
            }
        }
        return false;  //data non festiva
    }

    // Metodo per ottenere le date precluse di un mese e anno specifici
    public List<LocalDate> getDatePrecluse(LocalDate data) {
        List<LocalDate> dateFiltrate = new ArrayList<>();
        for (LocalDate dataPreclusa : datePrecluse) {
            if (dataPreclusa.getMonthValue() == data.getMonthValue() && dataPreclusa.getYear() == data.getYear()) {
                dateFiltrate.add(dataPreclusa);
            }
        }
        return dateFiltrate;
    }

    // Metodo per aggiungere una data alla lista delle date precluse
    public void aggiungiDataPreclusa(LocalDate data) {
        datePrecluse.add(data);
        salvaDati();
    }

    // Metodo per rimuovere una data dalla lista delle date precluse
    public void rimuoviDataPreclusa(LocalDate data) {
        boolean removed = datePrecluse.remove(data);
        if (removed) { // Only save if a date was actually removed
            salvaDati();
            System.out.println("Saved precluded dates after removing: " + data); // Optional log
        }
    }

    // Metodo per controllare se una data è preclusa
    public boolean isDataPreclusa(LocalDate data) {
        return datePrecluse.contains(data);
    }
    
    public  boolean isGiornoDiGenerazioneVisite() {
        LocalDate oggi = getDatacDateCorrenteLocalDate();
        if (oggi.getDayOfMonth() < 16 || isDataPreclusa(oggi)) return false;
    
        LocalDate primoFeriale = oggi.withDayOfMonth(16);
        while (primoFeriale.getDayOfWeek().getValue() > 5 || isDataPreclusa(primoFeriale)) {
            primoFeriale = primoFeriale.plusDays(1);
        }
    
        return oggi.equals(primoFeriale);
    }

    public boolean isFaseModificabile() {
        return getDatacDateCorrenteLocalDate().getDayOfMonth() <= 15;
    }
    
}
