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
    private Calendario calendario;
    private static final String CALENDARIO_FILE = "src/data/calendario.dat";

    public CalendarioController(PersistentDataManager dataManager) {
        this.dataManager = dataManager;

        Calendario loadedCalendario = (Calendario) dataManager.caricaOggetto(CALENDARIO_FILE);
        if (loadedCalendario == null) {
            loadedCalendario = new Calendario(); // Se il file non esiste, inizializza un nuovo calendario
        }

        this.calendario = loadedCalendario;
        salvaDati();
    }

    private void salvaDati() {
        dataManager.salvaOggetto(calendario, CALENDARIO_FILE);
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

    // questo metodo restituisce il PRIMO mese UTILE su cui configuratore puo fare
    // modifiche
    public LocalDate getNomeMesePrimoCheSiPuoModificare() {
        LocalDate data = calendario.getData();
        if (data.getDayOfMonth() > 15 && !isGiornoCancellato(data)) {
            CorpoDati corpoDati = dataManager.caricaOggetto(Costants.file_corpo);
            if (!corpoDati.getIsAlreadyStart()) {
                corpoDati.setIsAlreadyStart(true);
                dataManager.salvaOggetto(corpoDati, Costants.file_corpo);
            }
            data = data.plusMonths(1);
        }
        return data.withDayOfMonth(1);
    }

    public boolean isButtonLocked() {
        CorpoDati corpoDati = dataManager.caricaOggetto(Costants.file_corpo);
        return !corpoDati.getIsAlreadyStart();
    }

    private boolean isGiornoCancellato(LocalDate data) {
        for (LocalDate dataPreclusa : calendario.getDatePrecluse()) {
            if (data.equals(dataPreclusa)) {
                return true; // data festiva
            }
        }
        return false; // data non festiva
    }

    // Metodo per ottenere le date precluse di un mese e anno specifici
    public List<LocalDate> getDatePrecluse(LocalDate data) {
        List<LocalDate> dateFiltrate = new ArrayList<>();
        for (LocalDate dataPreclusa : calendario.getDatePrecluse()) {
            if (dataPreclusa.getMonthValue() == data.getMonthValue() && dataPreclusa.getYear() == data.getYear()) {
                dateFiltrate.add(dataPreclusa);
            }
        }
        return dateFiltrate;
    }

    // Metodo per aggiungere una data alla lista delle date precluse
    public void aggiungiDataPreclusa(LocalDate data) {
        calendario.addDatePreclusa(data);
        salvaDati();
    }

    // Metodo per rimuovere una data dalla lista delle date precluse
    public void rimuoviDataPreclusa(LocalDate data) {
        boolean removed = calendario.removeDatePreclusa(data);
        if (removed) { 
            salvaDati();
        }
    }

    // Metodo per controllare se una data è preclusa
    public boolean isDataPreclusa(LocalDate data) {
        return calendario.getDatePrecluse().contains(data);
    }

    public boolean isGiornoDiGenerazioneVisite() {
        LocalDate oggi = getDatacDateCorrenteLocalDate();
        if (oggi.getDayOfMonth() < 16 || isDataPreclusa(oggi))
            return false;

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
