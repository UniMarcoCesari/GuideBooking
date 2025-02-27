package view;

import controller.TipiVisitaController;
import controller.VolontariController;
import costants.Costants;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import model.TipoVisita;
import model.Volontario;
import view.corpoDati.CorpoDatiFase2;

public class NuovoTipoVisitaFrame extends JFrame {
    private final JTextField titoloField, descrizioneField, puntoIncontroField;
    private final JSpinner dataInizioSpinner, dataFineSpinner, oraInizioSpinner;
    private JSpinner durataSpinner, minPartecipantiSpinner, maxPartecipantiSpinner;
    private JCheckBox bigliettoCheckbox;

    // Sostituiamo il ComboBox con una JList e un modello per selezioni multiple
    private JList<String> volontariList;
    private DefaultListModel<String> volontariListModel;

    private TipiVisitaController tipiVisitaController;
    private VolontariController volontariController;

    private final CorpoDatiFase2 parent;

    public NuovoTipoVisitaFrame(CorpoDatiFase2 parent, TipiVisitaController tipoVisitaController) {
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setTitle("Nuovo Tipo Visita");

        this.parent = parent;
        this.tipiVisitaController = tipoVisitaController;
        this.volontariController = new VolontariController();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Pannello superiore con titolo e tasto indietro
        JPanel headerPanel = Costants.createHeaderPanel("Nuovo tipo visita");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Pannello per il form - usando GridBagLayout per un controllo migliore
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        formPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Creiamo un pannello scorrevole per contenere il form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Costants.BACKGROUND_COLOR);

        // Configurazione GridBagConstraints
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.anchor = GridBagConstraints.EAST;
        labelGbc.fill = GridBagConstraints.NONE;
        labelGbc.insets = new Insets(8, 10, 8, 10);
        labelGbc.gridx = 0;

        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.fill = GridBagConstraints.HORIZONTAL;
        fieldGbc.weightx = 1.0;
        fieldGbc.insets = new Insets(8, 10, 8, 10);
        fieldGbc.gridx = 1;

        // Sezione 1: Informazioni generali
        addSectionLabel(formPanel, "Informazioni Generali", 0);

        // Titolo
        labelGbc.gridy = 1;
        formPanel.add(createLabel("Titolo:"), labelGbc);
        titoloField = new JTextField(30);
        fieldGbc.gridy = 1;
        formPanel.add(titoloField, fieldGbc);

        // Descrizione
        labelGbc.gridy = 2;
        formPanel.add(createLabel("Descrizione:"), labelGbc);
        descrizioneField = new JTextField(30);
        fieldGbc.gridy = 2;
        formPanel.add(descrizioneField, fieldGbc);

        // Punto di Incontro
        labelGbc.gridy = 3;
        formPanel.add(createLabel("Punto di Incontro:"), labelGbc);
        puntoIncontroField = new JTextField(30);
        fieldGbc.gridy = 3;
        formPanel.add(puntoIncontroField, fieldGbc);

        // Sezione 2: Date e orari
        addSectionLabel(formPanel, "Date e Orari", 4);

        // Data Inizio
        labelGbc.gridy = 5;
        formPanel.add(createLabel("Data Inizio:"), labelGbc);
        SpinnerDateModel dataInizioModel = new SpinnerDateModel();
        dataInizioSpinner = new JSpinner(dataInizioModel);
        dataInizioSpinner.setEditor(new JSpinner.DateEditor(dataInizioSpinner, "yyyy-MM-dd"));
        Dimension spinnerSize = new Dimension(150, 25);
        dataInizioSpinner.setPreferredSize(spinnerSize);
        fieldGbc.gridy = 5;
        formPanel.add(dataInizioSpinner, fieldGbc);

        // Data Fine
        labelGbc.gridy = 6;
        formPanel.add(createLabel("Data Fine:"), labelGbc);
        SpinnerDateModel dataFineModel = new SpinnerDateModel();
        dataFineSpinner = new JSpinner(dataFineModel);
        dataFineSpinner.setEditor(new JSpinner.DateEditor(dataFineSpinner, "yyyy-MM-dd"));
        dataFineSpinner.setPreferredSize(spinnerSize);
        fieldGbc.gridy = 6;
        formPanel.add(dataFineSpinner, fieldGbc);

        // Ora Inizio
        labelGbc.gridy = 7;
        formPanel.add(createLabel("Ora Inizio:"), labelGbc);
        SpinnerDateModel oraInizioModel = new SpinnerDateModel();
        oraInizioSpinner = new JSpinner(oraInizioModel);
        oraInizioSpinner.setEditor(new JSpinner.DateEditor(oraInizioSpinner, "HH:mm"));
        oraInizioSpinner.setPreferredSize(spinnerSize);
        fieldGbc.gridy = 7;
        formPanel.add(oraInizioSpinner, fieldGbc);

