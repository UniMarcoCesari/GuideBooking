package view.configuratore;

import controller.CalendarioController;
import controller.LuoghiController;
import controller.TipiVisitaController;
import controller.VisiteController;
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
    private final VisiteController visiteController;
    private JTextField textArea;
    private JButton button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private CorpoDati corpoDati;

    public PannelloConfiguratore() {
        initializeFrame();

        //inizializziamo controller qui
        this.calendarioController = new CalendarioController();
        this.luoghiController = new LuoghiController();
        this.tipoVisitaController = new TipiVisitaController();
        this.volontariController = new VolontariController();
        this.corpoDati = DataManager.caricaCorpoDati(Costants.file_corpo);

        //inizializziamo il controller delle visite
        this.visiteController = new VisiteController(calendarioController, luoghiController, tipoVisitaController, volontariController);

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);

        // Titolo al centro
        JLabel titolo = new JLabel("Pannello Configuaratore", SwingConstants.CENTER);
        titolo.setForeground(Color.WHITE);
        titolo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titolo, BorderLayout.CENTER);

        // Bottone Logout a destra
        JButton logoutButton = Costants.creaBottoneLogOut();
        logoutButton.addActionListener(e -> {
            dispose();
            new view.login.LoginFrame().setVisible(true);
        });
        
        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRightPanel.setOpaque(false);
        headerRightPanel.add(logoutButton);
        headerPanel.add(headerRightPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        // Contenuto principale
        JPanel mainContentPanel = createMainContentPanel();
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = Costants.createFooterPanel("");
        footerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);
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
        topPanel.setBackground(Costants.BACKGROUND_COLOR);
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
        JPanel firstRowPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        firstRowPanel.setBackground(Costants.BACKGROUND_COLOR);

        button1 = Costants.createSimpleButton("Gestione Visite");
        button3 = Costants.createSimpleButton("Date Precluse");
        button4 = Costants.createSimpleButton("Gestione Luoghi");

        button1.addActionListener(e -> {
            dispose();
            new GestioneVisite(visiteController,calendarioController);
        });
        button3.addActionListener(e -> {
            dispose();
            new DatePrecluseSezione(calendarioController);
        });
        button4.addActionListener(e -> {
            dispose();
            new ListaLuoghi(luoghiController,tipoVisitaController,calendarioController);
        });

        firstRowPanel.add(button1);
        firstRowPanel.add(button3);
        firstRowPanel.add(button4);

        contentPanel.add(firstRowPanel, gbc);

        // **Secondo Pannello per altri tre bottoni**
        gbc.gridy = 2;  // Posiziona sotto il primo set di bottoni
        JPanel secondRowPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        secondRowPanel.setBackground(Costants.BACKGROUND_COLOR);

        button5 = Costants.createSimpleButton("Gestione tipi visita");
        button6 = Costants.createSimpleButton("Gestione volontari");
        button7 = Costants.createSimpleButton("Numero massimo iscrizioni");

        button5.addActionListener(e -> {
            dispose();
            new ListaTipiVisita(tipoVisitaController,visiteController,calendarioController);
        });
        button6.addActionListener(e -> {
            dispose();
            new ListaVolontari(volontariController, tipoVisitaController,calendarioController);
        });
        button7.addActionListener(e -> {
            dispose();
            new NumMax();
        });

        secondRowPanel.add(button5);
        secondRowPanel.add(button6);
        secondRowPanel.add(button7);

        contentPanel.add(secondRowPanel, gbc);

        return contentPanel;
    }

    private void aggiornaData(int giorni) {
        if (giorni < 0) {
            calendarioController.indietroUnGiorno();
        } else {
            calendarioController.avantiUnGiorno();
            visiteController.aggiornaVisiteAlCambioGiorno(calendarioController.getDatacDateCorrenteLocalDate());
        }
        textArea.setText(calendarioController.getDataCorrente());
        aggiornaBottoni();
        revalidate();
        repaint();
    }

    private void aggiornaBottoni() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ITALIAN);

        LocalDate firstModifiableMonth = calendarioController.getNomeMesePrimoCheSiPuoModificare();
               
        button1.setText("Visite per " + firstModifiableMonth.format(formatter));
        //button2.setText("Modifiche per " + firstModifiableMonth.plusMonths(1).format(formatter));
        button3.setText("Date precluse " + firstModifiableMonth.plusMonths(2).format(formatter));
    }

    // public static void main(String[] args) {
    //     new PannelloConfiguratore();
    // }
}
