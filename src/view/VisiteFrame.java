package view;

import controller.TipiVisitaController;
import model.TipoVisita;
import costants.Costants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VisiteFrame extends JFrame {
    private JPanel listaPanel;
    private TipiVisitaController controller;

    public VisiteFrame(TipiVisitaController controller) {
        setTitle("Gestione Visite");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        this.controller = controller;

        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Titolo
        JLabel titleLabel = new JLabel("Tipi di Visita Disponibili");
        titleLabel.setFont(Costants.TITLE_FONT);
        titleLabel.setForeground(Costants.ACCENT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Lista delle visite
        listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listaPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        aggiornaLista();

        // Pulsante per aprire il form di aggiunta
        JButton aggiungiButton = Costants.createMenuButton("Aggiungi Visita", "➕");
        aggiungiButton.addActionListener(e -> chiudiEMandaIndietro(this));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Costants.BACKGROUND_COLOR);
        buttonPanel.add(aggiungiButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }


    public void chiudiEMandaIndietro(VisiteFrame visiteFrame) {
        dispose();
        new NuovaVisitaFrame(this);
    }

    public void aggiornaLista() {
        listaPanel.removeAll();
        List<TipoVisita> visite = controller.getTipiVisita();

        if (visite.isEmpty()) {
            JLabel noDataLabel = new JLabel("⚠️ Nessuna visita disponibile.");
            noDataLabel.setFont(Costants.BUTTON_FONT);
            noDataLabel.setForeground(Costants.ACCENT_COLOR);
            listaPanel.add(noDataLabel);
        } else {
            for (TipoVisita visita : visite) {
                listaPanel.add(creaCardVisita(visita));
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private JPanel creaCardVisita(TipoVisita visita) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Costants.BORDER_COLOR, 2, true));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(700, 80));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titoloLabel = new JLabel("📌 " + visita.getTitolo());
        titoloLabel.setFont(Costants.BUTTON_FONT);
        titoloLabel.setForeground(Costants.ACCENT_COLOR);

        JLabel descrizioneLabel = new JLabel(visita.getDescrizione());
        descrizioneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descrizioneLabel.setForeground(Costants.BORDER_COLOR);
        textPanel.add(titoloLabel);
        textPanel.add(descrizioneLabel);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }
}
