package view;

import card.LuogoCard;
import controller.TipiVisitaController;
import model.Luogo;
import controller.LuoghiController;
import model.TipoVisita;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LuoghiFrame extends JFrame {
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 600;
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    private final JPanel listaPanel;
    private  JTextField nomeField;
    private  JTextField descrizioneField;
    private  JTextField posizioneField;
    private final LuoghiController controller;

    private final TipiVisitaController tipoVisitaController;

    public LuoghiFrame() {
        this.controller = new LuoghiController();
        this.tipoVisitaController = new TipiVisitaController();
        initializeFrame();

        listaPanel = createListPanel();
        add(new JScrollPane(listaPanel), BorderLayout.CENTER);
        add(createInputPanel(), BorderLayout.SOUTH);

        aggiornaLista();
    }

    private void initializeFrame() {
        setTitle("Gestione Luoghi");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        nomeField = addLabeledField(inputPanel, "Nome:");
        descrizioneField = addLabeledField(inputPanel, "Descrizione:");
        posizioneField = addLabeledField(inputPanel, "Posizione:");

        // Sezione per associare tipi di visita
        JLabel tipiVisitaLabel = new JLabel("Tipi di visita:");
        JComboBox<String> tipiVisitaComboBox = new JComboBox<>();
        for (model.TipoVisita tipoVisita : tipoVisitaController.getTipiVisita()) {
            tipiVisitaComboBox.addItem(tipoVisita.getTitolo());
        }
        inputPanel.add(tipiVisitaLabel);
        inputPanel.add(tipiVisitaComboBox);

        JButton addButton = new JButton("➕ Aggiungi Luogo");
        addButton.addActionListener(e -> aggiungiLuogo());
        inputPanel.add(addButton);

        return inputPanel;
    }


    private JTextField addLabeledField(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextField field = new JTextField();
        panel.add(field);
        return field;
    }

    public void aggiornaLista() {
        listaPanel.removeAll();
        List<Luogo> luoghi = controller.getLuoghi();

        if (luoghi.isEmpty()) {
            addEmptyStateLabel();
        } else {
            luoghi.forEach(this::addLuogoCard);
        }

        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private void addEmptyStateLabel() {
        JLabel emptyLabel = new JLabel("⚠️ Nessun luogo disponibile.");
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        emptyLabel.setForeground(Color.GRAY);
        listaPanel.add(emptyLabel);
    }

    private void addLuogoCard(Luogo luogo) {
        JPanel cardContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardContainer.setBackground(BACKGROUND_COLOR);
        cardContainer.add(new LuogoCard(luogo, controller, this));
        listaPanel.add(cardContainer);
    }

    private void aggiungiLuogo() {
        String nome = nomeField.getText().trim();
        String descrizione = descrizioneField.getText().trim();
        String posizione = posizioneField.getText().trim();

        if (!validateInput(nome, posizione)) {
            return;
        }

        controller.aggiungiLuogo(new Luogo(nome, descrizione, posizione));
        aggiornaLista();
        showSuccessMessage();
        clearFields();
    }

    private boolean validateInput(String nome, String posizione) {
        if (nome.isEmpty() || posizione.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nome e Posizione sono obbligatori!",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void showSuccessMessage() {
        JOptionPane.showMessageDialog(this, "Luogo aggiunto con successo!");
    }

    private void clearFields() {
        nomeField.setText("");
        descrizioneField.setText("");
        posizioneField.setText("");
    }
}