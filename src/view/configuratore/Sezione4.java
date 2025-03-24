package view.configuratore;

import card.LuogoCard;
import controller.CalendarioController;
import controller.LuoghiController;
import costants.Costants;
import model.Luogo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class Sezione4 extends JFrame {
    private JTextField textArea;
    private JPanel listPanel;
    private LuoghiController luoghiController;  // Controller for fetching the places

    public Sezione4(LuoghiController luoghiController) {
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

        aggiornaListaLuoghi();  // Update the list of places when the frame is displayed
    }

    public void aggiornaListaLuoghi() {
        listPanel.removeAll();  // Clear the current list of places
        List<Luogo> luoghi = luoghiController.getLuoghi();  // Fetch the list of places

        if (luoghi.isEmpty()) {
            JLabel emptyLabel = new JLabel("Nessun luogo disponibile.", SwingConstants.CENTER);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
        } else {
            luoghi.forEach(this::addLuogoCard);  // Add each place as a card
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

    private void addLuogoCard(Luogo luogo) {
        listPanel.add(Box.createVerticalStrut(6));
        listPanel.add(new LuogoCard(luogo, luoghiController));  // Add a card for each place
    }

    private void initializeFrame() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
