package view.configuratore;

import costants.Costants;
import model.CorpoDati;
import service.DataManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Sezione7 extends JFrame {
    private final CorpoDati corpoDati;

    public Sezione7() {
        initializeFrame();
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Se corpoDati è null, crea una nuova istanza
        CorpoDati corpoDati = DataManager.caricaCorpoDati(Costants.file_corpo);
        if (corpoDati == null) {
            corpoDati = new CorpoDati();
            corpoDati.setMaxPersone("0"); // Imposta un valore di default
        }
        this.corpoDati = corpoDati;

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Lista volontari");
        JButton indietro = Costants.createSimpleButton("Indietro");
        indietro.addActionListener(e -> {
            dispose();
            new PannelloConfiguratore().setVisible(true);
        });
        headerPanel.add(indietro, BorderLayout.WEST);
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
        CorpoDati finalCorpoDati = corpoDati;
        salvaBtn.addActionListener(e -> {
            finalCorpoDati.setMaxPersone(textField.getText());
            DataManager.salvaCorpoDati(finalCorpoDati,Costants.file_corpo);
            JOptionPane.showMessageDialog(this, "Dati salvati con successo!");
        });

        savePanel.add(salvaBtn);

        // Aggiungi il pannello per il pulsante "Salva"
        mainContentPanel.add(savePanel, BorderLayout.SOUTH);

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
}
