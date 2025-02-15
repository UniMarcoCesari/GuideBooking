package view;

import controller.AuthController;
import costants.Costants;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginFrame() {
        setTitle("Login Configuratore");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Pannello principale con BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Header (Logo + Titolo)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Costants.BACKGROUND_COLOR);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = new JLabel("🔑");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Accesso Configuratore");
        titleLabel.setFont(Costants.TITLE_FONT);
        titleLabel.setForeground(Costants.ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(titleLabel);

        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Form di login
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Costants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

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

        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Pulsante di login con larghezza automatica
        loginButton = Costants.createMenuButton("Accedi", "🔓");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(loginButton.getPreferredSize()); // Adatta la larghezza al testo

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            authenticate(username, password);
        });

        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(mainPanel);
        pack(); // Adatta automaticamente la finestra
        setVisible(true);
    }

    private void authenticate(String username, String password) {
        int verifica = AuthController.checkCredentials(username, password);

        if (verifica == -1) {
            JOptionPane.showMessageDialog(this, "Credenziali errate", "Errore", JOptionPane.ERROR_MESSAGE);
        } else if (verifica == 0) {
            dispose();
            new PannelloConfiguratore();
        } else if (verifica == 1) {
            dispose();
            new LoginPreConf(username).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
