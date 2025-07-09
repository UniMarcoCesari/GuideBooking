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
        // Initialize all controllers
        PersistentDataManager dataManager = new PersistentDataManager();
        AuthController authController = new AuthController(dataManager);
        TipiVisitaController tipiVisitaController = new TipiVisitaController(dataManager);
        VolontariController volontarioController = new VolontariController(authController, dataManager);
        LuoghiController luoghiController = new LuoghiController(dataManager);
        CalendarioController calendarioController = new CalendarioController(dataManager);
        VisiteController visiteController = new VisiteController(calendarioController, luoghiController, volontarioController,dataManager);
        
        
        // Initialize main controller with all the controllers
        MainController mainController = new MainController(dataManager,authController, tipiVisitaController, visiteController, volontarioController, luoghiController, calendarioController);

        // check credenziali existence
        if (authController.getCredenziali().isEmpty()) {
            // If no credentials exist, create default credentials
            authController.creaCredenzialiDefault();
        }

        CorpoDati corpoDati = dataManager.caricaCorpoDati(costants.Costants.file_corpo);

        
        if (corpoDati == null || !corpoDati.getIsAlreadyStart()) {
            SwingUtilities.invokeLater(() -> mainController.startFirstTimeSetup());
        }
        else {
            // Start normal application
            SwingUtilities.invokeLater(() -> mainController.showLoginPanel());
        }
    }
}
