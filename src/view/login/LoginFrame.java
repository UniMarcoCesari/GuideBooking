package view.login;

import controller.AuthController;
import costants.Costants;
import model.CorpoDati;
import service.DataManager;
import view.configuratore.PannelloConfiguratore;
import view.corpoDati.CorpoDatiFase1;
import view.corpoDati.CorpoDatiFase2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

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

        // Header (Logo + Titolo)
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(true);
        headerPanel.setBackground(Costants.BACKGROUND_COLOR);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS)); // BoxLayout orizzontale per il layout

        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Allinea l'header al centro

        JLabel logoLabel = new JLabel("🏥");
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

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Pulsante di login (centrato)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Costants.BACKGROUND_COLOR);

        loginButton = Costants.createMenuButton("Accedi");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(loginButton.getPreferredSize()); // Adatta la larghezza al testo

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            authenticate(username, password);
        });

        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void authenticate(String username, String password) {
        int verifica = AuthController.checkCredentials(username, password);

        if (verifica == -1) {  //errore
            JOptionPane.showMessageDialog(this, "Credenziali errate", "Errore", JOptionPane.ERROR_MESSAGE);
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
            new NewPasswordConf(username).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
