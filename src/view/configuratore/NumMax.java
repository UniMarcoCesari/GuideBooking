package view.configuratore;

import costants.Costants;
import model.CorpoDati;
import service.DataManager;

import javax.swing.*;
import java.awt.*;

public class NumMax extends JFrame {
    private CorpoDati corpoDati; // Rimosso il modificatore 'final'

    public NumMax() {
        initializeFrame();
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Inizializza corpoDati in modo sicuro
        initializeCorpoDati();

        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Lista volontari");
        JButton indietro = Costants.createSimpleButton("Indietro");
        indietro.addActionListener(_ -> {
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
        salvaBtn.addActionListener(_ -> {
            String nuovoValore = textField.getText();
            try {
                int nuovoNumero = Integer.parseInt(nuovoValore);
                corpoDati.setMaxPersone(String.valueOf(nuovoNumero));
                DataManager.salvaCorpoDati(corpoDati, Costants.file_corpo);
                JOptionPane.showMessageDialog(this, "Dati salvati con successo!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Il valore inserito non è un numero!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
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

    private void initializeCorpoDati() {
        try {
            corpoDati = DataManager.caricaCorpoDati(Costants.file_corpo);
            if (corpoDati == null) {
                corpoDati = new CorpoDati();
                corpoDati.setMaxPersone("0"); // Imposta un valore di default
                corpoDati.setAmbito("Default"); // Imposta un valore di default per ambito
                // Salva il nuovo corpoDati per evitare problemi futuri
                DataManager.salvaCorpoDati(corpoDati, Costants.file_corpo);
                System.out.println("[INFO] Creato nuovo CorpoDati con valori di default");
            }
        } catch (Exception e) {
            // In caso di errore, crea un nuovo CorpoDati e continua
            System.err.println("[ERRORE] Impossibile caricare CorpoDati: " + e.getMessage());
            corpoDati = new CorpoDati();
            corpoDati.setMaxPersone("0");
            corpoDati.setAmbito("Default");
            // Salva il nuovo corpoDati
            DataManager.salvaCorpoDati(corpoDati, Costants.file_corpo);
        }
    }

    private void initializeFrame() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

