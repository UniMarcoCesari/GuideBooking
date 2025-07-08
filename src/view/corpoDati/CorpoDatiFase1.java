package view.corpoDati;

import costants.Costants;
import model.CorpoDati;
import view.login.MainController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CorpoDatiFase1 extends JFrame {
    private final JTextField ambitoField;
    private final JTextField maxPersoneField;
    private final JLabel ambitoErrorLabel;
    private final JLabel maxPersoneErrorLabel;
    private final Border defaultBorder;
    MainController mainController;

    public CorpoDatiFase1(MainController mainController) {
        this.mainController = mainController;
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        // Layout principale
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));


        // Crea l'intestazione
        JPanel headerPanel = Costants.createHeaderPanel("Creazione corpo dei dati - Fase 1");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form per i dati
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(39, 39, 39, 39));  // Solo spazio

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 5, 10);
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


        // Pannello footer +  bottone avanti
        JPanel footerPanel = Costants.createFooterPanel("");
        JButton confermaButton = new JButton("Avanti");
        confermaButton.setPreferredSize(new Dimension(200, 40));
        footerPanel.add(confermaButton, BorderLayout.WEST);


        // Aggiunta componenti al pannello principale
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Event Listeners
        confermaButton.addActionListener(e -> {
            confermaDati();
        });

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
            CorpoDati corpoDati = new CorpoDati(ambito, maxPersoneText);
            mainController.salvaCorpoDati(corpoDati);
            mainController.showCorpoDatiFase2(corpoDati);
        }
    }

}
