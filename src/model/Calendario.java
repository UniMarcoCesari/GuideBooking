package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Locale;

public class Calendario
{
    private LocalDate data;

    public Calendario(){
        data = LocalDate.now();
    }

    public void avantiUnGiorno()
    {
        data = data.plus(1, ChronoUnit.DAYS);
        System.out.println(data.getMonth().name());
    }

    public void indietroUnGiorno()
    {
        data = data.minus(1, ChronoUnit.DAYS);
    }

    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ITALIAN);
        return date.format(formatter);
    }

    public String getDataString() {
        return formatDate(data);
    }

    public void avantiUnMese()
    {
        data = data.plus(1, ChronoUnit.MONTHS);
    }

    public void indietroUnMese()
    {
        data = data.minus(1, ChronoUnit.MONTHS);
    }

    public String getNomeMese()
    {
        return data.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
    }

    public String getNomeMesePiu(int i)
    {

        return data.getMonth().plus(i).getDisplayName(TextStyle.FULL, Locale.ITALIAN);
    }






}
