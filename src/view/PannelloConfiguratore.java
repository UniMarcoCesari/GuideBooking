package view;

import costants.Costants;
import model.Calendario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PannelloConfiguratore extends JFrame {

    private final Calendario calendario;
    private JTextField textArea;
    private JPanel mainContentPanel;
    private JButton button1, button2, button3;

    public PannelloConfiguratore() {
        initializeFrame();
        calendario = new Calendario();

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Pannello configuratore");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale
        mainContentPanel = createMainContentPanel();
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

    private JPanel createMainContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Costants.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;

        // Pannello superiore (Data + Bottoni)
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        textArea = new JTextField(calendario.getDataString(), 20);
        textArea.setHorizontalAlignment(JTextField.CENTER);
        textArea.setEditable(false);

        JButton indietroBtn = new JButton("<");
        indietroBtn.addActionListener(e -> aggiornaData(-1));

        JButton avantiBtn = new JButton(">");
        avantiBtn.addActionListener(e -> aggiornaData(1));

        topPanel.add(indietroBtn, BorderLayout.WEST);
        topPanel.add(textArea, BorderLayout.CENTER);
        topPanel.add(avantiBtn, BorderLayout.EAST);

        contentPanel.add(topPanel, gbc);

        // Pannello inferiore (Bottoni equidistanti)
        gbc.gridy = 1;
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 20, 0));

        // Bottoni personalizzati
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();

        aggiornaBottoni();

        bottomPanel.add(button1);
        bottomPanel.add(button2);
        bottomPanel.add(button3);

        contentPanel.add(bottomPanel, gbc);

        return contentPanel;
    }

    private void aggiornaData(int giorni) {
        if (giorni < 0) {
            calendario.indietroUnGiorno();
        } else {
            calendario.avantiUnGiorno();
        }
        textArea.setText(calendario.getDataString());
        aggiornaBottoni();
        revalidate();
        repaint();
    }

    private void aggiornaBottoni() {
        button1.setText("Modifiche per " + calendario.getNomeMese());
        button2.setText("Modifiche per " + calendario.getNomeMesePiu(1));
        button3.setText("Modifiche per " + calendario.getNomeMesePiu(2));
    }

    public static void main(String[] args) {
        new PannelloConfiguratore();
    }
}