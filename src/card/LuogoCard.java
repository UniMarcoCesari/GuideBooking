package card;

import model.Luogo;
import model.TipoVisita;
import controller.LuoghiController;
import view.LuoghiFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class LuogoCard extends JPanel {
    private static final int CARD_WIDTH = 750;
    private static final int CARD_HEIGHT = 120;
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color ACCENT_COLOR = new Color(49, 130, 189);
    private static final Color DARK_TEXT = new Color(50, 50, 50);
    private static final Color LIGHT_TEXT = new Color(100, 100, 100);
    private static final Color BACKGROUND_HOVER = new Color(235, 245, 255);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private final Luogo luogo;
    private final LuoghiController controller;
    private final LuoghiFrame frame;
    private final JPanel contentPanel;

    public LuogoCard(Luogo luogo, LuoghiController controller, LuoghiFrame frame) {
        this.luogo = luogo;
        this.controller = controller;
        this.frame = frame;

        // Configurazione panel
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        // Contenitore principale
        contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Icona luogo
        JLabel iconLabel = new JLabel("📍");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Area informazioni
        JPanel infoPanel = createInfoPanel();

        // Area pulsanti
        JPanel actionPanel = createButtonPanel();

        // Assembly
        contentPanel.add(iconLabel, BorderLayout.WEST);
        contentPanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.EAST);
        add(contentPanel);

        // Effetto hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(BACKGROUND_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.WHITE);
            }
        });
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 3));
        panel.setOpaque(false);

        // Nome
        JLabel nomeLabel = new JLabel(luogo.getNome());
        nomeLabel.setFont(TITLE_FONT);
        nomeLabel.setForeground(DARK_TEXT);

        // Posizione
        JLabel posizioneLabel = new JLabel("📍 " + luogo.getPosizione());
        posizioneLabel.setFont(NORMAL_FONT);
        posizioneLabel.setForeground(LIGHT_TEXT);

        // Descrizione
        JLabel descrizioneLabel = new JLabel("📝 " + (luogo.getDescrizione().isEmpty() ? "Nessuna descrizione" : luogo.getDescrizione()));
        descrizioneLabel.setFont(NORMAL_FONT);
        descrizioneLabel.setForeground(LIGHT_TEXT);

        // Tipi di Visita
        JLabel tipiVisitaLabel = new JLabel("🗓️ " + formatTipiVisita(luogo.getTipiVisita()));
        tipiVisitaLabel.setFont(NORMAL_FONT);
        tipiVisitaLabel.setForeground(LIGHT_TEXT);

        panel.add(nomeLabel);
        panel.add(posizioneLabel);
        panel.add(descrizioneLabel);
        panel.add(tipiVisitaLabel);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);

        JButton modificaButton = createIconButton("✏️ Modifica");
        JButton eliminaButton = createIconButton("🗑️ Elimina");

        modificaButton.addActionListener(e -> modificaLuogo());
        eliminaButton.addActionListener(e -> eliminaLuogo());

        panel.add(modificaButton);
        panel.add(eliminaButton);

        return panel;
    }

    private JButton createIconButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void modificaLuogo() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 5, 15));

        JTextField nomeField = new JTextField(luogo.getNome(), 20);
        JTextField posizioneField = new JTextField(luogo.getPosizione(), 20);
        JTextField descrizioneField = new JTextField(luogo.getDescrizione(), 20);

        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Posizione:"));
        panel.add(posizioneField);
        panel.add(new JLabel("Descrizione:"));
        panel.add(descrizioneField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Modifica Luogo", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nuovoNome = nomeField.getText().trim();
            String nuovaPosizione = posizioneField.getText().trim();
            String nuovaDescrizione = descrizioneField.getText().trim();

            if (isValidInput(nuovoNome, nuovaPosizione)) {
                updateLuogo(nuovoNome, nuovaPosizione, nuovaDescrizione);
                controller.salvaDati();
                //frame.aggiornaLista();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Nome e Posizione sono obbligatori!",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean isValidInput(String nome, String posizione) {
        return nome != null && !nome.trim().isEmpty() &&
                posizione != null && !posizione.trim().isEmpty();
    }

    private void updateLuogo(String nome, String posizione, String descrizione) {
        luogo.setNome(nome);
        luogo.setPosizione(posizione);
        luogo.setDescrizione(descrizione);
    }

    private void eliminaLuogo() {
        int conferma = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare il luogo '" + luogo.getNome() + "'?",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (conferma == JOptionPane.YES_OPTION) {
            controller.getLuoghi().remove(luogo);
            controller.salvaDati();
            frame.aggiornaListaLuoghi();
        }
    }

    private String formatTipiVisita(List<TipoVisita> tipiVisita) {
        if (tipiVisita == null || tipiVisita.isEmpty()) {
            return "Nessun tipo di visita";
        }
        return tipiVisita.stream()
                .map(TipoVisita::getTitolo)
                .collect(Collectors.joining(", "));
    }
}
