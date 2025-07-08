package view.configuratore;

import controller.TipiVisitaController;
import controller.VolontariController;
import costants.Costants;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.*;
import model.TipoVisita;
import model.Volontario;
import view.login.MainController;

public class NuovoTipoVisita extends JFrame {
    private final JTextField titoloField, descrizioneField, puntoIncontroField;
    private final JSpinner dataInizioSpinner, dataFineSpinner, oraInizioSpinner;
    private JSpinner durataSpinner, minPartecipantiSpinner, maxPartecipantiSpinner;
    private JCheckBox bigliettoCheckbox;

    private JList<String> giorniList;
    private DefaultListModel<String> giorniListModel;

    private JList<String> volontariListVolontari;
    private DefaultListModel<String> volontariListModelVolontari;

    private final TipiVisitaController tipiVisitaController;
    private final VolontariController volontariController;

    private final ListaTipiVisita parent;
    private final TipoVisita tipoVisitaDaModificare;
    private final boolean isModifica;

    public NuovoTipoVisita(ListaTipiVisita parent, MainController mainController) {
        this(parent, mainController, null);
    }

    public NuovoTipoVisita(ListaTipiVisita parent, MainController mainController, TipoVisita tipoVisitaDaModificare) {
        this.isModifica = (tipoVisitaDaModificare != null);
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setTitle(isModifica ? "Modifica Tipo Visita" : "Nuovo Tipo Visita");

        this.parent = parent;
        this.tipiVisitaController = mainController.getTipiVisitaController();
        this.volontariController = mainController.getVolontarioController();
        this.tipoVisitaDaModificare = tipoVisitaDaModificare;

        // Pannello principale con layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);

        // Titolo al centro
        JLabel titolo2 = new JLabel(isModifica ? "Modifica Tipo Visita" : "Nuovo Tipo Visita", SwingConstants.CENTER);
        titolo2.setForeground(Color.WHITE);
        titolo2.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titolo2, BorderLayout.CENTER);

        // Bottone Logout a destra
        JButton logoutButton = Costants.creaBottoneLogOut();
        logoutButton.addActionListener(e -> {
            dispose();
            new view.configuratore.PannelloConfiguratore(mainController).setVisible(true);
        });
        
        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRightPanel.setOpaque(false);
        headerRightPanel.add(logoutButton);
        headerPanel.add(headerRightPanel, BorderLayout.EAST);

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

        // Sezione 4: Giorni della settimana
        addSectionLabel(formPanel, "Giorni della settimana", 13);

        // Lista dei giorni della settimana con selezione multipla
        labelGbc.gridy = 14;
        formPanel.add(createLabel("Seleziona giorni:"), labelGbc);

        giorniListModel = new DefaultListModel<>();
        giorniList = new JList<>(giorniListModel);
        giorniList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        giorniListModel.addElement("Lunedì");
        giorniListModel.addElement("Martedì");
        giorniListModel.addElement("Mercoledì");
        giorniListModel.addElement("Giovedì");
        giorniListModel.addElement("Venerdì");
        giorniListModel.addElement("Sabato");
        giorniListModel.addElement("Domenica");

        JScrollPane giorniScrollPane = new JScrollPane(giorniList);
        giorniScrollPane.setPreferredSize(new Dimension(250, 100));
        giorniScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        fieldGbc.gridy = 14;
        formPanel.add(giorniScrollPane, fieldGbc);

        // Sezione 5: Volontari
        addSectionLabel(formPanel, "Gestione Volontari", 15);

        // Lista di volontari con selezione multipla
        labelGbc.gridy = 16;
        formPanel.add(createLabel("Seleziona Volontari:"), labelGbc);

