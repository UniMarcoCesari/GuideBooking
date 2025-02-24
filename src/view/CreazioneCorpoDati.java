package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CreazioneCorpoDati extends JFrame {
    private final JTextField ambitoField;
    private final JTextField maxPersoneField;
    private final JLabel ambitoErrorLabel;
    private final JLabel maxPersoneErrorLabel;
    private final Border defaultBorder;

    public CreazioneCorpoDati() {
        setTitle("Configurazione Iniziale - Sistema Gestione Visite");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Layout principale
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Pannello per il testo introduttivo
        JPanel introPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel introLabel = new JLabel("Ciao configuratore! Inserisci il corpo dei dati!", SwingConstants.CENTER);
        introLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        introPanel.add(introLabel);

        // Form per i dati
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dati di Configurazione"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Ambito Territoriale
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Ambito Territoriale:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        ambitoField = new JTextField(30);
        formPanel.add(ambitoField, gbc);

        // Label per errore ambito
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 10, 10);
        ambitoErrorLabel = new JLabel(" ");
        ambitoErrorLabel.setForeground(Color.RED);
        formPanel.add(ambitoErrorLabel, gbc);

        // Campo Numero massimo persone
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.insets = new Insets(15, 10, 5, 10);
        formPanel.add(new JLabel("Numero max persone per visita:"), gbc);

        gbc.gridx = 1;
        maxPersoneField = new JTextField(10);
        formPanel.add(maxPersoneField, gbc);

        // Label per errore numero persone
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 10, 10);
        maxPersoneErrorLabel = new JLabel(" ");
        maxPersoneErrorLabel.setForeground(Color.RED);
        formPanel.add(maxPersoneErrorLabel, gbc);

        defaultBorder = ambitoField.getBorder();

        // Pannello pulsante in basso a destra
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confermaButton = new JButton("Avanti");
        confermaButton.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(confermaButton);

        // Aggiunta componenti al pannello principale
        mainPanel.add(introPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Event Listeners
        confermaButton.addActionListener(e -> confermaDati());

        ambitoField.addCaretListener(e -> resetFieldError(ambitoField, ambitoErrorLabel));
        maxPersoneField.addCaretListener(e -> resetFieldError(maxPersoneField, maxPersoneErrorLabel));

        setVisible(true);
    }

    private void resetFieldError(JTextField field, JLabel errorLabel) {
        field.setBorder(defaultBorder);
        errorLabel.setText(" ");
    }

    private void showFieldError(JTextField field, JLabel errorLabel, String errorMessage) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        errorLabel.setText(errorMessage);
    }

    private void confermaDati() {
        boolean hasErrors = false;
        String ambito = ambitoField.getText().trim();
        String maxPersoneText = maxPersoneField.getText().trim();

        resetFieldError(ambitoField, ambitoErrorLabel);
        resetFieldError(maxPersoneField, maxPersoneErrorLabel);

        if (ambito.isEmpty()) {
            showFieldError(ambitoField, ambitoErrorLabel, "L'ambito territoriale non può essere vuoto!");
            hasErrors = true;
        }

        try {
            int maxPersone = Integer.parseInt(maxPersoneText);
            if (maxPersone <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showFieldError(maxPersoneField, maxPersoneErrorLabel, "Inserisci un numero valido maggiore di zero!");
            hasErrors = true;
        }

        if (!hasErrors) {
            JOptionPane.showMessageDialog(this, "Dati salvati con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LuoghiFrame().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreazioneCorpoDati::new);
    }
}
