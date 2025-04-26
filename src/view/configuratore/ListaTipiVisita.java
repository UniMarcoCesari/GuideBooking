package view.configuratore;

import card.TipoVisitaCard;
import controller.CalendarioController;
import controller.TipiVisitaController;
import controller.VisiteController;
import costants.Costants;
import model.TipoVisita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ListaTipiVisita extends JFrame {
    private JPanel listPanel;
    private TipiVisitaController tipiVisitaController;
    private CalendarioController calendarioController;

    public ListaTipiVisita(TipiVisitaController tipiVisitaController, VisiteController visiteController, CalendarioController calendarioController) {
        this.tipiVisitaController = tipiVisitaController;
        this.calendarioController = calendarioController;
        initializeFrame();
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);

        // Titolo al centro
        JLabel titolo = new JLabel("Lista tipi visita", SwingConstants.CENTER);
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

        // Contenuto principale
        listPanel = createListPanel();
        JScrollPane scrollPane = new JScrollPane(listPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = Costants.createFooterPanel(""); // Clear default text
        footerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);
        JButton aggiungiButton = Costants.createSimpleButton("Aggiungi");
        if(!calendarioController.isFaseModificabile())
        {
            aggiungiButton.setEnabled(false);
        }
        aggiungiButton.addActionListener(_ -> apriDialogAggiungiTipoVisita()); // Placeholder action
        footerPanel.add(aggiungiButton); // Add the button to the footer
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        aggiornaListaTipiVisita();  // Update the list of places when the frame is displayed
    }

    public void aggiornaListaTipiVisita() {
        listPanel.removeAll();  // Clear the current list of places
        List<TipoVisita> tipiVisita = tipiVisitaController.getTipiVisita();  // Fetch the list of places

        // Aggiungi i tipi di visita unici al pannello
        for (TipoVisita tipoVisita : tipiVisita) {
            addTipoVisitaCard(tipoVisita);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING));
        return panel;
    }

    private void addTipoVisitaCard(TipoVisita tipoVisita) {
        listPanel.add(Box.createVerticalStrut(6));
        // Instantiate and add the card for the given TipoVisita
        listPanel.add(new TipoVisitaCard(this,tipoVisita,tipiVisitaController,calendarioController));  // Add a card for each place
    }

    private void initializeFrame() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Placeholder method for the "Aggiungi" button action
    private void apriDialogAggiungiTipoVisita() {
        dispose();
        new NuovoTipoVisita(this, tipiVisitaController).setVisible(true);
    }

    public void rimuoviTipoVisita(TipoVisita tipoVisita) {
        tipiVisitaController.rimuoviTipoVisita(tipoVisita);
        aggiornaListaTipiVisita();
        dispose();
        new PannelloConfiguratore().setVisible(true);
    }
}
