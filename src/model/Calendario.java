package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class Calendario {
    private LocalDate data;
    private Set<LocalDate> datePrecluse; // Date in cui non si possono fare visite

    public Calendario() {
        this.data = LocalDate.now();
    }

    public LocalDate getData() {
        return data;
    }

    public String getDataString() {
        return data.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    }

    public Set<LocalDate> getDatePrecluse() {
        return datePrecluse;
    }

    public void setDatePrecluse(Set<LocalDate> datePrecluse) {
        this.datePrecluse = datePrecluse;
    }

    public void avantiUnGiorno() {
        data = data.plusDays(1);
    }

    public void indietroUnGiorno() {
        data = data.minusDays(1);
    }
}
