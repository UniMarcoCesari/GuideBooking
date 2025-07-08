package view.configuratore;

import costants.Costants;
import model.CorpoDati;
import service.PersistentDataManager;
import view.login.MainController;

import javax.swing.*;
import java.awt.*;

public class NumMax extends JFrame {
    private CorpoDati corpoDati; 
    private MainController mainController;

    public NumMax(MainController mainController) {
        this.mainController = mainController;
        initializeFrame();
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Inizializza corpoDati in modo sicuro
        initializeCorpoDati();

      // Header
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);

      // Titolo al centro
      JLabel titolo = new JLabel("Numero massimo di persone", SwingConstants.CENTER);
      titolo.setForeground(Color.WHITE);
      titolo.setFont(new Font("Arial", Font.BOLD, 20));
      headerPanel.add(titolo, BorderLayout.CENTER);

      // Bottone Logout a destra
      JButton logoutButton = Costants.creaBottoneLogOut();
      logoutButton.addActionListener(e -> {
          mainController.showPannelloConfiguratore();
      });
      
      JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      headerRightPanel.setOpaque(false);
      headerRightPanel.add(logoutButton);
      headerPanel.add(headerRightPanel, BorderLayout.EAST);

      mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale
        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Numero centrale (con grafica migliorata)
        JTextField textField = new JTextField();
        textField.setEditable(true);
        textField.setText(corpoDati.getMaxPersone());
        textField.setHorizontalAlignment(JTextField.CENTER); // Centra il testo
        textField.setFont(new Font("Arial", Font.BOLD, 30)); // Imposta un font più grande
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // Aggiungi un bordo
        textField.setBackground(Costants.BACKGROUND_COLOR);
        textField.setForeground(Color.BLACK);
        textField.setPreferredSize(new Dimension(300, 50)); // Imposta una dimensione preferita

        mainContentPanel.add(textField, BorderLayout.CENTER);

        // Pannello per il pulsante "Salva"
        JPanel savePanel = new JPanel();
        JButton salvaBtn = new JButton("Salva");
        salvaBtn.setFont(new Font("Arial", Font.BOLD, 14));
        salvaBtn.addActionListener(_ -> {
            mainController.salvaCorpoDati(corpoDati);
        });

        savePanel.add(salvaBtn);

        // Aggiungi il pannello per il pulsante "Salva"
        mainContentPanel.add(savePanel, BorderLayout.SOUTH);

        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = Costants.createFooterPanel("");
        footerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeCorpoDati() {
        this.corpoDati = mainController.getCorpoDati();
    }

    private void initializeFrame() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
