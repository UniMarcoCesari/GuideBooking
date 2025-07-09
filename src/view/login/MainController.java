package view.login;

import controller.AuthController;
import controller.CalendarioController;
import controller.LuoghiController;
import controller.TipiVisitaController;
import controller.VisiteController;
import controller.VolontariController;
import controller.LoginController;
import costants.Costants;
import model.CorpoDati;
import service.PersistentDataManager;

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

    // --- Router Methods ---

    public void showLoginPanel() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        LoginPanel loginView = new LoginPanel(this);
        LoginController loginController = new LoginController(loginView, this); // Pass the view and the main controller
        loginView.setController(loginController); // Link the controller to the view
        currentFrame = loginView;
        currentFrame.setVisible(true);
    }

    public void showPannelloConfiguratore() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.configuratore.PannelloConfiguratore(this);
        currentFrame.setVisible(true);
    }

    // Add more router methods as needed
    public void showPannelloVolontario(String username) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.volontario.PannelloVolontario(username, this);
        currentFrame.setVisible(true);
    }

    public void showPannelloFruitore(String username) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.fruitore.PannelloFruitore(username, this);
        currentFrame.setVisible(true);
    }

    public void showNewPasswordFrame(String username, enumerations.Ruolo ruolo) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new NewPasswordFrame(username, ruolo, this);
        currentFrame.setVisible(true);
    }

    public void showRegistrazioneFruitore() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.fruitore.RegistrazioneFruitore(this);
        currentFrame.setVisible(true);
    }

    public void showGestioneVisite() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.configuratore.GestioneVisite(this);
        currentFrame.setVisible(true);
    }

    public void showDatePrecluse() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.configuratore.DatePrecluseSezione(this);
        currentFrame.setVisible(true);
    }

    public void showListaLuoghi() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.configuratore.ListaLuoghi(this);
        currentFrame.setVisible(true);
    }

    public void showListaTipiVisita() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.configuratore.ListaTipiVisita(this);
        currentFrame.setVisible(true);
    }

    public void showListaVolontari() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.configuratore.ListaVolontari(this);
        currentFrame.setVisible(true);
    }

    public void showNumMax() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.configuratore.NumMax(this);
        currentFrame.setVisible(true);
    }

    public void showNuovoTipoVisita(view.configuratore.ListaTipiVisita parent) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.configuratore.NuovoTipoVisita(parent, this);
        currentFrame.setVisible(true);
    }

    public void showCorpoDatiFase2(CorpoDati corpoDati) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.corpoDati.CorpoDatiFase2(corpoDati, this);
        currentFrame.setVisible(true);
    }

    public void startFirstTimeSetup() {
        showCorpoDatiFase1();
    }

    public void showCorpoDatiFase1() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.corpoDati.CorpoDatiFase1(this);
        currentFrame.setVisible(true);
    }

    public void showVisualizzaTipiVisitaVolontario(String username) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.volontario.VisualizzaTipiVisitaVolontarioFrame(username, this);
        currentFrame.setVisible(true);
    }

    public void showGestisciDisponibilita(String username) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.volontario.GestisciDisponibilitaFrame(username, this);
        currentFrame.setVisible(true);
    }

    public void showVisualizzaMieVisite(String username) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new view.volontario.VisualizzaMieVisiteFrame(username, this);
        currentFrame.setVisible(true);
    }
}
