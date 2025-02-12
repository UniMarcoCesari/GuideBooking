package controller;
import model.Luogo;
import service.DataManager;
import java.util.ArrayList;
import java.util.List;
import costants.*;

public class AppController {
    private List<Luogo> luoghi;

    public AppController() {
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
}
