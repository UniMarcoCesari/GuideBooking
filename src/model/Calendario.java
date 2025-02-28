package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Calendario {
    private LocalDate data;

    public Calendario() {
        this.data = LocalDate.now();
    }

    public LocalDate getData() {
        return data;
    }

    public String getDataString() {
        return data.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    }

    public void avantiUnGiorno() {
        data = data.plusDays(1);
    }

    public void indietroUnGiorno() {
        data = data.minusDays(1);
    }
}
