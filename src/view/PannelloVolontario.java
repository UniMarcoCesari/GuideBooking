package view;

import controller.CalendarioController;
import costants.Costants;
import model.CorpoDati;
import service.DataManager;
import view.volontario.GestisciDisponibilitaFrame; // Import new frame
import view.volontario.VisualizzaTipiVisitaVolontarioFrame; // Import new frame

import controller.TipiVisitaController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class PannelloVolontario extends JFrame {
    private final CalendarioController calendarioController;
    private JTextField textArea;
    private JButton button1, button2;
    private CorpoDati corpoDati;
    private String username; // Add username field
    private TipiVisitaController tipiVisitaController;

    public PannelloVolontario(String username) { // Update constructor
        this.username = username; // Store username
        this.tipiVisitaController = new TipiVisitaController();
        initializeFrame();

        //inizializziamo controller qui
        calendarioController = new CalendarioController();
        this.corpoDati = DataManager.caricaCorpoDati(Costants.file_corpo);

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Pannello Volontario - " + corpoDati.getAmbito());
        headerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK); // Set specific color for volunteer panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale
        JPanel mainContentPanel = createMainContentPanel();
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = Costants.createFooterPanel("Footer");
        footerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK); // Set specific color for volunteer panel footer
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeFrame() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //IDEA PER SALVARE STATO APP
    @Override
    public void dispose() {
        super.dispose();
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
        textArea = new JTextField(calendarioController.getDataCorrente(), 20);
        textArea.setHorizontalAlignment(JTextField.CENTER);
        textArea.setEditable(false);

        JButton indietroBtn = new JButton("<");
        indietroBtn.addActionListener(_ -> aggiornaData(-1));

        JButton avantiBtn = new JButton(">");
        avantiBtn.addActionListener(_ -> aggiornaData(1));

        topPanel.add(indietroBtn, BorderLayout.WEST);
        topPanel.add(textArea, BorderLayout.CENTER);
        topPanel.add(avantiBtn, BorderLayout.EAST);

        contentPanel.add(topPanel, gbc);

        // Pannello inferiore (Bottoni equidistanti)
        gbc.gridy = 1;
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // Changed columns to 2

        button1 = Costants.createSimpleButton("Visualizza Tipi Visita"); // Changed text
        button2 = Costants.createSimpleButton("Gestisci Disponibilità"); // Changed text
        // button3 = Costants.createSimpleButton("Altro"); // Removed button3

        button1.addActionListener(_ -> {
            dispose();
            new VisualizzaTipiVisitaVolontarioFrame(this.username, this.tipiVisitaController).setVisible(true); // Open frame
        });
        button2.addActionListener(_ -> {
            dispose();
            new GestisciDisponibilitaFrame(this.username).setVisible(true); // Open frame
        });

        bottomPanel.add(button1);
        bottomPanel.add(button2);

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
        revalidate();
        repaint();
    }

   
}
