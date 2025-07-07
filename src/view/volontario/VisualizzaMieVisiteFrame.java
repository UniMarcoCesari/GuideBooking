package view.volontario;

import controller.VisiteController;
import costants.Costants;
import model.Visita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.util.List;

import card.VisitaCard; // Assicurati che VisitaCard sia importata

public class VisualizzaMieVisiteFrame extends JFrame {

    public VisualizzaMieVisiteFrame(String username,VisiteController visiteController) {


        initializeFrame();

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);

       
        // Titolo al centro
        JLabel titolo = new JLabel("Le tue visite assegnate", SwingConstants.CENTER);
        titolo.setForeground(Color.WHITE);
        titolo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titolo, BorderLayout.CENTER);

        // Bottone Logout a destra
        JButton logoutButton = Costants.creaBottoneLogOut();
        logoutButton.addActionListener(e -> {
            dispose();
            new view.volontario.PannelloVolontario(username).setVisible(true);
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

        // Ottieni le visite username
        List<Visita> visiteDelMese = visiteController.getVisite(username); 

        if (visiteDelMese.isEmpty()) {
            JLabel noVisiteLabel = new JLabel("Nessuna visita pianificata per questo mese.", SwingConstants.CENTER);
            noVisiteLabel.setFont(Costants.TITLE_FONT);
            cardContainerPanel.setLayout(new BorderLayout()); // Cambia layout per centrare il messaggio
            cardContainerPanel.add(noVisiteLabel, BorderLayout.CENTER);
            mainPanel.add(cardContainerPanel, BorderLayout.CENTER); // Aggiungi il pannello con il messaggio se non ci sono visite
        } else {
            // Pannello per le card, disposto verticalmente
            JPanel cardListPanel = new JPanel();
            cardListPanel.setLayout(new BoxLayout(cardListPanel, BoxLayout.Y_AXIS)); // Layout verticale
            cardListPanel.setBackground(Costants.BACKGROUND_COLOR);
            // Aggiunge padding interno al pannello delle card
            cardListPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // 20 pixel di padding interno

            for (Visita visita : visiteDelMese) {
                VisitaCard visitaCard = new VisitaCard(visita, username);
                // Imposta una dimensione preferita per le card
                visitaCard.setPreferredSize(new Dimension(400, 200));
                visitaCard.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra le card orizzontalmente
                cardListPanel.add(visitaCard);
                cardListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spaziatura verticale
            }

            // ScrollPane per la lista di card
            JScrollPane scrollPane = new JScrollPane(cardListPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // No scroll orizzontale
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Scroll verticale se necessario
            // Rimuove il bordo dallo JScrollPane per avere la scrollbar attaccata a destra
            scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Nessun bordo esterno
            scrollPane.getViewport().setBackground(Costants.BACKGROUND_COLOR); // Sfondo del viewport

            mainPanel.add(scrollPane, BorderLayout.CENTER); // Aggiungi lo scrollPane con le card
        }

        // Footer con genera visite
        JPanel footerPanel = Costants.createFooterPanel(""); // Usa il metodo costante
        footerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);
        

       

        // Aggiungi un pannello per il bottone per allinearlo a destra nel footer
        JPanel footerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerButtonPanel.setOpaque(false); // Rendi trasparente
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
