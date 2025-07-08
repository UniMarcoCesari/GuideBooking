package view.login;

import controller.AuthController;
import controller.CalendarioController;
import controller.LuoghiController;
import controller.TipiVisitaController;
import controller.VisiteController;
import controller.VolontariController;
import costants.Costants;
import model.CorpoDati;
import service.PersistentDataManager;

public class MainController {
    private final PersistentDataManager dataManager;
    private final AuthController authController;
    private final TipiVisitaController tipiVisitaController;
    private final VisiteController visiteController;
    private final VolontariController volontarioController;
    private final LuoghiController luoghiController;
    private final CalendarioController calendarioController;


    public MainController(PersistentDataManager dataManager, AuthController authController, TipiVisitaController tipiVisitaController,
            VisiteController visiteController, VolontariController volontarioController,
            LuoghiController luoghiController, CalendarioController calendarioController) {
        this.dataManager = dataManager;
        this.authController = authController;
        this.tipiVisitaController = tipiVisitaController;
        this.visiteController = visiteController;
        this.volontarioController = volontarioController;
        this.luoghiController = luoghiController;
        this.calendarioController = calendarioController;
    }

    public AuthController getAuthController() {
        return authController;
    }

    public TipiVisitaController getTipiVisitaController() {
        return tipiVisitaController;
    }

    public VisiteController getVisiteController() {
        return visiteController;
    }

    public VolontariController getVolontarioController() {
        return volontarioController;
    }

    public LuoghiController getLuoghiController() {
        return luoghiController;
    }

    public CalendarioController getCalendarioController() {
        return calendarioController;
    }

    public CorpoDati getCorpoDati() {
        return dataManager.caricaCorpoDati(Costants.file_corpo);
    }

    public void salvaCorpoDati(CorpoDati corpoDati) {
        dataManager.salvaCorpoDati(corpoDati, Costants.file_corpo);
    }

}
