package view.login;

import controller.AuthController;
import costants.Costants;
import model.CorpoDati;
import service.DataManager;
import view.configuratore.PannelloConfiguratore;
import view.corpoDati.CorpoDatiFase1;
import view.fruitore.PannelloFruitore;
import view.fruitore.RegistrazioneFruitore; // Import aggiunto
import view.volontario.PannelloVolontario;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField usernameField = new JTextField("pre", 15);
    private final JPasswordField passwordField = new JPasswordField("test", 15);
    private final JButton loginButton;
    private final JButton registratiButton; // Dichiarazione nuovo pulsante

    public LoginFrame() {
        setTitle("LogIn");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        // Pannello principale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Header (Logo + Titolo a sinistra, Bottone Registrati a destra)
        JPanel headerPanel = new JPanel(new BorderLayout()); // Cambiato layout in BorderLayout
        headerPanel.setOpaque(true);
        headerPanel.setBackground(Costants.BACKGROUND_COLOR);
        // Rimosso headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); non più necessario con BorderLayout

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
            dispose(); // Chiudi la finestra di login
            new RegistrazioneFruitore().setVisible(true); // Apri la finestra di registrazione
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
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            authenticate(username, password);
        });

        // Rimosso creazione e aggiunta bottone Registrati da qui

        buttonPanel.add(loginButton);
        // Rimosso Box.createRigidArea e buttonPanel.add(registratiButton)
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void authenticate(String username, String password) {
        int verifica = AuthController.checkCredentials(username, password);
        if (verifica == -2) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei dati", "Errore", JOptionPane.ERROR_MESSAGE);
        }
        if (verifica == -1) {  //errore
            JOptionPane.showMessageDialog(this, "Username o password errati", "Errore", JOptionPane.ERROR_MESSAGE);
        } else if (verifica == 0) {  //configuratore
            dispose();

            CorpoDati corpoDati = DataManager.caricaCorpoDati(Costants.file_corpo);

            if (corpoDati == null)
            {
                new CorpoDatiFase1().setVisible(true);
            }
            else
            {
                new PannelloConfiguratore().setVisible(true);
            }


        } else if (verifica == 1) { //PRE-configuratore
            dispose();
            new NewPasswordConf(username, "configuratore").setVisible(true);
        } else if (verifica == 2) { //volontario
            dispose();
            new PannelloVolontario(username).setVisible(true); // Pass username
        } else if (verifica == 3) { //preVolontario
            dispose();
            new NewPasswordConf(username, "volontario").setVisible(true);
        } else if (verifica == 4) { //volontario cancellato
            JOptionPane.showMessageDialog(this, "Utente cancellato", "Errore", JOptionPane.ERROR_MESSAGE);
            dispose();
            new LoginFrame().setVisible(true);
        } else if (verifica == 5) { // fruitore
            dispose();
            new PannelloFruitore(username).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
