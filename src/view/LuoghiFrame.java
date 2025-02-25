package view;

import card.LuogoCard;
import controller.TipiVisitaController;
import model.Luogo;
import controller.LuoghiController;
import model.TipoVisita;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LuoghiFrame extends JFrame {
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 800;
    private static final Color BACKGROUND_COLOR = new Color(245, 248, 250);
    private static final Color ACCENT_COLOR = new Color(49, 130, 189);
    private static final Color TEXT_COLOR = new Color(60, 60, 60);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final int SPACING = 12;

    private final JPanel listaPanel;
    private JTextField nomeField;
    private JTextField descrizioneField;
    private JTextField posizioneField;
    private final LuoghiController controller;
    private final TipiVisitaController tipoVisitaController;
    private JComboBox<String> tipiVisitaComboBox;
    private DefaultListModel<String> tipiVisitaModel;
    private JList<String> tipiVisitaList;

    public LuoghiFrame() {
        this.controller = new LuoghiController();
        this.tipoVisitaController = new TipiVisitaController();

        initializeFrame();

        JPanel mainPanel = new JPanel(new BorderLayout(SPACING, SPACING));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(SPACING, SPACING, SPACING, SPACING));

        JPanel headerPanel = createHeaderPanel("Gestione Luoghi");
        listaPanel = createListPanel();
        JScrollPane scrollPane = createStyledScrollPane(listaPanel);
        JPanel inputPanel = createInputPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel);
        aggiornaLista();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Gestione Luoghi");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JPanel createHeaderPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(ACCENT_COLOR);
        panel.setBorder(new EmptyBorder(10, SPACING, 10, SPACING));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Aggiungi Nuovo Luogo"));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        nomeField = createStyledTextField();
        descrizioneField = createStyledTextField();
        posizioneField = createStyledTextField();

        tipiVisitaModel = new DefaultListModel<>();
        tipiVisitaList = new JList<>(tipiVisitaModel);
        JScrollPane tipiVisitaScrollPane = createStyledScrollPane(tipiVisitaList);
        tipiVisitaScrollPane.setPreferredSize(new Dimension(0, 80));

        tipiVisitaComboBox = new JComboBox<>();
        for (model.TipoVisita tipoVisita : tipoVisitaController.getTipiVisita()) {
            tipiVisitaComboBox.addItem(tipoVisita.getTitolo());
        }

        JButton addTipoVisitaButton = createStyledButton("Aggiungi Tipo");
        addTipoVisitaButton.addActionListener(e -> aggiungiTipoVisita());

        JButton addButton = createStyledButton("Salva Luogo");
        addButton.addActionListener(e -> aggiungiLuogo());

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; panel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Descrizione:"), gbc);
        gbc.gridx = 1; panel.add(descrizioneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Posizione:"), gbc);
        gbc.gridx = 1; panel.add(posizioneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Tipi di visita:"), gbc);
        gbc.gridx = 1; panel.add(tipiVisitaComboBox, gbc);
        gbc.gridx = 2; panel.add(addTipoVisitaButton, gbc);

        gbc.gridx = 1; gbc.gridy = 4; panel.add(tipiVisitaScrollPane, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(addButton, gbc);

        return panel;
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
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void aggiungiTipoVisita() {
        String tipoVisita = (String) tipiVisitaComboBox.getSelectedItem();
        if (tipoVisita != null && !tipiVisitaModel.contains(tipoVisita)) {
            tipiVisitaModel.addElement(tipoVisita);
        }
    }

    public void aggiornaLista() {
        listaPanel.removeAll();
        List<Luogo> luoghi = controller.getLuoghi();

        if (luoghi.isEmpty()) {
            listaPanel.add(new JLabel("Nessun luogo disponibile.", SwingConstants.CENTER));
        } else {
            luoghi.forEach(this::addLuogoCard);
        }

        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void addLuogoCard(Luogo luogo) {
        listaPanel.add(Box.createVerticalStrut(6));
        listaPanel.add(new LuogoCard(luogo, controller, this));
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
            JOptionPane.showMessageDialog(this, "Insersci almeno un tipo visita");
            return;
        }

        controller.aggiungiLuogo(new Luogo(nome, descrizione, posizione,tipiVisita));
        aggiornaLista();
        JOptionPane.showMessageDialog(this, "Luogo aggiunto con successo!");
        clearFields();
    }

    private boolean validateInput(String nome, String posizione) {
        return !nome.isEmpty() && !posizione.isEmpty();
    }

    private void clearFields() {
        nomeField.setText("");
        descrizioneField.setText("");
        posizioneField.setText("");
        tipiVisitaModel.clear();
    }
}
