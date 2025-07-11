package card;

import model.TipoVisita;
import model.Volontario;
import view.configuratore.ListaVolontari;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.CalendarioController;
import controller.TipiVisitaController;
import controller.VolontariController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class VolontarioCard extends JPanel {
    private static final int CARD_WIDTH = 400;
    private static final int CARD_HEIGHT = 120;
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color DARK_TEXT = new Color(50, 50, 50);
    private static final Color LIGHT_TEXT = new Color(100, 100, 100);
    private static final Color BACKGROUND_HOVER = new Color(235, 245, 255);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private final Volontario volontario;
    private final List<TipoVisita> listaTipoVisita;
    private final VolontariController volontarioController;
    private final TipiVisitaController tipoVisitaController;
    private final CalendarioController calendarioController;
    private final ListaVolontari parent;

    public VolontarioCard(Volontario volontario, List<TipoVisita> listaTipoVisita, VolontariController volontarioController,TipiVisitaController tipoVisitaController,CalendarioController calendarioController,ListaVolontari parent) {
        this.volontario = volontario;
        this.listaTipoVisita = listaTipoVisita;
        this.volontarioController = volontarioController;
        this.tipoVisitaController = tipoVisitaController;
        this.calendarioController = calendarioController;
        this.parent = parent;

        // Configurazione panel
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, CARD_HEIGHT));

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        // Contenitore principale
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Area informazioni
        JPanel infoPanel = createInfoPanel();

        // Assembly
        JPanel buttonPanel = createButtonPanel();
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
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 3));
        panel.setOpaque(false);

        // Nome volontario
        JLabel nomeLabel = new JLabel(volontario.getNome());
        nomeLabel.setFont(TITLE_FONT);
        nomeLabel.setForeground(DARK_TEXT);

        // Tipi di visita
        JLabel tipiVisitaLabel = new JLabel("Tipi di visita: " + formatTipiVisita(listaTipoVisita));
        tipiVisitaLabel.setFont(NORMAL_FONT);
        tipiVisitaLabel.setForeground(LIGHT_TEXT);

        panel.add(nomeLabel);
        panel.add(tipiVisitaLabel);

        return panel;
    }

    private String formatTipiVisita(List<TipoVisita> tipiVisita) {
        if (tipiVisita == null || tipiVisita.isEmpty()) {
            return "Nessun tipo di visita";
        }
        return tipiVisita.stream()
                .map(TipoVisita::getTitolo)
                .collect(Collectors.joining(", "));
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Layout verticale
        panel.setOpaque(false);

        JButton eliminaButton = createIconButton("Elimina");
        if(!calendarioController.isFaseModificabile())
        {
            eliminaButton.setEnabled(false);
        }
        eliminaButton.addActionListener(e -> {
            volontarioController.rimuoviVolontario(volontario);
            tipoVisitaController.rimuoviVolonatario(volontario);
            parent.aggiornaListaVolontari();
            System.out.println("Elimina button clicked for " + volontario.getNome());
        });


        // Aggiungere spazio tra i pulsanti
        eliminaButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(eliminaButton);

        return panel;
    }

    private JButton createIconButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(49, 130, 189));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }
}
