package controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

import costants.Costants;
import model.Calendario;
import model.CorpoDati;
import service.DataManager;

public class CalendarioController {
    private final Calendario calendario;
    private final List<LocalDate> datePrecluse;

    public CalendarioController() {
        this.calendario = new Calendario();
        this.datePrecluse = DataManager.caricaDatePrecluse(Costants.file_date);
    }

    public String getDataCorrente() {
        return calendario.getDataString();
    }

    public void avantiUnGiorno() {
        calendario.avantiUnGiorno();
    }

    public void indietroUnGiorno() {
        calendario.indietroUnGiorno();
    }

    public String getNomeMese() {
        return calendario.getData().getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
    }

    public String getNomeMesePiu(int mesi) {
        return calendario.getData().plusMonths(mesi).getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
    }


    // questo metodo restituisce il PRIMO mese UTILE su cui configuratore puo fare modifiche
    public Month getNomeMesePrimoCheSiPuoModificare()
    {
        if (calendario.getData().getDayOfMonth()>15)   //  se dopo il 15
        {
            if (!isGiornoCancellato(calendario.getData()))  //   e se data non festiva
            {
                CorpoDati corpoDati = DataManager.caricaCorpoDati(Costants.file_corpo);
                if(corpoDati.getIsAlreadyStart() == false)
                {
                    corpoDati.setIsAlreadyStart(true);
                    DataManager.salvaCorpoDati(corpoDati, Costants.file_corpo);
                }
                return calendario.getData().getMonth().plus(1);  // mese successivo
            }
        }
        return calendario.getData().getMonth(); // te stesso
    }

    public boolean isButtonLocked() {
        CorpoDati corpoDati = DataManager.caricaCorpoDati(Costants.file_corpo);
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
    public List<LocalDate> getDatePrecluse(int mese) {
        List<LocalDate> dateFiltrate = new ArrayList<>();
        for (LocalDate data : datePrecluse) {
            if (data.getMonthValue() == mese ) {
                dateFiltrate.add(data);
            }
        }
        return dateFiltrate;
    }

    // Metodo per aggiungere una data alla lista delle date precluse
    public void aggiungiDataPreclusa(LocalDate data) {
        datePrecluse.add(data);
        DataManager.salvaDatePrecluse(datePrecluse, Costants.file_date);
    }

    // Metodo per rimuovere una data dalla lista delle date precluse
    public void rimuoviDataPreclusa(LocalDate data) {
        datePrecluse.remove(data);
    }

    // Metodo per controllare se una data è preclusa
    public boolean isDataPreclusa(LocalDate data) {
        return datePrecluse.contains(data);
    }
}
