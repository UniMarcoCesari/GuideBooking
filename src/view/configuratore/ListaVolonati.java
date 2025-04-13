package view.configuratore;
import card.VolontarioCard;
import controller.LuoghiController;
import costants.Costants;
import model.Luogo;
import model.TipoVisita;
import model.Volontario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaVolonati extends JFrame {
    private JTextField textArea;
    private JPanel listPanel;
    private LuoghiController luoghiController;

    public ListaVolonati(LuoghiController luoghiController) {
        this.luoghiController = luoghiController;
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

        // Contenuto principale
        JPanel mainContentPanel = new JPanel();
        listPanel = createListPanel();
        mainContentPanel.add(listPanel);  // Add the listPanel to mainContentPanel
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        // Footer (se necessario)
        JPanel footerPanel = Costants.createFooterPanel("Footer");
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        aggiornaListaVolontari();  // Update the list of places when the frame is displayed
    }

    public void aggiornaListaVolontari() {
        listPanel.removeAll();  // Pulisce la lista attuale

        List<Luogo> luoghi = luoghiController.getLuoghi();  // Ottiene i luoghi
        HashMap<Volontario, List<TipoVisita>> mappaVolontari = new HashMap<>();

        // Costruisce la mappa Volontario -> Lista di TipoVisita
        for (Luogo luogo : luoghi) {
            for (TipoVisita tipoVisita : luogo.getTipiVisita()) {
                for (Volontario volontario : tipoVisita.getVolontari()) {
                    // Se il volontario è già presente nella mappa, aggiunge il nuovo tipo di visita
                    mappaVolontari.computeIfAbsent(volontario, k -> new ArrayList<>()).add(tipoVisita);
                }
            }
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
        listPanel.add(new VolontarioCard(volontario, tipiVisita, luoghiController));
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

}
