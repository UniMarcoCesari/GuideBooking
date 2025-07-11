package factory;

import javax.swing.JFrame;
import controller.LoginController;
import enumerations.Ruolo;
import model.CorpoDati;
import view.configuratore.*;
import view.corpoDati.CorpoDatiFase1;
import view.corpoDati.CorpoDatiFase2;
import view.fruitore.PannelloFruitore;
import view.fruitore.RegistrazioneFruitore;
import view.login.LoginPanel;
import view.login.MainController;
import view.login.NewPasswordFrame;
import view.volontario.GestisciDisponibilitaFrame;
import view.volontario.PannelloVolontario;
import view.volontario.VisualizzaMieVisiteFrame;
import view.volontario.VisualizzaTipiVisitaVolontarioFrame;

public class ViewFactory {
    
    private final MainController mainController;

    public ViewFactory(MainController mainController) {
        this.mainController = mainController;
    }

    public JFrame createLoginPanel() {
        LoginPanel loginView = new LoginPanel(mainController);
        LoginController loginController = new LoginController(loginView, mainController);
        loginView.setController(loginController);
        return loginView;
    }

    public JFrame createPannelloConfiguratore() {
        return new PannelloConfiguratore(mainController);
    }

    public JFrame createPannelloVolontario(String username) {
        return new PannelloVolontario(username, mainController);
    }
    
    public JFrame createPannelloFruitore(String username) {
        return new PannelloFruitore(username, mainController);
    }

    public JFrame createNewPasswordFrame(String username, Ruolo ruolo) {
        return new NewPasswordFrame(username, ruolo, mainController);
    }

    public JFrame createRegistrazioneFruitore() {
        return new RegistrazioneFruitore(mainController);
    }
    
    public JFrame createGestioneVisite() {
        return new GestioneVisite(mainController);
    }

    public JFrame createDatePrecluse() {
        return new DatePrecluseSezione(mainController);
    }

    public JFrame createListaLuoghi() {
        return new ListaLuoghi(mainController);
    }
    
    public JFrame createListaTipiVisita() {
        return new ListaTipiVisita(mainController);
    }
    
    public JFrame createListaVolontari() {
        return new ListaVolontari(mainController);
    }

    public JFrame createNumMax() {
        return new NumMax(mainController);
    }

    public JFrame createNuovoTipoVisita(ListaTipiVisita parent) {
        return new NuovoTipoVisita(parent, mainController);
    }

    public JFrame createCorpoDatiFase1() {
        return new CorpoDatiFase1(mainController);
    }
    
    public JFrame createCorpoDatiFase2(CorpoDati corpoDati) {
        return new CorpoDatiFase2(corpoDati, mainController);
    }

    public JFrame createVisualizzaTipiVisitaVolontario(String username) {
        return new VisualizzaTipiVisitaVolontarioFrame(username, mainController);
    }

    public JFrame createGestisciDisponibilita(String username) {
        return new GestisciDisponibilitaFrame(username, mainController);
    }
    
    public JFrame createVisualizzaMieVisite(String username) {
        return new VisualizzaMieVisiteFrame(username, mainController);
    }
}