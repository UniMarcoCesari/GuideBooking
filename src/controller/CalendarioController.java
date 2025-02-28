package controller;

import model.Calendario;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Set;

public class CalendarioController {
    private final Calendario calendario;
    private final Set<LocalDate> giorniFestivi;

    public CalendarioController(Set<LocalDate> giorniFestivi) {
        this.calendario = new Calendario();
        this.giorniFestivi = giorniFestivi;
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


}
