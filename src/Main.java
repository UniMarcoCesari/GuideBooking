import javax.swing.SwingUtilities;

import controller.AuthController;
import controller.TipiVisitaController;
import controller.VisiteController;
import controller.VolontariController;
import service.PersistentDataManager;
import controller.LuoghiController;
import controller.CalendarioController;
import controller.LoginController;
import view.login.LoginPanel;
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

        SwingUtilities.invokeLater(() -> new LoginPanel(mainController).setVisible(true));

    }
}

