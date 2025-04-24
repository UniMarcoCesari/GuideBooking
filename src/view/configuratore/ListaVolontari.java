package view.configuratore;
import card.VolontarioCard;
import controller.TipiVisitaController;
import controller.VisiteController;
import controller.VolontariController;
import costants.Costants;
import model.TipoVisita;
import model.Volontario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaVolontari extends JFrame {
    private JPanel listPanel;
    private VolontariController volontariController;
    private TipiVisitaController tipiVisitaController;
    private VisiteController visiteController;

    public ListaVolontari(VolontariController volontariController, TipiVisitaController tipiVisitaController, VisiteController visiteController) {
        this.volontariController = volontariController;
        this.tipiVisitaController = tipiVisitaController;
        this.visiteController = visiteController;
        initializeFrame();
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Lista volontari");
        JButton indietro = Costants.createSimpleButton("Indietro");
        indietro.addActionListener(e -> {
            dispose();
            new PannelloConfiguratore().setVisible(true);
        });
        headerPanel.add(indietro, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale con scroll
        listPanel = createListPanel();
        JScrollPane scrollPane = new JScrollPane(listPanel); // Wrap listPanel in a JScrollPane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Show scrollbar only when needed
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrollbar
        scrollPane.setBorder(null); // Remove default border of scroll pane
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Improve scroll speed

        // Use a panel with BorderLayout to better manage the scroll pane
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(Costants.BACKGROUND_COLOR); // Match background
        mainContentPanel.add(scrollPane, BorderLayout.CENTER); // Add scrollPane instead of listPanel directly
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);


        // Footer
        JPanel footerPanel = Costants.createFooterPanel(""); // Clear default text
        JButton aggiungiButton = Costants.createSimpleButton("+"); // Use "+" symbol
        aggiungiButton.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Make button bigger
        aggiungiButton.setToolTipText("Aggiungi nuovo volontario");
        aggiungiButton.addActionListener(e -> aggiungiNuovoVolontario());
        footerPanel.add(aggiungiButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        aggiornaListaVolontari();  // Update the list of places when the frame is displayed
    }

    public void aggiornaListaVolontari() {
        listPanel.removeAll();  // Pulisce la lista attuale
        
        HashMap<Volontario, List<TipoVisita>> mappaVolontari = new HashMap<>();
        
        // Inizializza la mappa con tutti i volontari dal controller
        for (Volontario volontario : volontariController.getListaVolontari()) {
            ArrayList<TipoVisita> listaTipiVisita = new ArrayList<>();

            for (TipoVisita tipoVisita : tipiVisitaController.getTipiVisita()) {
                for (Volontario v : tipoVisita.getVolontari()) {
                    if (v.getNome().equals(volontario.getNome())) {
                        listaTipiVisita.add(tipoVisita);
                    }
                }
            }

            mappaVolontari.put(volontario, listaTipiVisita);
        }
        
        
        
        // Aggiunge le card per ogni volontario con la lista dei suoi tipi di visita
        for (Map.Entry<Volontario, List<TipoVisita>> entry : mappaVolontari.entrySet()) {
            addVolontarioCard(entry.getKey(), entry.getValue());
        }
        
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void addVolontarioCard(Volontario volontario, List<TipoVisita> tipiVisita) {
        listPanel.add(Box.createVerticalStrut(6));
        listPanel.add(new VolontarioCard(volontario, tipiVisita, volontariController,tipiVisitaController,visiteController, this));
    }


    private JPanel createListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING));
        return panel;
    }

    private void initializeFrame() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void aggiungiNuovoVolontario() {
        String username = JOptionPane.showInputDialog(this, "Inserisci l'username del nuovo volontario:", "Aggiungi Volontario", JOptionPane.PLAIN_MESSAGE);

        if (username != null && !username.trim().isEmpty()) {
            username = username.trim();
            // Call the controller method to add the volunteer
            boolean aggiunto = volontariController.aggiungiVolontario(username); // Pass 'this' for error dialog parent
            if (aggiunto) {
                aggiornaListaVolontari(); // Refresh the list if added successfully
                JOptionPane.showMessageDialog(this, "Volontario '" + username + "' aggiunto con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile aggiungere il volontario '" + username + "'.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else if (username != null) {
            // User entered empty or whitespace only
            JOptionPane.showMessageDialog(this, "L'username non può essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
        // If username is null, the user cancelled the dialog, do nothing.
    }
}
