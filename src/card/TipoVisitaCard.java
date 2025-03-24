package card;

import costants.Costants;
import model.Luogo;
import model.TipoVisita;
import controller.LuoghiController;
import view.corpoDati.CorpoDatiFase2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TipoVisitaCard extends JPanel {
    private static final int CARD_WIDTH = 400;
    private static final int CARD_HEIGHT = 120;
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color ACCENT_COLOR = new Color(49, 130, 189);
    private static final Color DARK_TEXT = new Color(50, 50, 50);
    private static final Color LIGHT_TEXT = new Color(100, 100, 100);
    private static final Color BACKGROUND_HOVER = new Color(235, 245, 255);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private final TipoVisita tipoVisita;
    private final LuoghiController controller;
    private final JPanel contentPanel;

    public TipoVisitaCard(TipoVisita tipoVisita, LuoghiController controller) {
        this.tipoVisita = tipoVisita;
        this.controller = controller;

        System.out.println(tipoVisita.getGiorniSettimana().toString());
        System.out.println(tipoVisita.getVolontari().toString());


        // Configurazione panel
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, CARD_HEIGHT));

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        // Contenitore principale
        contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(12, 15, 12, 15));


        // Area informazioni
        JPanel infoPanel = createInfoPanel();



        // Assembly

        contentPanel.add(infoPanel, BorderLayout.CENTER);
        //contentPanel.add(actionPanel, BorderLayout.EAST);
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

        // Titolo
        JLabel nomeLabel = new JLabel(tipoVisita.getTitolo());
        nomeLabel.setFont(TITLE_FONT);
        nomeLabel.setForeground(DARK_TEXT);

        // Descrizione
        JLabel posizioneLabel = new JLabel("Descrizione: " + tipoVisita.getDescrizione());
        posizioneLabel.setFont(NORMAL_FONT);
        posizioneLabel.setForeground(LIGHT_TEXT);

        // DataInizio e data fine
        String data_inizio = Costants.formatToItalian(tipoVisita.getDataInizio());
        String data_fine = Costants.formatToItalian(tipoVisita.getDataFine());
        JLabel descrizioneLabel = new JLabel("Dal "+data_inizio+" al "+data_fine);
        descrizioneLabel.setFont(NORMAL_FONT);
        descrizioneLabel.setForeground(LIGHT_TEXT);


        panel.add(nomeLabel);
        panel.add(posizioneLabel);
        panel.add(descrizioneLabel);


        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Layout verticale
        panel.setOpaque(false);

        JButton modificaButton = createIconButton("Modifica");
        JButton eliminaButton = createIconButton("Elimina");


        // Aggiungere spazio tra i pulsanti
        modificaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        eliminaButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(modificaButton);
        panel.add(Box.createVerticalStrut(10)); // Spazio tra i pulsanti
        panel.add(eliminaButton);

        return panel;
    }


    private JButton createIconButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }



    private boolean isValidInput(String nome, String posizione) {
        return nome != null && !nome.trim().isEmpty() &&
                posizione != null && !posizione.trim().isEmpty();
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
