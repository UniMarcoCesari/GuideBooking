package view;

import controller.LuoghiController;
import controller.TipiVisitaController;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CreazioneCorpoDati extends JFrame {
    private final JTextField ambitoField;
    private final JTextField maxPersoneField;
    private final JLabel ambitoErrorLabel;
    private final JLabel maxPersoneErrorLabel;
    private final Border defaultBorder;

    private final LuoghiController luoghiController = new LuoghiController();
    private final TipiVisitaController tipiVisitaController = new TipiVisitaController();


    public CreazioneCorpoDati() {
        setTitle("Configurazione Iniziale - Sistema Gestione Visite");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Layout principale
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Titolo
        JLabel titleLabel = new JLabel("Creazione Corpo Dati", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // Creo un pannello centrale che conterrà sia il form che i bottoni di gestione
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 20)); // 2 righe, 1 colonna, con spazio verticale

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

        // Label per errore ambito (inizialmente vuota)
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 10, 10);
        ambitoErrorLabel = new JLabel(" ");
        ambitoErrorLabel.setForeground(Color.RED);
        ambitoErrorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        formPanel.add(ambitoErrorLabel, gbc);

        // Campo Numero massimo persone
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(15, 10, 5, 10);
        formPanel.add(new JLabel("Numero max persone per visita:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maxPersoneField = new JTextField(10);
        formPanel.add(maxPersoneField, gbc);

        // Label per errore numero persone (inizialmente vuota)
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 10, 10);
        maxPersoneErrorLabel = new JLabel(" ");
        maxPersoneErrorLabel.setForeground(Color.RED);
        maxPersoneErrorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        formPanel.add(maxPersoneErrorLabel, gbc);

        // Salva il bordo predefinito per ripristinarlo quando non ci sono errori
        defaultBorder = ambitoField.getBorder();

        // Pannello Gestione Luoghi e Visite
        JPanel gestionePanel = new JPanel();
        gestionePanel.setBorder(BorderFactory.createTitledBorder("Gestione Luoghi e Visite"));
        gestionePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30));

        JButton manageLocationsButton = new JButton("Gestione Luoghi 🏢");
        manageLocationsButton.setPreferredSize(new Dimension(200, 40));
        JButton manageVisitsButton = new JButton("Gestione Visite 📋");
        manageVisitsButton.setPreferredSize(new Dimension(200, 40));

        gestionePanel.add(manageLocationsButton);
        gestionePanel.add(manageVisitsButton);

        // Aggiungo form e gestione al pannello centrale
        centerPanel.add(formPanel);
        centerPanel.add(gestionePanel);

        // Pannello pulsanti (Conferma e Logout)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        JButton confermaButton = new JButton("Conferma e Procedi ✅");
        confermaButton.setPreferredSize(new Dimension(200, 40));
        JButton logoutButton = new JButton("Logout 🚪");
        logoutButton.setPreferredSize(new Dimension(200, 40));

        buttonPanel.add(confermaButton);
        buttonPanel.add(logoutButton);

        // Aggiunta componenti al pannello principale
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event Listeners
        confermaButton.addActionListener(e -> confermaDati());
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        manageLocationsButton.addActionListener(e -> {
            dispose();
            new LuoghiFrame(luoghiController).setVisible(true);
        });

        manageVisitsButton.addActionListener(e -> {
            dispose();
            new VisiteFrame(tipiVisitaController).setVisible(true);
        });

        // Aggiungi listener per rimuovere bordi rossi quando l'utente inizia a correggere
        ambitoField.addCaretListener(e -> {
            resetFieldError(ambitoField, ambitoErrorLabel);
        });

        maxPersoneField.addCaretListener(e -> {
            resetFieldError(maxPersoneField, maxPersoneErrorLabel);
        });

        setVisible(true);
    }

    // Metodo per ripristinare l'aspetto normale di un campo dopo un errore
    private void resetFieldError(JTextField field, JLabel errorLabel) {
        field.setBorder(defaultBorder);
        errorLabel.setText(" ");
    }

    // Metodo per evidenziare un campo con errore
    private void showFieldError(JTextField field, JLabel errorLabel, String errorMessage) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        errorLabel.setText(errorMessage);
    }

    // Metodo per validare i dati e passare alla schermata generale
    private void confermaDati() {
        boolean hasErrors = false;
        String ambito = ambitoField.getText().trim();
        String maxPersoneText = maxPersoneField.getText().trim();

        // Reset iniziale degli errori
        resetFieldError(ambitoField, ambitoErrorLabel);
        resetFieldError(maxPersoneField, maxPersoneErrorLabel);

        // Verifica ambito territoriale
        if (ambito.isEmpty()) {
            showFieldError(ambitoField, ambitoErrorLabel, "L'ambito territoriale non può essere vuoto!");
            hasErrors = true;
        }

        // Verifica numero massimo persone
        try {
            int maxPersone = Integer.parseInt(maxPersoneText);
            if (maxPersone <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showFieldError(maxPersoneField, maxPersoneErrorLabel, "Inserisci un numero valido maggiore di zero!");
            hasErrors = true;
        }

        // Verifica luoghi
        if (luoghiController.getLuoghi().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Nessun luoghi existente!", "Errore", JOptionPane.ERROR_MESSAGE);
            hasErrors = true;
        }

        // Se non ci sono errori, procedi
        if (!hasErrors) {
            JOptionPane.showMessageDialog(this, "Dati salvati con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreazioneCorpoDati::new);
    }
}