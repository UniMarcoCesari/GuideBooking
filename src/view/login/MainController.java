package view.login;

import controller.AuthController;
import controller.CalendarioController;
import controller.LuoghiController;
import controller.TipiVisitaController;
import controller.VisiteController;
import controller.VolontariController;
import costants.Costants;
import enumerations.Ruolo;
import model.CorpoDati;
import service.PersistentDataManager;
import view.configuratore.ListaTipiVisita;
import factory.ViewFactory;

import javax.swing.JFrame;

public class MainController {
    private JFrame currentFrame;
    private final PersistentDataManager dataManager;
    private final AuthController authController;
    private final TipiVisitaController tipiVisitaController;
    private final VisiteController visiteController;
    private final VolontariController volontarioController;
    private final LuoghiController luoghiController;
    private final CalendarioController calendarioController;
    
    private final ViewFactory viewFactory; // <-- NUOVA DIPENDENZA

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

        // Il MainController crea la sua factory, passandole un riferimento
        // a se stesso (this) affinché la factory possa iniettarlo nelle viste.
        this.viewFactory = new ViewFactory(this);
    }

    // --- Metodi getter per i servizi (rimangono invariati) ---
    public AuthController getAuthController() { return authController; }
    public TipiVisitaController getTipiVisitaController() { return tipiVisitaController; }
    public VisiteController getVisiteController() { return visiteController; }
    public VolontariController getVolontarioController() { return volontarioController; }
    public LuoghiController getLuoghiController() { return luoghiController; }
    public CalendarioController getCalendarioController() { return calendarioController; }
    public CorpoDati getCorpoDati() { return dataManager.caricaOggetto(Costants.file_corpo); }
    public void salvaCorpoDati(CorpoDati corpoDati) { dataManager.salvaOggetto(corpoDati, Costants.file_corpo); }


    // --- Metodi Router (ora delegano la creazione alla factory) ---

    private void showFrame(JFrame frame) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = frame;
        currentFrame.setVisible(true);
    }

    public void showLoginPanel() {
        showFrame(viewFactory.createLoginPanel());
    }

    public void showPannelloConfiguratore() {
        showFrame(viewFactory.createPannelloConfiguratore());
    }

    public void showPannelloVolontario(String username) {
        showFrame(viewFactory.createPannelloVolontario(username));
    }

    public void showPannelloFruitore(String username) {
        showFrame(viewFactory.createPannelloFruitore(username));
    }

    public void showNewPasswordFrame(String username, Ruolo ruolo) {
        showFrame(viewFactory.createNewPasswordFrame(username, ruolo));
    }

    public void showRegistrazioneFruitore() {
        showFrame(viewFactory.createRegistrazioneFruitore());
    }

    public void showGestioneVisite() {
        showFrame(viewFactory.createGestioneVisite());
    }

    public void showDatePrecluse() {
        showFrame(viewFactory.createDatePrecluse());
    }

    public void showListaLuoghi() {
        showFrame(viewFactory.createListaLuoghi());
    }

    public void showListaTipiVisita() {
        showFrame(viewFactory.createListaTipiVisita());
    }

    public void showListaVolontari() {
        showFrame(viewFactory.createListaVolontari());
    }

    public void showNumMax() {
        showFrame(viewFactory.createNumMax());
    }

    public void showNuovoTipoVisita(ListaTipiVisita parent) {
        showFrame(viewFactory.createNuovoTipoVisita(parent));
    }

    public void showCorpoDatiFase1() {
        showFrame(viewFactory.createCorpoDatiFase1());
    }

    public void showCorpoDatiFase2(CorpoDati corpoDati) {
        showFrame(viewFactory.createCorpoDatiFase2(corpoDati));
    }

    public void startFirstTimeSetup() {
        showCorpoDatiFase1();
    }

    public void showVisualizzaTipiVisitaVolontario(String username) {
        showFrame(viewFactory.createVisualizzaTipiVisitaVolontario(username));
    }

    public void showGestisciDisponibilita(String username) {
        showFrame(viewFactory.createGestisciDisponibilita(username));
    }

    public void showVisualizzaMieVisite(String username) {
        showFrame(viewFactory.createVisualizzaMieVisite(username));
    }
}