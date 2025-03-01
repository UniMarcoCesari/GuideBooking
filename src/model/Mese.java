package model;

import java.io.Serializable;

public class Mese implements Serializable {
    private final int numeroMese;
    private final int anno;

    public Mese(Calendario calendario) {
        this.numeroMese = calendario.getData().getMonthValue();
        this.anno = calendario.getData().getYear();
    }

    public int getNumeroMese() {
        return numeroMese;
    }

    public int getAnno() {
        return anno;
    }
}