        volontariListModelVolontari = new DefaultListModel<>();
        volontariListVolontari = new JList<>(volontariListModelVolontari);
        volontariListVolontari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane volontariScrollPaneVolontari = new JScrollPane(volontariListVolontari);
        volontariScrollPaneVolontari.setPreferredSize(new Dimension(250, 100));
        volontariScrollPaneVolontari.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        fieldGbc.gridy = 16;
        formPanel.add(volontariScrollPaneVolontari, fieldGbc);

        // Aggiorna la lista dei volontari
        aggiornaListaVolontari();

        // Pulsante aggiungi volontario
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Costants.BACKGROUND_COLOR);

        labelGbc.gridy = 17;
        labelGbc.gridx = 0;
        labelGbc.gridwidth = 2;
        formPanel.add(buttonPanel, labelGbc);

        //footer e annulla
        JButton salvaButton = Costants.createSimpleButton(isModifica ? "Salva Modifiche" : "Salva Visita");
        salvaButton.addActionListener(e -> {
            List<String> giorniSettimanaSelezionati = giorniList.getSelectedValuesList();
            Set<DayOfWeek> giorniSettimana = giorniSettimanaSelezionati.stream()
                    .map(this::convertToDayOfWeek)
                    .collect(Collectors.toSet());

            List<String> volontariSelezionati = volontariListVolontari.getSelectedValuesList();
            ArrayList<Volontario> listaVolontari = new ArrayList<>();
            for (String nomeVolontario : volontariSelezionati) {
                listaVolontari.add(new Volontario(nomeVolontario));
            }

            String titolo = titoloField.getText().trim();
            String descrizione = descrizioneField.getText().trim();
            String puntoIncontro = puntoIncontroField.getText().trim();

            if (titolo.isEmpty() || descrizione.isEmpty() || puntoIncontro.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Errore: Tutti i campi devono essere compilati!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (volontariListVolontari.getSelectedIndices().length == 0) {
                JOptionPane.showMessageDialog(this, "Errore: Seleziona almeno un volontario!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (giorniList.getSelectedIndices().length == 0) {
                JOptionPane.showMessageDialog(this, "Errore: Seleziona almeno un giorno della settimana!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dataInizio = convertToLocalDate(dataInizioSpinner.getValue());
            LocalDate dataFine = convertToLocalDate(dataFineSpinner.getValue());

            if (dataInizio.isAfter(dataFine)) {
                JOptionPane.showMessageDialog(this, "Errore: La data di inizio non può essere dopo la data di fine!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalTime oraInizio = convertToLocalTime(oraInizioSpinner.getValue());

            if (!isModifica && tipiVisitaController.titoloEsiste(titolo)) {
                JOptionPane.showMessageDialog(this, "Errore: Esiste già una visita con questo titolo!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int minPartecipanti = (int) minPartecipantiSpinner.getValue();
            int maxPartecipanti = (int) maxPartecipantiSpinner.getValue();
            boolean richiedeBiglietto = bigliettoCheckbox.isSelected();

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
                    giorniSettimana,
                    oraInizio,
                    (int) durataSpinner.getValue(),
                    richiedeBiglietto,
                    minPartecipanti,
                    maxPartecipanti,
                    listaVolontari
            );

            if (isModifica) {
                tipiVisitaController.modificaTipoVisita(nuovaVisita);
                JOptionPane.showMessageDialog(this, "Tipo di visita modificato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                tipiVisitaController.aggiungiVisita(nuovaVisita);
                JOptionPane.showMessageDialog(this, "Tipo di visita aggiunto con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            }

            parent.aggiornaListaTipiVisita();

            chiudiEmandaIndietro();
        });

        JButton annullaButton = Costants.createSimpleButton("Annulla");
        annullaButton.addActionListener(e -> chiudiEmandaIndietro());

        JPanel footerPanel = Costants.createFooterPanel("");
        footerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);
        footerPanel.add(salvaButton, BorderLayout.CENTER);
        footerPanel.add(annullaButton, BorderLayout.EAST);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Se stiamo modificando un tipo visita esistente, popoliamo i campi
        if (isModifica) {
            popolaCampiDaModificare();
        }

        add(mainPanel);
        setVisible(true);
    }

    private void popolaCampiDaModificare() {
        titoloField.setText(tipoVisitaDaModificare.getTitolo());
        descrizioneField.setText(tipoVisitaDaModificare.getDescrizione());
        puntoIncontroField.setText(tipoVisitaDaModificare.getPuntoIncontro());
        dataInizioSpinner.setValue(Date.from(tipoVisitaDaModificare.getDataInizio().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dataFineSpinner.setValue(Date.from(tipoVisitaDaModificare.getDataFine().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        oraInizioSpinner.setValue(Date.from(tipoVisitaDaModificare.getOraInizio().atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()));
        durataSpinner.setValue(tipoVisitaDaModificare.getDurataMinuti());
        minPartecipantiSpinner.setValue(tipoVisitaDaModificare.getMinPartecipanti());
        maxPartecipantiSpinner.setValue(tipoVisitaDaModificare.getMaxPartecipanti());
        bigliettoCheckbox.setSelected(tipoVisitaDaModificare.isBigliettoNecessario());

        // Seleziona i giorni della settimana
        Set<DayOfWeek> giorniSelezionati = tipoVisitaDaModificare.getGiorniSettimana();
        List<Integer> indiciGiorniSelezionati = new ArrayList<>();
        
        for (int i = 0; i < giorniListModel.getSize(); i++) {
            String giorno = giorniListModel.getElementAt(i);
            DayOfWeek dayOfWeek = convertToDayOfWeek(giorno);
            if (giorniSelezionati.contains(dayOfWeek)) {
                indiciGiorniSelezionati.add(i);
            }
        }
        
        int[] indiciArray = indiciGiorniSelezionati.stream().mapToInt(Integer::intValue).toArray();
        giorniList.setSelectedIndices(indiciArray);

        // Seleziona i volontari
        List<String> volontariSelezionati = tipoVisitaDaModificare.getVolontari().stream()
                .map(Volontario::getNome)
                .collect(Collectors.toList());
                
        List<Integer> indiciVolontariSelezionati = new ArrayList<>();
        
        for (int i = 0; i < volontariListModelVolontari.getSize(); i++) {
            String nomeVolontario = volontariListModelVolontari.getElementAt(i);
            if (volontariSelezionati.contains(nomeVolontario)) {
                indiciVolontariSelezionati.add(i);
            }
        }
        
        int[] indiciVolontariArray = indiciVolontariSelezionati.stream().mapToInt(Integer::intValue).toArray();
        volontariListVolontari.setSelectedIndices(indiciVolontariArray);
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

    private void chiudiEmandaIndietro() {
        this.dispose();
        parent.setVisible(true);
    }

    public void aggiornaListaVolontari() {
        volontariListModelVolontari.clear();
        ArrayList<Volontario> volontari = volontariController.getListaVolontari();
        for (Volontario volontario : volontari) {
            volontariListModelVolontari.addElement(volontario.getNome());
        }
    }

    public JList<String> getVolontariList() {
        return volontariListVolontari;
    }

    private DayOfWeek convertToDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek.toLowerCase()) {
            case "lunedì":
                return DayOfWeek.MONDAY;
            case "martedì":
                return DayOfWeek.TUESDAY;
            case "mercoledì":
                return DayOfWeek.WEDNESDAY;
            case "giovedì":
                return DayOfWeek.THURSDAY;
            case "venerdì":
                return DayOfWeek.FRIDAY;
            case "sabato":
                return DayOfWeek.SATURDAY;
            case "domenica":
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Giorno della settimana non valido: " + dayOfWeek);
        }
    }

    private LocalDate convertToLocalDate(Object spinnerValue) {
        return ((Date) spinnerValue).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalTime convertToLocalTime(Object spinnerValue) {
        return ((Date) spinnerValue).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }
}