package view.fruitore;

import controller.AuthController;
import costants.Costants;
import enumerations.Ruolo;
import model.CorpoDati;
import service.PersistentDataManager;
import view.configuratore.PannelloConfiguratore;
import view.corpoDati.CorpoDatiFase1;
import view.login.LoginPanel;
import view.login.MainController;
import view.volontario.PannelloVolontario;

import javax.swing.*;
import java.awt.*;

public class RegistrazioneFruitore extends JFrame {
    private final JTextField usernameField = new JTextField("fruitore", 15);
    private final JPasswordField passwordField = new JPasswordField("t", 15);
    private final JButton loginButton;
    private final AuthController authController;
    private final MainController mainController;

    public RegistrazioneFruitore(MainController mainController) {
        this.mainController = mainController;
        this.authController = mainController.getAuthController();

        setTitle("Registrazione-Fruitore");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        // Pannello principale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Header (Logo + Titolo)
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(true);
        headerPanel.setBackground(Costants.BACKGROUND_COLOR);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS)); // BoxLayout orizzontale per il layout

        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Allinea l'header al centro

        JLabel logoLabel = new JLabel(" "); // Logo
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));

        // Pannello per il titolo
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Costants.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Sistema di Gestione Visite");
        titleLabel.setFont(Costants.TITLE_FONT);
        titleLabel.setForeground(Costants.ACCENT_COLOR);

        JLabel subtitleLabel = new JLabel("Pannello di Controllo Amministratore");
        subtitleLabel.setFont(Costants.BUTTON_FONT);
        subtitleLabel.setForeground(Costants.BORDER_COLOR);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);

        // Aggiungi logo e titolo al pannello
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Spazio tra logo e titolo
        headerPanel.add(titlePanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form di login (centrato)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Costants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Pulsante di login (centrato)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Costants.BACKGROUND_COLOR);

        loginButton = Costants.createMenuButton("Registrati");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(loginButton.getPreferredSize()); // Adatta la larghezza al testo

        loginButton.addActionListener(_ -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            registrati(username, password);
        });

        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void registrati(String username, String password) {
        boolean verifica = authController.creaNuovaCredenziale(username, password, Ruolo.FRUITORE);
        if(!verifica)
        {
            //credenziali gia esistono
            JOptionPane.showMessageDialog(this, "Username gia esistente", "Errore", JOptionPane.ERROR_MESSAGE);

        }
        else
        {
            mainController.showLoginPanel();
        }

    }

}
