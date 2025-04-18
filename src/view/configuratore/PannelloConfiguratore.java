package view.configuratore;

import controller.CalendarioController;
import controller.LuoghiController;
import controller.TipiVisitaController;
import controller.VolontariController;
import costants.Costants;
import model.CorpoDati;
import service.DataManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.Locale;

public class PannelloConfiguratore extends JFrame {
    private final CalendarioController calendarioController;
    private final LuoghiController luoghiController;
    private final TipiVisitaController tipoVisitaController;
    private final VolontariController volontariController;
    private JTextField textArea;
    private JButton button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private CorpoDati corpoDati;

    public PannelloConfiguratore() {
        initializeFrame();

        //inizializziamo controller qui
        calendarioController = new CalendarioController();
        this.luoghiController = new LuoghiController();
        this.tipoVisitaController = new TipiVisitaController();
        this.volontariController = new VolontariController();
        this.corpoDati = DataManager.caricaCorpoDati(Costants.file_corpo);

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Pannello configuratore - "+corpoDati.getAmbito() );
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

        button1 = Costants.createSimpleButton("Sezione 1");
        button2 = Costants.createSimpleButton("Sezione 2");
        button3 = Costants.createSimpleButton("Sezione 3");

        button1.addActionListener(e -> {
            dispose();
            new Sezione1(calendarioController);
        });
        button3.addActionListener(e -> {
            dispose();
            new DatePrecluseSezione(calendarioController);
        });

        aggiornaBottoni();

        bottomPanel.add(button1);
        bottomPanel.add(button2);
        bottomPanel.add(button3);

        contentPanel.add(bottomPanel, gbc);

        // **Secondo Pannello per altri tre bottoni**
        gbc.gridy = 2;  // Posiziona sotto il primo set di bottoni
        JPanel secondBottomPanel = new JPanel(new GridLayout(1, 3, 20, 0));

        button4 = Costants.createSimpleButton("LISTA LUOGHI");
        button5 = Costants.createSimpleButton("LISTA TIPI VISITA");
        button6 = Costants.createSimpleButton("LISTA VOLONTARI");

        button4.addActionListener(e -> {
            dispose();
            new ListaLuoghi(luoghiController,tipoVisitaController);
        });
        button5.addActionListener(e -> {
            dispose();
            new ListaTipiVisita(tipoVisitaController);
        });
        button6.addActionListener(e -> {
            dispose();
            new ListaVolontari(volontariController, tipoVisitaController);
        });

        secondBottomPanel.add(button4);
        secondBottomPanel.add(button5);
        secondBottomPanel.add(button6);

        contentPanel.add(secondBottomPanel, gbc);

        // **Terzo Pannello per altri tre bottoni**
        gbc.gridy = 3;  // Posiziona sotto il secondo set di bottoni
        JPanel thirdBottomPanel = new JPanel(new GridLayout(1, 3, 20, 0));

        button7 = Costants.createSimpleButton("NUM MAX");
        button8 = Costants.createSimpleButton("MODIFICHE 2");
        button9 = Costants.createSimpleButton("MODIFICHE 3");

        button7.addActionListener(e -> {
            dispose();
            new NumMax();
        });
        button8.addActionListener(e -> {
            dispose();
            //new Sezione8(calendarioController);
        });
        button9.addActionListener(e -> {
            dispose();
            //new Sezione9(calendarioController);
        });

        thirdBottomPanel.add(button7);
        thirdBottomPanel.add(button8);
        thirdBottomPanel.add(button9);

        contentPanel.add(thirdBottomPanel, gbc);

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
        LocalDate firstModifiableMonth = calendarioController.getNomeMesePrimoCheSiPuoModificare();
        if (calendarioController.isButtonLocked()) {
            button1.setEnabled(false);
        } else {
            button1.setEnabled(true);
        }

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ITALIAN);

        button1.setText("Modifiche per " + firstModifiableMonth.format(formatter));
        button2.setText("Modifiche per " + firstModifiableMonth.plusMonths(1).format(formatter));
        button3.setText("Date precluse " + firstModifiableMonth.plusMonths(2).format(formatter)); // Changed to plusMonths(2) to reflect the user's request for a 3-month difference
    }

    public static void main(String[] args) {
        new PannelloConfiguratore();
    }
}
