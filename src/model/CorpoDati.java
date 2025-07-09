package model;

import java.io.Serializable;

public class CorpoDati implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String ambito;
    private String maxPersone;
    private Boolean isAlreadyStart = false;

    public CorpoDati(String ambito, String maxPersone) {
        this.ambito = ambito;
        this.maxPersone = maxPersone;
    }

    public CorpoDati() {
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getMaxPersone() {
        return maxPersone;
    }

    public void setMaxPersone(String maxPersone) {
        this.maxPersone = maxPersone;
    }


    public Boolean getIsAlreadyStart() {
        return isAlreadyStart;
    }
    public void setIsAlreadyStart(Boolean isAlreadyStart) {
        this.isAlreadyStart = isAlreadyStart;
    }

}
