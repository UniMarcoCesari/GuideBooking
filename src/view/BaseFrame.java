package view;

import costants.Costants;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BaseFrame extends JFrame {

    public BaseFrame(String title) {
        // Inizializza la finestra
        initializeFrame(title);

        // Crea il pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Aggiungi l'header
        JPanel headerPanel = Costants.createHeaderPanel(title);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Aggiungi il contenuto principale
        JPanel contentPanel = createMainContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Aggiungi il footer (se necessario)
        JPanel footerPanel = Costants.createFooterPanel("Footer");
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeFrame(String title) {
        setTitle(title);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel createMainContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Costants.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING));
        // Aggiungi i componenti al contentPanel qui (come campi di input, bottoni, ecc.)
        return contentPanel;
    }


    public static void main(String[] args) {
        new BaseFrame("My Application");
    }
}
