package controller;
import model.Luogo;
import service.DataManager;
import java.util.ArrayList;
import java.util.List;

public class AppController {
    private List<Luogo> luoghi;

    public AppController() {
        luoghi = (List<Luogo>) DataManager.caricaDatiLuogo("luoghi.dat");
        if (luoghi == null) {
            luoghi = new ArrayList<>();
        }
    }

    public void aggiungiLuogo(Luogo luogo) {
        luoghi.add(luogo);
        DataManager.salvaDatiLuogo(luoghi, "luoghi.dat");
    }

    public List<Luogo> getLuoghi() {
        return luoghi;
    }
}
