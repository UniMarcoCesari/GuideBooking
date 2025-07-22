package view.volontario;

import controller.VisiteController;
import costants.Costants;
import model.Visita;
import view.login.MainController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.util.List;

import card.VisitaCard; 

public class VisualizzaMieVisiteFrame extends JFrame {

    // Controller per le visite
    private VisiteController visiteController;

    public VisualizzaMieVisiteFrame(String username,MainController mainController) {
        this.visiteController = mainController.getVisiteController();

        initializeFrame();

        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);

        JLabel titolo = new JLabel("Le tue visite assegnate", SwingConstants.CENTER);
        titolo.setForeground(Color.WHITE);
        titolo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titolo, BorderLayout.CENTER);

        JButton logoutButton = Costants.creaBottoneLogOut();
        logoutButton.addActionListener(e -> {
            mainController.showPannelloVolontario(username);
        });

        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRightPanel.setOpaque(false);
        headerRightPanel.add(logoutButton);
        headerPanel.add(headerRightPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Pannello contenente le card delle visite
        JPanel cardContainerPanel = new JPanel();
        cardContainerPanel.setLayout(new BoxLayout(cardContainerPanel, BoxLayout.Y_AXIS));
        cardContainerPanel.setBackground(Costants.BACKGROUND_COLOR);
        cardContainerPanel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING));

        // Recupera le visite del mese
        List<Visita> visiteDelMese = visiteController.getVisite(username); 

        // Se non ci sono visite, mostra un messaggio
        if (visiteDelMese.isEmpty()) {
            JLabel noVisiteLabel = new JLabel("Nessuna visita pianificata per questo mese.", SwingConstants.CENTER);
            noVisiteLabel.setFont(Costants.TITLE_FONT);
            cardContainerPanel.setLayout(new BorderLayout()); 
            cardContainerPanel.add(noVisiteLabel, BorderLayout.CENTER);
            mainPanel.add(cardContainerPanel, BorderLayout.CENTER); 
        } else {
            // Crea un pannello per le card delle visite
            JPanel cardListPanel = new JPanel();
            cardListPanel.setLayout(new BoxLayout(cardListPanel, BoxLayout.Y_AXIS)); 
            cardListPanel.setBackground(Costants.BACKGROUND_COLOR);

            // Crea le card delle visite
            for (Visita visita : visiteDelMese) {
                VisitaCard visitaCard = new VisitaCard(mainController, visita, username);
                visitaCard.setPreferredSize(new Dimension(400, 200));
                visitaCard.setAlignmentX(Component.CENTER_ALIGNMENT); 
                cardListPanel.add(visitaCard);
                cardListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            // Aggiungi un pannello di scroll
            JScrollPane scrollPane = new JScrollPane(cardListPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
            scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
            scrollPane.getViewport().setBackground(Costants.BACKGROUND_COLOR); 

            mainPanel.add(scrollPane, BorderLayout.CENTER); 
        }

        // Footer
        JPanel footerPanel = Costants.createFooterPanel(""); 
        footerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);
        

        JPanel footerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerButtonPanel.setOpaque(false); 
        footerPanel.add(footerButtonPanel, BorderLayout.EAST); 

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Gestione Visite Pianificate"); 
        setSize(1200, 800);
        setMinimumSize(new Dimension(800, 600)); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    }
}

