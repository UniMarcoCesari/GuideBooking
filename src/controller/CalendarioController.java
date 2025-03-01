package controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import model.Calendario;

public class CalendarioController {
    private final Calendario calendario;
    private final Set<LocalDate> giorniFestivi;
    private final Set<LocalDate> datePrecluse;

    public CalendarioController(Set<LocalDate> giorniFestivi) {
        this.calendario = new Calendario();
        this.giorniFestivi = giorniFestivi;
        this.datePrecluse = new HashSet<>();
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
        if (calendario.getData().getDayOfMonth()>=15)   //  se dopo il 15
        {
            if (!isGiornoCancellato(calendario.getData()))  //   e se data non festiva
            {
                return calendario.getData().getMonth().plus(1);  // mese successivo
            }
        }
        return calendario.getData().getMonth(); // te stesso
    }

    private boolean isGiornoCancellato(LocalDate data)
    {
        for (LocalDate dataFestiva : giorniFestivi)
        {
            if (data.equals(dataFestiva))
            {
                return true;  //data festiva
            }
        }
        return false;  //data non festiva
    }

    // Metodo per ottenere le date precluse di un mese e anno specifici
    public Set<LocalDate> getDatePrecluse(int mese, int anno) {
        Set<LocalDate> dateFiltrate = new HashSet<>();
        for (LocalDate data : datePrecluse) {
            if (data.getMonthValue() == mese && data.getYear() == anno) {
                dateFiltrate.add(data);
            }
        }
        return dateFiltrate;
    }

    // Metodo per aggiungere una data alla lista delle date precluse
    public void aggiungiDataPreclusa(LocalDate data) {
        datePrecluse.add(data);
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
