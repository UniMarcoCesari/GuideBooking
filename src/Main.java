import javax.swing.SwingUtilities;

import controller.AuthController;
import controller.TipiVisitaController;
import controller.VisiteController;
import controller.VolontariController;
import model.CorpoDati;
import service.PersistentDataManager;
import controller.LuoghiController;
import controller.CalendarioController;
import view.login.MainController;

public class Main {
    public static void main(String[] args) {
        // Inizializza tutti i controller
        PersistentDataManager dataManager = new PersistentDataManager();
        AuthController authController = new AuthController(dataManager);
        TipiVisitaController tipiVisitaController = new TipiVisitaController(dataManager);
        VolontariController volontarioController = new VolontariController(authController, dataManager);
        LuoghiController luoghiController = new LuoghiController(dataManager);
        CalendarioController calendarioController = new CalendarioController(dataManager);
        VisiteController visiteController = new VisiteController(calendarioController, luoghiController, volontarioController,dataManager);
        
        
        // Inizializza il controller principale con tutti i controller
        MainController mainController = new MainController(dataManager,authController, tipiVisitaController, visiteController, volontarioController, luoghiController, calendarioController);

        // Verifica esistenza delle credenziali
        if (authController.getCredenziali().isEmpty()) {
            // Se non esistono credenziali, crea credenziali di default
            authController.creaCredenzialiDefault();
        }

        CorpoDati corpoDati = dataManager.caricaOggetto(costants.Costants.file_corpo);

        
        if (corpoDati == null || !corpoDati.getIsAlreadyStart()) {
            // Avvia setup iniziale
            SwingUtilities.invokeLater(() -> mainController.startFirstTimeSetup());
        }
        else {
            // Avvia applicazione normale
            SwingUtilities.invokeLater(() -> mainController.showLoginPanel());
        }
    }
}

