package view.configuratore;

import controller.CalendarioController;
import costants.Costants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PannelloConfiguratore extends JFrame {
    private final CalendarioController calendarioController;
    private JTextField textArea;
    private JButton button1, button2, button3;

    public PannelloConfiguratore() {
        initializeFrame();

        // Definiamo alcuni giorni festivi di esempio
        Set<LocalDate> giorniFestivi = new HashSet<>();
        giorniFestivi.add(LocalDate.of(2025, 2, 15));  // 15 febbraio 2025
        giorniFestivi.add(LocalDate.of(2025, 2, 16)); //  16 febbraio 2025

        //inizializiamo controller qui
        calendarioController = new CalendarioController(giorniFestivi);


        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Pannello configuratore");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale
        JPanel mainContentPanel = createMainContentPanel();
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        // Footer
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

    //IDEA PER SALVARE STATO APP
    @Override
    public void dispose() {
        super.dispose();
    }


    //Pannello centrale
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
        textArea = new JTextField(calendarioController.getDataCorrente(), 20);
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

        button1 = Costants.createSimpleButton("");
        button2 = Costants.createSimpleButton("");
        button3 = Costants.createSimpleButton("");

        button1.addActionListener(e -> {
            dispose();
            new Sezione1(calendarioController);
        });
        button3.addActionListener(e -> {
            dispose();
            new Sezione3(calendarioController);
        });

        aggiornaBottoni();

        bottomPanel.add(button1);
        bottomPanel.add(button2);
        bottomPanel.add(button3);

        contentPanel.add(bottomPanel, gbc);

        return contentPanel;
    }

    private void aggiornaData(int giorni) {
        if (giorni < 0) {
            calendarioController.indietroUnGiorno();
        } else {
            calendarioController.avantiUnGiorno();
        }
        textArea.setText(calendarioController.getDataCorrente());
        aggiornaBottoni();
        revalidate();
        repaint();
    }

    private void aggiornaBottoni() {
        button1.setText("Modifiche per " + calendarioController.getNomeMesePrimoCheSiPuoModificare());
        button2.setText("Modifiche per " + calendarioController.getNomeMesePrimoCheSiPuoModificare().plus(1));
        button3.setText("Modifiche per " + calendarioController.getNomeMesePrimoCheSiPuoModificare().plus(2));
    }


    public static void main(String[] args) {
        new PannelloConfiguratore();
    }
}
