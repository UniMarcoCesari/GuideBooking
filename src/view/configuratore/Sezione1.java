package view.configuratore;

import costants.Costants;

import javax.swing.*;
import java.awt.*;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class Sezione1 extends JFrame {

    Month mese;
    private JTextField textArea;
    private final JPanel mainContentPanel;

    public Sezione1(Month month) {
        initializeFrame();
        mese = month;

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Cose che puoi fare nel mese di " + mese.getDisplayName(TextStyle.FULL, Locale.ITALIAN));
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale
        mainContentPanel = new JPanel();
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        // Footer (se necessario)
        JPanel footerPanel = Costants.createFooterPanel("Footer");
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeFrame() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        new PannelloConfiguratore();
    }
}