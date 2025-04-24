package card;

import costants.Costants;
import model.TipoVisita;
import model.Volontario;
import view.configuratore.ListaTipiVisita;
import view.configuratore.NuovoTipoVisita;
import controller.TipiVisitaController; // Added import for TipiVisitaController
import controller.VisiteController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private final JPanel contentPanel;
    private final TipiVisitaController tipiVisitaController;
    private final VisiteController visiteController;
    private final ListaTipiVisita parent;

    public TipoVisitaCard(ListaTipiVisita parent,TipoVisita tipoVisita, TipiVisitaController tipiVisitaController, VisiteController visiteController) {
        this.tipoVisita = tipoVisita;
        this.parent = parent;
        this.tipiVisitaController = tipiVisitaController;
        this.visiteController = visiteController;

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

        // Action buttons
        JPanel buttonPanel = createButtonPanel(tipoVisita);

        // Assembly
        contentPanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.EAST);
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
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 3)); // Increased rows to 5
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

        // Volontari associati
        String volontariStr = formatVolontari(tipoVisita.getVolontari());
        JLabel volontariLabel = new JLabel("Volontari: " + volontariStr);
        volontariLabel.setFont(NORMAL_FONT);
        volontariLabel.setForeground(LIGHT_TEXT);

        panel.add(nomeLabel);
        panel.add(posizioneLabel);
        panel.add(descrizioneLabel);
        panel.add(volontariLabel); // Add the volunteers label

        return panel;
    }

    private JPanel createButtonPanel(TipoVisita tipoVisita) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Layout verticale
        panel.setOpaque(false);

        JButton modificaButton = createIconButton("Modifica");
        modificaButton.addActionListener(e -> {
            
            
            // Open the NuovoTipoVisita frame in edit mode by passing the tipo visita to modify
            new NuovoTipoVisita(parent, tipiVisitaController, tipoVisita);
            
            // Hide the parent frame since the new frame will be displayed
            parent.setVisible(false);
        });
        
        JButton eliminaButton = createIconButton("Elimina");
        eliminaButton.addActionListener(e -> {
            tipiVisitaController.rimuoviTipoVisita(tipoVisita);
            visiteController.eliminaVisiteConTipoVisita(tipoVisita);
            parent.aggiornaListaTipiVisita();});
       

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


    // Helper method to format volunteer names
    private String formatVolontari(List<Volontario> volontari) {
        if (volontari == null || volontari.isEmpty()) {
            return "Nessuno";
        }
        return volontari.stream()
                .map(Volontario::getNome) // Get username
                .collect(Collectors.joining(", "));
    }
}