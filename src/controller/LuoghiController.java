package controller;
import costants.*;
import java.util.ArrayList;
import java.util.List;
import model.Luogo;
import model.TipoVisita;
import service.DataManager;

public class LuoghiController {
    private List<Luogo> luoghi;

    public LuoghiController() {
        luoghi = DataManager.caricaDati(Costants.file_luoghi);
        if (luoghi == null) {
            luoghi = new ArrayList<>();
            salvaDati();
        }
    }

    public void aggiungiLuogo(Luogo luogo) {
        luoghi.add(luogo);
        salvaDati();
    }

    public List<Luogo> getLuoghi() {
        return luoghi;
    }

    public void salvaDati() {
        DataManager.salvaDati(luoghi, Costants.file_luoghi);
    }

    public Boolean aggiungiTipoVisita(Luogo luogoSelezionato, TipoVisita tipoVisita) {
        for (Luogo luogo : luoghi) {
            if(luogo.equals(luogoSelezionato)) {
                for (TipoVisita tipo : luogo.getTipiVisita()) {
                    if (tipo.equals(tipoVisita)) {
                        return false;
                    }
                }
                luogo.aggiungiTipoVisita(tipoVisita);
                salvaDati();
                return true;
            }
        }
        return false;
    
    }
}