        // Durata
        labelGbc.gridy = 8;
        formPanel.add(createLabel("Durata (minuti):"), labelGbc);
        durataSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 300, 5));
        durataSpinner.setPreferredSize(spinnerSize);
        fieldGbc.gridy = 8;
        formPanel.add(durataSpinner, fieldGbc);

        // Sezione 3: Partecipanti
        addSectionLabel(formPanel, "Partecipanti", 9);

        // Min Partecipanti
        labelGbc.gridy = 10;
        formPanel.add(createLabel("Minimo Partecipanti:"), labelGbc);
        minPartecipantiSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        minPartecipantiSpinner.setPreferredSize(spinnerSize);
        fieldGbc.gridy = 10;
        formPanel.add(minPartecipantiSpinner, fieldGbc);

        // Max Partecipanti
        labelGbc.gridy = 11;
        formPanel.add(createLabel("Massimo Partecipanti:"), labelGbc);
        maxPartecipantiSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        maxPartecipantiSpinner.setPreferredSize(spinnerSize);
        fieldGbc.gridy = 11;
        formPanel.add(maxPartecipantiSpinner, fieldGbc);

        // Biglietto
        labelGbc.gridy = 12;
        formPanel.add(createLabel("Richiede Biglietto:"), labelGbc);
        bigliettoCheckbox = new JCheckBox();
        fieldGbc.gridy = 12;
        formPanel.add(bigliettoCheckbox, fieldGbc);

        // Sezione 4: Volontari
        addSectionLabel(formPanel, "Gestione Volontari", 13);

        // Lista di volontari con selezione multipla
        labelGbc.gridy = 14;
        formPanel.add(createLabel("Seleziona Volontari:"), labelGbc);

        // Inizializza la lista con selezione multipla
        volontariListModel = new DefaultListModel<>();
        volontariList = new JList<>(volontariListModel);
        volontariList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Aggiungi un bordo alla lista
        JScrollPane volontariScrollPane = new JScrollPane(volontariList);
        volontariScrollPane.setPreferredSize(new Dimension(250, 100));
        volontariScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        fieldGbc.gridy = 14;
        formPanel.add(volontariScrollPane, fieldGbc);

        // Aggiorna la lista dei volontari
        aggiornaListaVolontari();

        // Pulsante aggiungi volontario
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Costants.BACKGROUND_COLOR);
        JButton aggiungiVolontarioButton = new JButton("Aggiungi Nuovo Volontario");
        aggiungiVolontarioButton.addActionListener(e -> creaNuovoVolontario());
        buttonPanel.add(aggiungiVolontarioButton);

        labelGbc.gridy = 15;
        labelGbc.gridx = 0;
        labelGbc.gridwidth = 2;
        formPanel.add(buttonPanel, labelGbc);

        //footer e annulla
        JButton salvaButton = Costants.createSimpleButton("Salva Visita");
        salvaButton.addActionListener(e -> salvaVisita());

        JButton annullaButton = Costants.createSimpleButton("Annulla");
        annullaButton.addActionListener(e -> chiudiEmandaIndietro());


        JPanel footerPanel = Costants.createFooterPanel("");
        footerPanel.add(salvaButton, BorderLayout.CENTER);
        footerPanel.add(annullaButton, BorderLayout.EAST);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Dialog", Font.BOLD, 12));
        return label;
    }

    private void addSectionLabel(JPanel panel, String text, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 5, 5);

        JLabel sectionLabel = new JLabel(text);
        sectionLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        sectionLabel.setForeground(new Color(0, 102, 204));
        panel.add(sectionLabel, gbc);

    }

    // Metodo che aggiorna la lista dei volontari nella JList
    public void aggiornaListaVolontari() {
        volontariListModel.clear(); // Rimuove gli elementi esistenti
        ArrayList<Volontario> volontari = volontariController.getListaVolontari();
        for (Volontario volontario : volontari) {
            volontariListModel.addElement(volontario.getNome());
        }
    }

    // Getter per accedere alla lista volontari da altre classi
    public JList<String> getVolontariList() {
        return volontariList;
    }

    private void creaNuovoVolontario() {
        new VolontariFrame(this, volontariController).setVisible(true);
        aggiornaListaVolontari();
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

        // Verifica che sia stato selezionato almeno un volontario
        if (volontariList.getSelectedIndices().length == 0) {
            JOptionPane.showMessageDialog(this, "Errore: Seleziona almeno un volontario!", "Errore", JOptionPane.ERROR_MESSAGE);
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
        if (tipiVisitaController.titoloEsiste(titolo)) {
            JOptionPane.showMessageDialog(this, "Errore: Esiste già una visita con questo titolo!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Recupera i volontari selezionati
        List<String> volontariSelezionati = volontariList.getSelectedValuesList();
        // Aggiungi i volontari alla visita
        ArrayList<String> volontari = new ArrayList<>(volontariSelezionati);

        // Messaggio di debug per verificare i volontari selezionati
        StringBuilder sb = new StringBuilder("Volontari selezionati: ");
        for (String v : volontari) {
            sb.append(v).append(", ");
        }
        System.out.println(sb.toString());

        // Ottiene i valori dai nuovi campi
        int minPartecipanti = (int) minPartecipantiSpinner.getValue();
        int maxPartecipanti = (int) maxPartecipantiSpinner.getValue();
        boolean richiedeBiglietto = bigliettoCheckbox.isSelected();

        // Verifica che il minimo non superi il massimo
        if (minPartecipanti > maxPartecipanti) {
            JOptionPane.showMessageDialog(this, "Errore: Il numero minimo di partecipanti non può essere maggiore del massimo!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoVisita nuovaVisita = new TipoVisita(
                titolo,
                descrizione,
                puntoIncontro,
                dataInizio,
                dataFine,
                volontari,
                oraInizio,
                (int) durataSpinner.getValue(),
                richiedeBiglietto,
                minPartecipanti,
                maxPartecipanti
        );

        tipiVisitaController.aggiungiVisita(nuovaVisita);

        // Aggiorna la lista dei tipi di visita nel frame padre
        parent.aggiornaListaTipiVisita();

        // Mostra messaggio di conferma
        JOptionPane.showMessageDialog(this, "Tipo di visita aggiunto con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

        chiudiEmandaIndietro();
    }


    private void chiudiEmandaIndietro() {
        dispose();
    }

    private LocalDate convertToLocalDate(Object spinnerValue) {
        return ((Date) spinnerValue).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalTime convertToLocalTime(Object spinnerValue) {
        return ((Date) spinnerValue).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }
}