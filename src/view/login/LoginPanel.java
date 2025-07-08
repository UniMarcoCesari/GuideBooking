package view.login;

import controller.*;

import costants.Costants;
import enumerations.Ruolo;
import view.configuratore.PannelloConfiguratore;
import view.corpoDati.CorpoDatiFase1;
import view.fruitore.PannelloFruitore;
import view.fruitore.RegistrazioneFruitore; 
import view.volontario.PannelloVolontario;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JFrame implements ILoginView{
    private final JTextField usernameField = new JTextField("pre", 15);
    private final JPasswordField passwordField = new JPasswordField("test", 15);
    private final JButton loginButton;
    private final JButton registratiButton; 


    private final AuthController authController;
    private final MainController mainController;

    public LoginPanel(MainController mainController) {
        this.mainController = mainController;

        this.authController = mainController.getAuthController();
        
        //initilize frame 
        setTitle("Login");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        

        // Pannello principale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Header (Logo + Titolo a sinistra, Bottone Registrati a destra)
        JPanel headerPanel = new JPanel(new BorderLayout()); 
        headerPanel.setOpaque(true);
        headerPanel.setBackground(Costants.BACKGROUND_COLOR);
        
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

        // Pannello per il contenuto sinistro dell'header (logo + titolo)
        JPanel headerContentPanel = new JPanel();
        headerContentPanel.setOpaque(false); // Rendi trasparente per usare lo sfondo dell'headerPanel
        headerContentPanel.setLayout(new BoxLayout(headerContentPanel, BoxLayout.X_AXIS));
        headerContentPanel.add(logoLabel);
        headerContentPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        headerContentPanel.add(titlePanel);

        // Pannello per il pulsante Registrati (allineato a destra)
        JPanel registrationButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        registrationButtonPanel.setOpaque(false); // Rendi trasparente
        registratiButton = Costants.createSimpleButton("Registrati");
        registratiButton.addActionListener(_ -> {
            // loginController.vaiARegistrazione();
        });
        registrationButtonPanel.add(registratiButton);

        // Aggiungi i pannelli all'headerPanel principale
        headerPanel.add(headerContentPanel, BorderLayout.CENTER);
        headerPanel.add(registrationButtonPanel, BorderLayout.EAST);


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

        loginButton = Costants.createMenuButton("Accedi");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(loginButton.getPreferredSize()); // Adatta la larghezza al testo

        loginButton.addActionListener(_ -> {
            // loginController.tentaLogin();
        });

        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }


    @Override
    public String getUsername() {
        return usernameField.getText();
    }

    @Override
    public String getPassword() {
        // Ritorna la password come stringa, migliorabile la sicurezza in un futuro
        return new String(passwordField.getPassword());
    }

    @Override
    public void mostraErrore(String titolo, String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, titolo, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void mostraMessaggio(String titolo, String messaggio) {
      JOptionPane.showMessageDialog(this, messaggio, titolo, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void apriPannelloConfiguratore() {
        new PannelloConfiguratore(mainController).setVisible(true);
    }

    @Override
    public void apriCorpoDatiFase1() {
        new CorpoDatiFase1(mainController).setVisible(true);
    }

    @Override
    public void apriPannelloVolontario(String username) {
       new PannelloVolontario(username, mainController).setVisible(true);
    }

    @Override
    public void apriPannelloFruitore(String username) {
        new PannelloFruitore(username,mainController).setVisible(true);
    }

    @Override
    public void apriNewPassword(String username, Ruolo ruolo) {
        new NewPasswordFrame(username, ruolo, mainController).setVisible(true);
    }


    @Override
    public void apriRegistrazioneFruitore() {
        new RegistrazioneFruitore(mainController).setVisible(true);
    }

    @Override
    public void chiudi() {
       this.dispose();
    }

    @Override
    public void pulisciCampi() {
        usernameField.setText("");
        passwordField.setText("");
    }


    
}
