package view.configuratore;

import card.LuogoCard;
import card.TipoVisitaCard;
import controller.CalendarioController;
import controller.LuoghiController;
import costants.Costants;
import model.Luogo;
import model.TipoVisita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class Sezione5 extends JFrame {
    private JTextField textArea;
    private JPanel listPanel;
    private LuoghiController luoghiController;

    public Sezione5(LuoghiController luoghiController) {
        this.luoghiController = luoghiController;
        initializeFrame();
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Lista luoghi");
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

        aggiornaListaTipiVisita();  // Update the list of places when the frame is displayed
    }

    public void aggiornaListaTipiVisita() {
        listPanel.removeAll();  // Clear the current list of places
        List<Luogo> luoghi = luoghiController.getLuoghi();  // Fetch the list of places

        for (Luogo luogo : luoghi) {
            for (TipoVisita tipoVisita :luogo.getTipiVisita())
            {
                addTipoVisitaCard(tipoVisita);
            }
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
        listPanel.add(new TipoVisitaCard(tipoVisita, luoghiController));  // Add a card for each place
    }

    private void initializeFrame() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
