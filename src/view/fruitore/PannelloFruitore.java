package view.fruitore;

import card.VisitaCard;
import controller.CalendarioController;
import controller.LuoghiController;
import controller.TipiVisitaController;
import controller.VisiteController;
import controller.VolontariController; 
import costants.Costants;
import model.Visita;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PannelloFruitore extends JFrame {

    private VisiteController visiteController; 
    private CalendarioController calendarioController = new CalendarioController();
    private LuoghiController luoghiController = new LuoghiController();
    private TipiVisitaController tipiVisitaController = new TipiVisitaController();
    private VolontariController volontariController = new VolontariController();

    private String username; 

    public PannelloFruitore(String username) {
        this.username = username;
        this.visiteController = new VisiteController(calendarioController, luoghiController, tipiVisitaController, volontariController); 


        initializeFrame();

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);
        //mainPanel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING));

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Pannello Fruitore - Visite Disponibili");
        // Potresti voler un colore diverso per l'header del fruitore
        headerPanel.setBackground(new Color(200, 220, 255)); // Esempio: Azzurro chiaro
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Pannello centrale con la lista di visite
        JPanel listaVisitePanel = createListaVisitePanel();
        JScrollPane scrollPane = new JScrollPane(listaVisitePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null); // Rimuove il bordo dello scrollpane
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer (opzionale)
        JPanel footerPanel = Costants.createFooterPanel("Benvenuto " + username);
        footerPanel.setBackground(new Color(200, 220, 255)); // Stesso colore dell'header
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Pannello Fruitore");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiude solo questa finestra
    }

    private JPanel createListaVisitePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Layout verticale
        panel.setBackground(Costants.BACKGROUND_COLOR);
        // Imposta l'allineamento X della lista stessa (utile se il wrapper è più largo)
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        // Recupera tutte le visite da VisiteController e filtra quelle pertinenti per un fruitore
        // Esempio: mostriamo solo visite CONFERMATE o COMPLETE
        List<Visita> visiteDisponibili = visiteController.getAllVisite();
               
        if (visiteDisponibili.isEmpty()) {
            panel.add(new JLabel("Nessuna visita disponibile al momento."));
        } else {
            for (Visita visita : visiteDisponibili) {
                VisitaCard card = new VisitaCard(visita, username);

                // Aggiungi l'ActionListener per il bottone Iscriviti
                card.addIscrivitiActionListener(e -> {
                    try {
                        // Ottenere il numero di partecipanti dal command dell'evento
                        int numeroPartecipanti = Integer.parseInt(e.getActionCommand());

                        // Chiamare il controller per gestire l'iscrizione
                        visiteController.iscriviFruitore(
                                visita,
                                username,
                                numeroPartecipanti
                        );

                        JOptionPane.showMessageDialog(
                                this,
                                "Iscrizione completata con successo per " + numeroPartecipanti + " partecipanti!",
                                "Iscrizione Confermata",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // Aggiorna la lista delle visite dopo l'iscrizione
                        // Chiudi il pannello corrente
                        dispose();

                        // Apri un nuovo pannello
                        SwingUtilities.invokeLater(() -> {
                            new PannelloFruitore(username).setVisible(true);
                        });

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Si è verificato un errore durante l'elaborazione della richiesta.",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } catch (Exception ex) {
                        // Gestisci altre possibili eccezioni
                        JOptionPane.showMessageDialog(
                                this,
                                "Errore: " + ex.getMessage(),
                                "Errore Iscrizione",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });

                // Aggiungi l'ActionListener per il bottone Disiscriviti
                card.addDisiscrivitiActionListener(e -> {
                    try {
                        // Chiamare il controller per disiscrivere l'utente
                        visiteController.disdiciIscrizione(
                                visita,
                                visita.getIscrizioni().stream()
                                        .filter(i -> i.getUsernameFruitore().equals(username))
                                        .findFirst().get().getCodicePrenotazione(),
                                username
                        );

                        JOptionPane.showMessageDialog(
                                this,
                                "Disiscrizione effettuata con successo!",
                                "Disiscrizione Confermata",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // Aggiorna la lista delle visite dopo la disiscrizione
                        // Chiudi il pannello corrente
                        dispose();

                        // Apri un nuovo pannello
                        SwingUtilities.invokeLater(() -> {
                            new PannelloFruitore(username).setVisible(true);
                        });

                    } catch (Exception ex) {
                        // Gestisci altre possibili eccezioni
                        JOptionPane.showMessageDialog(
                                this,
                                "Errore: " + ex.getMessage(),
                                "Errore Disiscrizione",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });

                panel.add(card);
                panel.add(Box.createVerticalStrut(Costants.SPACING)); // Spazio tra le card
            }
        }
        // Aggiungi uno spaziatore flessibile alla fine per spingere le card verso l'alto
        panel.add(Box.createVerticalGlue());

        // Crea un pannello wrapper con FlowLayout centrato
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Centrato, nessun gap
        wrapperPanel.setBackground(Costants.BACKGROUND_COLOR); // Imposta lo stesso sfondo
        wrapperPanel.add(panel); // Aggiungi il pannello con BoxLayout al wrapper

        return wrapperPanel; // Restituisci il wrapper
    }

    // Potrebbe servire un metodo per aggiornare la lista
    public void aggiornaListaVisite() {
        // Rimuovi il vecchio scrollPane e aggiungine uno nuovo con la lista aggiornata
        BorderLayout layout = (BorderLayout) getContentPane().getLayout();
        Component oldScrollPane = layout.getLayoutComponent(BorderLayout.CENTER);
        if (oldScrollPane instanceof JScrollPane) {
            remove(oldScrollPane);
        }

        // Il metodo createListaVisitePanel ora restituisce il wrapperPanel
        JPanel wrapperPanel = createListaVisitePanel();
        JScrollPane scrollPane = new JScrollPane(wrapperPanel); // Aggiungi il wrapper allo scrollpane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        String username = "fruitore"; // Replace with actual username if needed
        new PannelloFruitore(username).setVisible(true);
    });
}

}
