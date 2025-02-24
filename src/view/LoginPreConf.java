package view;

import controller.AuthController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPreConf extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JButton loginButton;

    private final String username;

    public LoginPreConf(String username) {
        this.username = username;

        setTitle("Inizializzazione Configuratore");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode("#FFFAFA"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Primo Accesso Configuratore");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        usernameField.setFocusable(false);
        usernameField.setText(username);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Conferma Password:"), gbc);
        confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        loginButton = new JButton("Salva Credenziali");
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaCredenziali();
            }
        });

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        add(panel);
    }

    private void salvaCredenziali() {
        String password = new String(passwordField.getPassword());
        String confermaPassword = new String(confirmPasswordField.getPassword());

        if (password.isEmpty() || confermaPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "I campi password non possono essere vuoti!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confermaPassword)) {
            JOptionPane.showMessageDialog(this, "Le password non coincidono!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "✅ Credenziali salvate con successo!");
        AuthController.setNewPassConfiguratore(username, password);
        dispose();  // Chiude la finestra
        new CreazioneCorpoDati().setVisible(true); //Creazione corpo dati
    }
}
