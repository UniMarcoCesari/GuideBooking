package model;

import java.util.ArrayList;

public class CorpoDati
{
    private String ambito;
    private String maxPersone;
    private ArrayList<Luogo> luogi;

    CorpoDati(String ambito, String maxPersone, ArrayList<Luogo> luogi)
    {
        this.ambito = ambito;
        this.maxPersone = maxPersone;
        this.luogi = luogi;
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

    public ArrayList<Luogo> getLuogi() {
        return luogi;
    }

    public void setLuogi(ArrayList<Luogo> luogi) {
        this.luogi = luogi;
    }
}
