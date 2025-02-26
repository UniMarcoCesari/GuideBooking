package view;

import controller.TipiVisitaController;
import controller.VolontariController;
import model.TipoVisita;
import costants.Costants;
import model.Volontario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VolontariFrame extends JFrame {
    private final JTextField nomeVolontarioField;
    private final JButton aggiungiVolontarioButton;
    private final JList<String> volontariList;
    private final DefaultListModel<String> volontariListModel;

    private VolontariController controller;

    NuovoTipoVisitaFrame parent;

    public VolontariFrame(NuovoTipoVisitaFrame nuovoTipoVisitaFrame, VolontariController volontariController) {

        parent = nuovoTipoVisitaFrame;
        controller = volontariController;

        setTitle("Gestione Volontari");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Pannello superiore con titolo e tasto indietro
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = Costants.createMenuButton("Indietro");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(e -> chiudiEmandaIndietro());

        JLabel titleLabel = new JLabel("Gestione Volontari", SwingConstants.CENTER);
        titleLabel.setFont(Costants.TITLE_FONT);
        titleLabel.setForeground(Costants.ACCENT_COLOR);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Pannello per il form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Costants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Campo per nome del volontario
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Nome Volontario:"), gbc);
        nomeVolontarioField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(nomeVolontarioField, gbc);
        row++;

        // Pulsante per aggiungere volontario
        aggiungiVolontarioButton = new JButton("Aggiungi Volontario");
        aggiungiVolontarioButton.addActionListener(e -> aggiungiVolontario());
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(aggiungiVolontarioButton, gbc);
        row++;

        // Lista dei volontari esistenti
        volontariListModel = new DefaultListModel<>();
        volontariList = new JList<>(volontariListModel);
        JScrollPane scrollPane = new JScrollPane(volontariList);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(scrollPane, gbc);
        row++;

        // Carica volontari esistenti
        caricaVolontari();

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }



    private void caricaVolontari() {
        ArrayList<Volontario> volontari = controller.getListaVolontari();
        for (Volontario volontario : volontari) {
            volontariListModel.addElement(volontario.getNome());
        }
    }

    private void aggiungiVolontario() {
        String nomeVolontario = nomeVolontarioField.getText().trim();
        if (nomeVolontario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Errore: Il nome del volontario non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aggiungi il volontario al controller
        boolean aggiuntaConfermta = controller.aggiungiVolontario(new Volontario(nomeVolontario));

        if (!aggiuntaConfermta) {
            JOptionPane.showMessageDialog(this, "Errore: esiste gia un volontario con questo nome!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aggiorna la lista dei volontari
        volontariListModel.addElement(nomeVolontario);
        nomeVolontarioField.setText("");

    }

    private void chiudiEmandaIndietro() {
       // parent.aggiornaListaVolonatari();
        dispose();

        //new NuovoTipoVisitaFrame().setVisible(true);  // Torna alla finestra precedente, ad esempio quella per i tipi di visita
    }


}
