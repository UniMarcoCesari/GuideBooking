package model;

import java.io.Serializable;

public class Iscrizione implements Serializable {
    private String codicePrenotazione;
    private String usernameFruitore;
    private int numeroPersone;

    public Iscrizione(String codicePrenotazione, String usernameFruitore, int numeroPersone) {
        this.codicePrenotazione = codicePrenotazione;
        this.usernameFruitore = usernameFruitore;
        this.numeroPersone = numeroPersone;
    }

    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    public String getUsernameFruitore() {
        return usernameFruitore;
    }

    public int getNumeroPersone() {
        return numeroPersone;
    }

    @Override
    public String toString() {
        return "Iscrizione{" +
                "codicePrenotazione='" + codicePrenotazione + '\'' +
                ", usernameFruitore='" + usernameFruitore + '\'' +
                ", numeroPersone=" + numeroPersone +
                '}';
    }
}
