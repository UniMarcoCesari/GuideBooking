package view.configuratore;

import controller.CalendarioController;
import controller.VisiteController;
import costants.Costants;
import model.Visita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import card.VisitaCard; // Assicurati che VisitaCard sia importata

public class GestioneVisite extends JFrame {
    private final YearMonth meseAnno; // Cambiato a YearMonth per chiarezza

    public GestioneVisite(VisiteController visiteController, CalendarioController calendarioController) {

        // Determina il mese e anno target (il prossimo mese rispetto alla data corrente del calendario)
        LocalDate dataCorrente = calendarioController.getDatacDateCorrenteLocalDate();
        this.meseAnno = YearMonth.from(dataCorrente.plusMonths(1));

        initializeFrame();

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);

        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ITALIAN);
        String yearMonthLabel = meseAnno.format(monthYearFormatter);
        // Titolo al centro
        JLabel titolo = new JLabel("Visite pianificate per "+ yearMonthLabel, SwingConstants.CENTER);
        titolo.setForeground(Color.WHITE);
        titolo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titolo, BorderLayout.CENTER);

        // Bottone Logout a destra
        JButton logoutButton = Costants.creaBottoneLogOut();
        logoutButton.addActionListener(e -> {
            dispose();
            new view.configuratore.PannelloConfiguratore().setVisible(true);
        });
        
        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRightPanel.setOpaque(false);
        headerRightPanel.add(logoutButton);
        headerPanel.add(headerRightPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        

        // Contenuto principale con ScrollPane e BoxLayout
        JPanel cardContainerPanel = new JPanel();
        cardContainerPanel.setLayout(new BoxLayout(cardContainerPanel, BoxLayout.Y_AXIS));
        cardContainerPanel.setBackground(Costants.BACKGROUND_COLOR); // Sfondo del contenitore delle card
        cardContainerPanel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING)); // Padding

        // Ottieni le visite per il mese e anno target direttamente dal controller
        List<Visita> visiteDelMese = visiteController.getVisite(meseAnno.atDay(1)); // Passa il primo giorno del mese target

        if (visiteDelMese.isEmpty()) {
            JLabel noVisiteLabel = new JLabel("Nessuna visita pianificata per questo mese.", SwingConstants.CENTER);
            noVisiteLabel.setFont(Costants.TITLE_FONT);
            cardContainerPanel.setLayout(new BorderLayout()); // Cambia layout per centrare il messaggio
            cardContainerPanel.add(noVisiteLabel, BorderLayout.CENTER);
        } else {
            // Pannello per il carosello
            JPanel carouselPanel = new JPanel();
            carouselPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Spaziatura tra le card
            carouselPanel.setBackground(Costants.BACKGROUND_COLOR);

            for (Visita visita : visiteDelMese) {
                VisitaCard visitaCard = new VisitaCard(visita);
                // Imposta una dimensione fissa per tutte le card
                visitaCard.setPreferredSize(new Dimension(400, 200)); // Larghezza fissa, altezza preferita
                carouselPanel.add(visitaCard);
            }

            // ScrollPane per il carosello
            JScrollPane scrollPane = new JScrollPane(carouselPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        // Footer con genera visite
        JPanel footerPanel = Costants.createFooterPanel(""); // Usa il metodo costante
        footerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);
        JButton generaVisiteButton = Costants.createSimpleButton("Genera Visite");
        generaVisiteButton.setFont(Costants.BUTTON_FONT); // Applica font costante
        generaVisiteButton.addActionListener(e -> {
            try {
                visiteController.generaVisite();
                // Ricarica la pagina per vedere cambiamenti
                dispose();
                new GestioneVisite(visiteController, calendarioController).setVisible(true);
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
