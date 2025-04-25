package view.volontario;

import controller.VisiteController;
import costants.Costants;
import model.Visita;
import view.PannelloVolontario; // Per tornare indietro

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import card.VisitaCard; // Usa la card migliorata

public class VisualizzaMieVisiteFrame extends JFrame {

    private final String username;
    private final VisiteController visiteController;

    public VisualizzaMieVisiteFrame(String username, VisiteController visiteController) {
        this.username = username;
        this.visiteController = visiteController;

        initializeFrame();

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        // mainPanel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING)); // Rimosso per eliminare padding esterno
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        // Usiamo lo stesso colore header del PannelloVolontario per coerenza
        JPanel headerPanel = Costants.createHeaderPanel("Le Tue Visite Assegnate");
        headerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);

        JButton indietro = Costants.createSimpleButton("Indietro");
        indietro.setFont(Costants.BUTTON_FONT);
        indietro.addActionListener(e -> {
            dispose();
            // Torna al PannelloVolontario passando l'username
            new PannelloVolontario(this.username).setVisible(true);
        });
        // Pannello per allineare il bottone a sinistra
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(indietro);
        headerPanel.add(buttonPanel, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale con ScrollPane e GridLayout per le card
        JPanel cardContainerPanel = new JPanel();
        cardContainerPanel.setLayout(new GridLayout(0, 3, Costants.SPACING, Costants.SPACING)); // 3 colonne
        cardContainerPanel.setBackground(Costants.BACKGROUND_COLOR);
        cardContainerPanel.setBorder(new EmptyBorder(Costants.SPACING, 0, Costants.SPACING, 0));

        // Filtra le visite per questo volontario utilizzando getAllVisite()
        List<Visita> mieVisite = visiteController.getAllVisite() // Ottieni tutte le visite
                                    .stream()
                                    .filter(v -> v.getGuidaAssegnata() != null && v.getGuidaAssegnata().getNome().equals(this.username)) // Filtra per username
                                    .sorted(Comparator.comparing(Visita::getData)) // Ordina per data
                                    .collect(Collectors.toList());


        if (mieVisite.isEmpty()) {
            JLabel noVisiteLabel = new JLabel("Nessuna visita assegnata trovata.", SwingConstants.CENTER);
            noVisiteLabel.setFont(Costants.TITLE_FONT);
            cardContainerPanel.setLayout(new BorderLayout());
            cardContainerPanel.add(noVisiteLabel, BorderLayout.CENTER);
        } else {
            for (Visita visita : mieVisite) {
                VisitaCard visitaCard = new VisitaCard(visita);
                cardContainerPanel.add(visitaCard);
            }
            // Aggiungi pannelli vuoti per riempire l'ultima riga
             int items = mieVisite.size();
             int cols = 3;
             int remainder = items % cols;
             if (remainder != 0) {
                 for (int i = 0; i < cols - remainder; i++) {
                     cardContainerPanel.add(new JPanel(){{setOpaque(false);}});
                 }
             }
        }

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(cardContainerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer (opzionale, può essere vuoto)
        JPanel footerPanel = Costants.createFooterPanel("");
        footerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Le Tue Visite - " + username);
        setSize(1000, 700); // Leggermente più piccolo di Sezione1
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiudi solo questa finestra
    }
}
