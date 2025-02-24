package view;

import controller.TipiVisitaController;
import model.TipoVisita;
import costants.Costants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class NuovaVisitaFrame extends JFrame {
    private final JTextField titoloField, descrizioneField, puntoIncontroField;
    private final JSpinner dataInizioSpinner, dataFineSpinner, oraInizioSpinner;
    private JSpinner durataSpinner, minPartecipantiSpinner, maxPartecipantiSpinner;
    private JCheckBox bigliettoCheckbox;

    private TipiVisitaController controller;
    private VisiteFrame parentFrame;

    public NuovaVisitaFrame(VisiteFrame parent) {
        setTitle("Nuova Visita");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        this.parentFrame = parent;
        this.controller = new TipiVisitaController();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Pannello superiore con titolo e tasto indietro
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = Costants.createMenuButton("🔙 Indietro", "");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(e -> dispose()); // Torna a VisiteFrame

        JLabel titleLabel = new JLabel("Nuova Visita", SwingConstants.CENTER);
        titleLabel.setFont(Costants.TITLE_FONT);
        titleLabel.setForeground(Costants.ACCENT_COLOR);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Pannello per il form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Costants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Titolo:"), gbc);
        titoloField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(titoloField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Descrizione:"), gbc);
        descrizioneField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(descrizioneField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Punto di Incontro:"), gbc);
        puntoIncontroField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(puntoIncontroField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Data Inizio:"), gbc);
        dataInizioSpinner = new JSpinner(new SpinnerDateModel());
        dataInizioSpinner.setEditor(new JSpinner.DateEditor(dataInizioSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        formPanel.add(dataInizioSpinner, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Data Fine:"), gbc);
        dataFineSpinner = new JSpinner(new SpinnerDateModel());
        dataFineSpinner.setEditor(new JSpinner.DateEditor(dataFineSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        formPanel.add(dataFineSpinner, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Ora Inizio:"), gbc);
        oraInizioSpinner = new JSpinner(new SpinnerDateModel());
        oraInizioSpinner.setEditor(new JSpinner.DateEditor(oraInizioSpinner, "HH:mm"));
        gbc.gridx = 1;
        formPanel.add(oraInizioSpinner, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Durata (minuti):"), gbc);
        durataSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 300, 5));
        gbc.gridx = 1;
        formPanel.add(durataSpinner, gbc);
        row++;

        // Pulsante per salvare
        JButton salvaButton = Costants.createMenuButton("Salva Visita", "💾");
        salvaButton.addActionListener(e -> salvaVisita());
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(salvaButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }



    private void salvaVisita() {
        String titolo = titoloField.getText().trim();
        String descrizione = descrizioneField.getText().trim();
        String puntoIncontro = puntoIncontroField.getText().trim();

        // Controllo che tutti i campi siano compilati
        if (titolo.isEmpty() || descrizione.isEmpty() || puntoIncontro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Errore: Tutti i campi devono essere compilati!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate dataInizio = convertToLocalDate(dataInizioSpinner.getValue());
        LocalDate dataFine = convertToLocalDate(dataFineSpinner.getValue());

        // Controllo validità delle date
        if (dataInizio.isAfter(dataFine)) {
            JOptionPane.showMessageDialog(this, "Errore: La data di inizio non può essere dopo la data di fine!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalTime oraInizio = convertToLocalTime(oraInizioSpinner.getValue());

        // Controllo se il titolo esiste già
        if (controller.titoloEsiste(titolo)) {
            JOptionPane.showMessageDialog(this, "Errore: Esiste già una visita con questo titolo!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoVisita nuovaVisita = new TipoVisita(
                titolo,
                descrizione,
                puntoIncontro,
                dataInizio,
                dataFine,
                new ArrayList<>(),
                oraInizio,
                (int) durataSpinner.getValue(),
                false,
                1,
                10
        );

        controller.aggiungiVisita(nuovaVisita);
        controller.salvaDati();

        parentFrame.aggiornaLista(); // Aggiorna immediatamente la lista delle visite
        dispose();
        JOptionPane.showMessageDialog(parentFrame, "Visita aggiunta con successo!");

    }

    private LocalDate convertToLocalDate(Object spinnerValue) {
        return ((Date) spinnerValue).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalTime convertToLocalTime(Object spinnerValue) {
        return ((Date) spinnerValue).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }
}
