package view.corpoDati;

import card.LuogoCard;
import controller.TipiVisitaController;
import controller.VolontariController;
import costants.Costants;
import model.CorpoDati;
import model.Luogo;
import controller.LuoghiController;
import model.TipoVisita;
import view.configuratore.PannelloConfiguratore;
import view.login.MainController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CorpoDatiFase2 extends JFrame {
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private static final Color BACKGROUND_COLOR = new Color(245, 248, 250);
    private static final Color ACCENT_COLOR = new Color(49, 130, 189);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);


    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final int SPACING = 12;

    private final JPanel listaPanel;
    private JTextField nomeField;
    private JTextField descrizioneField;
    private JTextField posizioneField;
    private JComboBox<String> tipiVisitaComboBox;
    private DefaultListModel<String> tipiVisitaModel;
    private JList<String> tipiVisitaList;

    private CorpoDati corpoDati;    

    private final MainController mainController;

    private final LuoghiController luoghiController;
    private final TipiVisitaController tipoVisitaController;
    private final VolontariController volontariController;

    public CorpoDatiFase2(CorpoDati corpoDati, MainController mainController) {
        this.mainController = mainController;
        this.luoghiController = mainController.getLuoghiController();
        this.tipoVisitaController = mainController.getTipiVisitaController();
        this.volontariController = mainController.getVolontarioController();
        this.corpoDati = corpoDati;

        initializeFrame();

        // Crea un pannello principale con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(SPACING, SPACING));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Crea l'intestazione
        JPanel headerPanel = Costants.createHeaderPanel("Creazione corpo dei dati - Fase 2");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Crea un pannello per il contenuto con un JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBorder(null);
        splitPane.setDividerSize(0); // Elimina la barra di divisione
        splitPane.setResizeWeight(0.5);
        splitPane.setEnabled(false);  // Disabilita il ridimensionamento

        // Pannello sinistro per la lista dei luoghi
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setMinimumSize(new Dimension(FRAME_WIDTH / 2, FRAME_HEIGHT));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setBorder(new EmptyBorder(SPACING, SPACING, SPACING, SPACING));  // Solo spazio

        // Aggiungi un titolo al pannello di sinistra
        JLabel listTitleLabel = new JLabel("Lista dei Luoghi");
        listTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitleLabel.setBorder(new EmptyBorder(0, 0, SPACING, 0));

        listaPanel = createListPanel();
        JScrollPane scrollPane = createStyledScrollPane(listaPanel);

        leftPanel.add(listTitleLabel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Pannello destro per l'input
        JPanel inputPanel = createInputPanel();
        inputPanel.setBorder(new EmptyBorder(SPACING, SPACING, SPACING, SPACING));  // Solo spazio
        inputPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        // Aggiungi un titolo al pannello di destra
        JLabel inputTitleLabel = new JLabel("Gestione Input");
        inputTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        inputTitleLabel.setBorder(new EmptyBorder(0, 0, SPACING, 0));

        // Crea un pannello con BorderLayout per il pannello di destra
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(new EmptyBorder(SPACING, SPACING, SPACING, SPACING));  // Solo spazio
        rightPanel.add(inputTitleLabel, BorderLayout.NORTH);
        rightPanel.add(inputPanel, BorderLayout.CENTER);

        // Aggiungi pannelli al JSplitPane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        // Aggiungi JSplitPane al pannello principale
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Crea l'intestazione
        JPanel footerPanel = Costants.createFooterPanel("");

        // bottone
        JButton salvaButton = Costants.createSimpleButton("Avvia applicazione");
        salvaButton.addActionListener(_ -> {salvaEMandaPannello();});
        footerPanel.add(salvaButton);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        aggiornaListaLuoghi();
        setVisible(true);
    }


    private void salvaEMandaPannello()
    {
        mainController.salvaCorpoDati(corpoDati);
        dispose();
        new PannelloConfiguratore(mainController).setVisible(true);
    }


    private void initializeFrame() {
        setTitle("Gestione Luoghi");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    private JPanel createListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(SPACING, SPACING, SPACING, SPACING));
        return panel;
    }

    private JScrollPane createStyledScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createInputPanel() {
        // Crea un panel principale con GridBagLayout per un posizionamento flessibile
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(BACKGROUND_COLOR);

        // Configura GridBagConstraints per controllare il layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Aumentato spazio tra componenti
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Inizializza i campi di testo con stile coerente
        nomeField = createStyledTextField();
        descrizioneField = createStyledTextField();
        posizioneField = createStyledTextField();

        // Configura la lista dei tipi di visita
        tipiVisitaModel = new DefaultListModel<>();
        tipiVisitaList = new JList<>(tipiVisitaModel);
        tipiVisitaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tipiVisitaScrollPane = createStyledScrollPane(tipiVisitaList);
        tipiVisitaScrollPane.setMinimumSize(new Dimension(0, 100));  // Aumentata altezza


        // Configura la combo box dei tipi di visita
        tipiVisitaComboBox = new JComboBox<>();
        for (model.TipoVisita tipoVisita : tipoVisitaController.getTipiVisita()) {
            String descr = "["+tipoVisita.getTitolo()+"]";
            tipiVisitaComboBox.addItem(descr);
        }
        tipiVisitaComboBox.setPreferredSize(new Dimension(200, 30));  // Dimensione fissa per la combo box

        // Crea bottoni con testo migliorato e font più grande
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 12);

        JButton addTipoVisitaButton = createStyledButton("Aggiungi Tipo");
        addTipoVisitaButton.setFont(buttonFont);
        addTipoVisitaButton.setToolTipText("Aggiungi il tipo di visita selezionato alla lista");
        addTipoVisitaButton.addActionListener(e -> aggiungiTipoVisita());

        JButton addNewTipoVisitaButton = createStyledButton("Nuovo Tipo");
        addNewTipoVisitaButton.setFont(buttonFont);
        addNewTipoVisitaButton.setToolTipText("Crea un nuovo tipo di visita");
        addNewTipoVisitaButton.addActionListener(e -> openTipoVisitaDialog());

        JButton addButton = createStyledButton("Salva Luogo");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));  // Font più grande per il bottone principale
        addButton.setBackground(new Color(46, 139, 87));  // Verde più scuro per il bottone principale
        addButton.addActionListener(e -> aggiungiLuogo());

        // Crea label con font migliorato
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        JLabel nomeLabel = new JLabel("Nome:");
        JLabel descrizioneLabel = new JLabel("Descrizione:");
        JLabel posizioneLabel = new JLabel("Posizione:");
        JLabel tipiVisitaLabel = new JLabel("Tipi di visita:");

        nomeLabel.setFont(labelFont);
        descrizioneLabel.setFont(labelFont);
        posizioneLabel.setFont(labelFont);
        tipiVisitaLabel.setFont(labelFont);

        // Aggiungi componenti al panel usando GridBagConstraints
        // Prima riga - Nome
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(nomeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(nomeField, gbc);

        // Seconda riga - Descrizione
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(descrizioneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(descrizioneField, gbc);

        // Terza riga - Posizione
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(posizioneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(posizioneField, gbc);

        // Quarta riga - ComboBox e bottoni per tipo visita
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(tipiVisitaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(tipiVisitaComboBox, gbc);

        // Creiamo un pannello per i bottoni orizzontali
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addTipoVisitaButton);
        buttonPanel.add(addNewTipoVisitaButton);

        gbc.gridx = 2;
        panel.add(buttonPanel, gbc);

        // Quinta riga - Lista tipi visita
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel selectedTypesLabel = new JLabel("Tipi selezionati:");
        selectedTypesLabel.setFont(labelFont);
        panel.add(selectedTypesLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(tipiVisitaScrollPane, gbc);

        // Sesta riga - Bottone salva
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);  // Più spazio sopra il bottone salva
        panel.add(addButton, gbc);

        return panel;
    }

    private void openTipoVisitaDialog() {
        new NuovoTipoVisitaFrame(this, tipoVisitaController,volontariController).setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(LABEL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(LABEL_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(true);
        return button;
    }

    private void aggiungiTipoVisita() {
        String tipoVisita = (String) tipiVisitaComboBox.getSelectedItem();
        if (tipoVisita != null && !tipiVisitaModel.contains(tipoVisita)) {
            tipiVisitaModel.addElement(tipoVisita);
        }
    }

    public void aggiornaListaLuoghi() {
        listaPanel.removeAll();
        List<Luogo> luoghi = luoghiController.getLuoghi();

        if (luoghi.isEmpty()) {
            JLabel emptyLabel = new JLabel("Nessun luogo disponibile.", SwingConstants.CENTER);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaPanel.add(emptyLabel);
        } else {
            luoghi.forEach(this::addLuogoCard);
        }

        listaPanel.revalidate();
        listaPanel.repaint();
    }

    public void aggiornaListaTipiVisita() {
        // Clear the current combo box items
        tipiVisitaComboBox.removeAllItems();

        // Add updated tipi visita to the combo box
        for (TipoVisita tipoVisita : tipoVisitaController.getTipiVisita()) {
            tipiVisitaComboBox.addItem(tipoVisita.getTitolo());
        }

        // Clear the model (no need to recreate it)
        tipiVisitaModel.clear();

        // Refresh the UI
        tipiVisitaComboBox.revalidate();
        tipiVisitaComboBox.repaint();
        tipiVisitaList.revalidate();
        tipiVisitaList.repaint();
    }

    private void addLuogoCard(Luogo luogo) {
        listaPanel.add(Box.createVerticalStrut(6));
        listaPanel.add(new LuogoCard(luogo,luoghiController,null));
    }

    private void aggiungiLuogo() {
        String nome = nomeField.getText().trim();
        String descrizione = descrizioneField.getText().trim();
        String posizione = posizioneField.getText().trim();
        ArrayList<TipoVisita> tipiVisita = new ArrayList<>();

        for (int i = 0; i < tipiVisitaModel.size(); i++) {
            String n = tipiVisitaModel.elementAt(i);
            for (int j = 0; j < tipoVisitaController.getTipiVisita().size(); j++) {
                if(n.equals(tipoVisitaController.getTipiVisita().get(j).getTitolo())) {
                    tipiVisita.add(tipoVisitaController.getTipiVisita().get(j));
                }
            }
        }

        if (!validateInput(nome, posizione)) return;

        if (tipiVisitaModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci almeno un tipo visita");
            return;
        }

        luoghiController.aggiungiLuogo(new Luogo(nome, descrizione, posizione, tipiVisita));

        aggiornaListaLuoghi();
        JOptionPane.showMessageDialog(this, "Luogo aggiunto con successo!");
        clearFields();
    }

    private boolean validateInput(String nome, String posizione) {
        if (nome.isEmpty() || posizione.isEmpty()) {
            JOptionPane.showMessageDialog(this, "I campi Nome e Posizione sono obbligatori",
                    "Errore di validazione", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearFields() {
        nomeField.setText("");
        descrizioneField.setText("");
        posizioneField.setText("");
        tipiVisitaModel.clear();
    }

}
