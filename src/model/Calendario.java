package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

public class Calendario implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate data;
    private Set<LocalDate> datePrecluse;

    public Calendario() {
        this.data = LocalDate.now();
        this.datePrecluse = new HashSet<>();
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

    public void addDatePreclusa(LocalDate data) {
        datePrecluse.add(data);
    }

    public boolean removeDatePreclusa(LocalDate data) {
        return datePrecluse.remove(data);
    }
}
