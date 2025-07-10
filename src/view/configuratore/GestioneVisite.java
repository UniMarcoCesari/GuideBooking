package view.configuratore;

import controller.CalendarioController;
import controller.VisiteController;
import costants.Costants;
import model.Visita;
import view.login.MainController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import card.VisitaCard;

public class GestioneVisite extends JFrame {
    private final YearMonth meseAnno; // Cambiato a YearMonth per chiarezza

    private final CalendarioController calendarioController;  
    private final VisiteController visiteController;

    public GestioneVisite(MainController mainController) {

        this.calendarioController = mainController.getCalendarioController();
        this.visiteController = mainController.getVisiteController();

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
        JLabel titolo = new JLabel("Visite pianificate", SwingConstants.CENTER);
        titolo.setForeground(Color.WHITE);
        titolo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titolo, BorderLayout.CENTER);

        // Bottone Logout a destra
        JButton logoutButton = Costants.creaBottoneLogOut();
        logoutButton.addActionListener(e -> {
            mainController.showPannelloConfiguratore();
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
        List<Visita> visiteDelMese = visiteController.getAllVisite(); // Passa il primo giorno del mese target

        if (visiteDelMese.isEmpty()) {
            JLabel noVisiteLabel = new JLabel("Nessuna visita pianificata per questo mese.", SwingConstants.CENTER);
            noVisiteLabel.setFont(Costants.TITLE_FONT);
            cardContainerPanel.setLayout(new BorderLayout()); // Cambia layout per centrare il messaggio
            cardContainerPanel.add(noVisiteLabel, BorderLayout.CENTER);
        } else {
            // Pannello per le card, disposto verticalmente
            JPanel cardListPanel = new JPanel();
            cardListPanel.setLayout(new BoxLayout(cardListPanel, BoxLayout.Y_AXIS)); // Layout verticale
            cardListPanel.setBackground(Costants.BACKGROUND_COLOR);
            // Aggiunge padding interno al pannello delle card invece che allo JScrollPane
            cardListPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // 20 pixel di padding interno

            for (Visita visita : visiteDelMese) {
                VisitaCard visitaCard = new VisitaCard(mainController, visita,null); // Passa il mainController e visita, senza username
                // Imposta una dimensione preferita per le card (BoxLayout rispetterà l'altezza preferita)
                // Potrebbe essere necessario aggiustare la larghezza o usaresetAlignmentX per centrare
                visitaCard.setPreferredSize(new Dimension(400, 200));
                visitaCard.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra le card orizzontalmente
                cardListPanel.add(visitaCard);
                cardListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Aggiunge spaziatura verticale tra le card
            }

            // ScrollPane per la lista di card
            JScrollPane scrollPane = new JScrollPane(cardListPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // No scroll orizzontale
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Scroll verticale se necessario
            // Rimuove il bordo dallo JScrollPane per avere la scrollbar attaccata a destra
            scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Nessun bordo esterno
            scrollPane.getViewport().setBackground(Costants.BACKGROUND_COLOR); // Assicura che lo sfondo del viewport sia coerente

            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        // Footer con genera visite
        JPanel footerPanel = Costants.createFooterPanel(""); // Usa il metodo costante
        footerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);
        JButton generaVisiteButton = Costants.createSimpleButton("Genera Visite");
        generaVisiteButton.setFont(Costants.BUTTON_FONT); // Applica font costante
        generaVisiteButton.setBackground(Costants.CONFIGURATORE_HEADER_BACK);
        generaVisiteButton.addActionListener(e -> {
            try {
                visiteController.generaVisite();
                // Ricarica la pagina per vedere cambiamenti
                mainController.showGestioneVisite();
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

        footerPanel.add(generaVisiteButton, BorderLayout.CENTER); // Aggiungi il bottone al centro

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
