package view.configuratore;

import controller.CalendarioController;
import costants.Costants;
import model.Calendario;

import javax.swing.*;
import java.awt.*;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class Sezione3 extends JFrame {
    private final Month mese;
    private JTextField textArea;

    public Sezione3(CalendarioController calendarioController) {
        initializeFrame();
        mese = calendarioController.getNomeMesePrimoCheSiPuoModificare().plus(+2); //prende mese
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Cose che puoi fare nel mese di " + mese.getDisplayName(TextStyle.FULL, Locale.ITALIAN));
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale
        JPanel mainContentPanel = new JPanel();

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