import java.awt.CardLayout;

import javax.swing.*;

import controller.AuthController;
import controller.LoginController;
import service.PersistentDataManager;
import view.login.LoginPanel;

public class MainFrame extends JFrame {
    private CardLayout layout = new CardLayout();
    private JPanel container = new JPanel(layout);

    private PersistentDataManager dataManager;
    private AuthController authController;
    private LoginController loginController;

    public MainFrame() {
        setTitle("Applicazione");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        dataManager = new PersistentDataManager();
        authController = new AuthController(dataManager);


        // Inizializza schermate
        LoginPanel loginPanel = new LoginPanel(this, authController,loginController);
        // PannelloConfiguratore configuratorePanel = new PannelloConfiguratore();
        // // Aggiungi altri pannelli...

        // Aggiungi tutti i pannelli con nomi univoci
        container.add(loginPanel, "login");
        // container.add(configuratorePanel, "configuratore");

        add(container);
        setVisible(true);
    }

    public void showScreen(String screenName) {
        layout.show(container, screenName);
    }
}
