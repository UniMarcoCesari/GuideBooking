package view.configuratore;

import controller.CalendarioController;
import controller.VisiteController;
import costants.Costants;
import model.Visita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import card.VisitaCard; // Assicurati che VisitaCard sia importata

public class Sezione1 extends JFrame {
    private final YearMonth meseAnno; // Cambiato a YearMonth per chiarezza
    private final VisiteController visiteController;
    private final CalendarioController calendarioController;

    public Sezione1(VisiteController visiteController, CalendarioController calendarioController) {
        this.visiteController = visiteController;
        this.calendarioController = calendarioController;

        // Determina il mese e anno target (il prossimo mese rispetto alla data corrente del calendario)
        LocalDate dataCorrente = calendarioController.getDatacDateCorrenteLocalDate();
        this.meseAnno = YearMonth.from(dataCorrente.plusMonths(1));

        initializeFrame();

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING)); // Padding esterno
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ITALIAN);
        JPanel headerPanel = Costants.createHeaderPanel("Visite Pianificate per " + meseAnno.format(formatter));

        JButton indietro = Costants.createSimpleButton("Indietro");
        indietro.setFont(Costants.BUTTON_FONT); // Applica font costante
        indietro.addActionListener(e -> {
            dispose();
            // Assumendo che PannelloConfiguratore non richieda argomenti o che siano gestiti diversamente
            new PannelloConfiguratore().setVisible(true);
        });
        // Aggiungi un pannello per il bottone per allinearlo a sinistra nell'header
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false); // Rendi trasparente per usare lo sfondo dell'header
        buttonPanel.add(indietro);
        headerPanel.add(buttonPanel, BorderLayout.WEST); // Aggiungi il pannello del bottone

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale con ScrollPane e GridLayout
        JPanel cardContainerPanel = new JPanel();
        // GridLayout con 0 righe (flessibili), 3 colonne, e spacing
        cardContainerPanel.setLayout(new GridLayout(0, 3, Costants.SPACING, Costants.SPACING));
        cardContainerPanel.setBackground(Costants.BACKGROUND_COLOR); // Sfondo del contenitore delle card
        cardContainerPanel.setBorder(new EmptyBorder(Costants.SPACING, 0, Costants.SPACING, 0)); // Padding verticale

        // Ottieni le visite per il mese e anno target direttamente dal controller
        List<Visita> visiteDelMese = visiteController.getVisite(meseAnno.atDay(1)); // Passa il primo giorno del mese target

        if (visiteDelMese.isEmpty()) {
            JLabel noVisiteLabel = new JLabel("Nessuna visita pianificata per questo mese.", SwingConstants.CENTER);
            noVisiteLabel.setFont(Costants.TITLE_FONT);
            cardContainerPanel.setLayout(new BorderLayout()); // Cambia layout per centrare il messaggio
            cardContainerPanel.add(noVisiteLabel, BorderLayout.CENTER);
        } else {
            for (Visita visita : visiteDelMese) {
                VisitaCard visitaCard = new VisitaCard(visita);
                cardContainerPanel.add(visitaCard);
            }
             // Aggiungi pannelli vuoti per riempire l'ultima riga se necessario
             int items = visiteDelMese.size();
             int cols = 3;
             int remainder = items % cols;
             if (remainder != 0) {
                 for (int i = 0; i < cols - remainder; i++) {
                     cardContainerPanel.add(new JPanel(){{setOpaque(false);}}); // Pannello trasparente vuoto
                 }
             }
        }

        // ScrollPane per le card
        JScrollPane scrollPane = new JScrollPane(cardContainerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Rimuovi bordo dello scrollpane
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Velocità scroll

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer con genera visite
        JPanel footerPanel = Costants.createFooterPanel(""); // Usa il metodo costante
        JButton generaVisiteButton = Costants.createSimpleButton("Genera Visite Prossimo Mese");
        generaVisiteButton.setFont(Costants.BUTTON_FONT); // Applica font costante
        generaVisiteButton.addActionListener(e -> {
            try {
                visiteController.generaVisite();
                JOptionPane.showMessageDialog(this, "Generazione visite completata.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                // Ricarica la pagina per vedere cambiamenti
                dispose();
                new Sezione1(visiteController, calendarioController).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore durante la generazione delle visite: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // Log dell'errore
            }
        });

        // Disabilita il bottone se non siamo nel giorno di generazione
        if (!calendarioController.isGiornoDiGenerazioneVisite()) {
            generaVisiteButton.setEnabled(false);
            // Tooltip aggiornato senza riferimento a getGiornoGenerazione()
            generaVisiteButton.setToolTipText("La generazione delle visite è possibile solo il primo giorno feriale a partire dal 16 del mese corrente.");
        }

        // Aggiungi un pannello per il bottone per allinearlo a destra nel footer
        JPanel footerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerButtonPanel.setOpaque(false); // Rendi trasparente
        footerButtonPanel.add(generaVisiteButton);
        footerPanel.add(footerButtonPanel, BorderLayout.EAST); // Aggiungi il pannello del bottone

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Gestione Visite Pianificate"); // Titolo più descrittivo
        setSize(1200, 800);
        setMinimumSize(new Dimension(800, 600)); // Dimensione minima
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiudi solo questa finestra, non l'app
    }
}
