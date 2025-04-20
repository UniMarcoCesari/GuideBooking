package view.volontario;

import controller.VolontariController;
import costants.Costants;
import model.TipoVisita;
import model.Volontario;
import view.PannelloVolontario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VisualizzaTipiVisitaVolontarioFrame extends JFrame {

    private String username;
    private JPanel listPanel;
    private VolontariController volontariController;
    private controller.TipiVisitaController tipiVisitaController;

    public VisualizzaTipiVisitaVolontarioFrame(String username, controller.TipiVisitaController tipiVisitaController) {
        this.username = username;
        this.volontariController = new VolontariController();
        this.tipiVisitaController = tipiVisitaController;

        initializeFrame();

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Tipi di Visita Associati - " + username);
        headerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK); // Colore specifico per volontario

        // Pulsante indietro
        JButton indietroButton = Costants.createSimpleButton("Indietro");
        indietroButton.addActionListener(_ -> {
            dispose();
            new PannelloVolontario(username).setVisible(true);
        });
        headerPanel.add(indietroButton, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Costants.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING));

        // Pannello per la lista dei tipi di visita
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Costants.BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Costants.BORDER_COLOR));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = Costants.createFooterPanel("");
        footerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK); // Colore specifico per volontario
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Carica e visualizza i tipi di visita associati al volontario
        caricaTipiVisitaAssociati();

        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Visualizza Tipi Visita Associati - " + username);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void caricaTipiVisitaAssociati() {
        
        List<TipoVisita> tipiVisitaAssociati = tipiVisitaController.getTipiVisitaPerVolontario(username);

    
        // Visualizza i tipi di visita trovati
        if (tipiVisitaAssociati.isEmpty()) {
            JLabel noVisiteLabel = new JLabel("Non sei associato a nessun tipo di visita");
            noVisiteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(noVisiteLabel);
        } else {
            for (TipoVisita tipoVisita : tipiVisitaAssociati) {
                aggiungiTipoVisitaPanel(tipoVisita);
            }
        }

        // Aggiorna l'interfaccia
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void aggiungiTipoVisitaPanel(TipoVisita tipoVisita) {
        // Crea un pannello per il tipo di visita
        JPanel tipoVisitaPanel = new JPanel();
        tipoVisitaPanel.setLayout(new BoxLayout(tipoVisitaPanel, BoxLayout.Y_AXIS));
        tipoVisitaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Costants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        tipoVisitaPanel.setBackground(Color.WHITE);
        tipoVisitaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        tipoVisitaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Titolo
        JLabel titoloLabel = new JLabel(tipoVisita.getTitolo());
        titoloLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titoloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tipoVisitaPanel.add(titoloLabel);

        // Spazio
        tipoVisitaPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Descrizione
        JLabel descrizioneLabel = new JLabel("Descrizione: " + tipoVisita.getDescrizione());
        descrizioneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tipoVisitaPanel.add(descrizioneLabel);

        // Punto di incontro
        JLabel puntoIncontroLabel = new JLabel("Punto di incontro: " + tipoVisita.getPuntoIncontro());
        puntoIncontroLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tipoVisitaPanel.add(puntoIncontroLabel);

        // Date
        JLabel dateLabel = new JLabel("Periodo: " + tipoVisita.getDataInizio() + " - " + tipoVisita.getDataFine());
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tipoVisitaPanel.add(dateLabel);

        // Giorni della settimana
        JLabel giorniLabel = new JLabel("Giorni: " + String.join(", ", tipoVisita.getGiorniSettimana()));
        giorniLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tipoVisitaPanel.add(giorniLabel);

        // Orario e durata
        JLabel orarioLabel = new JLabel("Orario: " + tipoVisita.getOraInizio() + " (durata: " + tipoVisita.getDurataMinuti() + " minuti)");
        orarioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tipoVisitaPanel.add(orarioLabel);

        // Biglietto
        JLabel bigliettoLabel = new JLabel("Biglietto necessario: " + (tipoVisita.isBigliettoNecessario() ? "Sì" : "No"));
        bigliettoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tipoVisitaPanel.add(bigliettoLabel);

        // Partecipanti
        JLabel partecipantiLabel = new JLabel("Partecipanti: min " + tipoVisita.getMinPartecipanti() + ", max " + tipoVisita.getMaxPartecipanti());
        partecipantiLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tipoVisitaPanel.add(partecipantiLabel);

        // Aggiungi il pannello alla lista
        listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        listPanel.add(tipoVisitaPanel);
    }


}
