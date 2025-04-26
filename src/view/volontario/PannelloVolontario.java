package view.volontario;

import controller.CalendarioController;
import costants.Costants;
import model.CorpoDati;
import service.DataManager;
import controller.TipiVisitaController;
import controller.VisiteController;
import controller.LuoghiController;
import controller.VolontariController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PannelloVolontario extends JFrame {
    private final CalendarioController calendarioController;
    private JTextField textArea;
    private JButton button1, button2, button3;
    private CorpoDati corpoDati;
    private String username;
    private TipiVisitaController tipiVisitaController;
    private VisiteController visiteController;

    public PannelloVolontario(String username) {
        this.username = username;
        this.tipiVisitaController = new TipiVisitaController();
        initializeFrame();

        calendarioController = new CalendarioController();
        LuoghiController luoghiController = new LuoghiController();
        VolontariController volontariController = new VolontariController();
        this.visiteController = new VisiteController(calendarioController, luoghiController, tipiVisitaController, volontariController);
        this.corpoDati = DataManager.caricaCorpoDati(Costants.file_corpo);

        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);

        // Titolo al centro
        JLabel titolo = new JLabel("Pannello Volontario", SwingConstants.CENTER);
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
        JPanel footerPanel = Costants.createFooterPanel("Footer");
        footerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeFrame() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

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

        // Pannello superiore (Data + Bottoni avanti/indietro)
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        textArea = new JTextField(calendarioController.getDataCorrente(), 20);
        textArea.setHorizontalAlignment(JTextField.CENTER);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            textArea.getBorder(),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        JButton indietroBtn = new JButton("<");
        indietroBtn.setMargin(new Insets(0, 0, 0, 0));
        indietroBtn.setFocusPainted(false);
        indietroBtn.addActionListener(_ -> aggiornaData(-1));

        JButton avantiBtn = new JButton(">");
        avantiBtn.setMargin(new Insets(0, 0, 0, 0));
        avantiBtn.setFocusPainted(false);
        avantiBtn.addActionListener(_ -> aggiornaData(1));

        topPanel.add(indietroBtn, BorderLayout.WEST);
        topPanel.add(textArea, BorderLayout.CENTER);
        topPanel.add(avantiBtn, BorderLayout.EAST);

        contentPanel.add(topPanel, gbc);

        // Pannello inferiore (Bottoni)
        gbc.gridy = 1;
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 20, 0));

        button1 = Costants.createSimpleButton("Visualizza Tipi Visita");
        button2 = Costants.createSimpleButton("Gestisci Disponibilità");
        button3 = Costants.createSimpleButton("Visualizza Mie Visite");

        button1.addActionListener(_ -> {
            dispose();
            new VisualizzaTipiVisitaVolontarioFrame(this.username, this.tipiVisitaController).setVisible(true);
        });
        button2.addActionListener(_ -> {
            dispose();
            new GestisciDisponibilitaFrame(this.username).setVisible(true);
        });
        button3.addActionListener(_ -> {
            dispose();
            new VisualizzaMieVisiteFrame(this.username, this.visiteController).setVisible(true);
        });

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
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PannelloVolontario("marco").setVisible(true));
    }
}
