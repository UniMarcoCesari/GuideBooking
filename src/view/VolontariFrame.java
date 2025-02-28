package view;

import controller.VolontariController;
import model.Volontario;
import costants.Costants;
import view.corpoDati.NuovoTipoVisitaFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VolontariFrame extends JFrame {
    private final JTextField nomeVolontarioField;
    private final JList<String> volontariList;
    private final DefaultListModel<String> volontariListModel;
    private final VolontariController controller;
    private final NuovoTipoVisitaFrame parent;

    public VolontariFrame(NuovoTipoVisitaFrame nuovoTipoVisitaFrame, VolontariController volontariController) {
        this.parent = nuovoTipoVisitaFrame;
        this.controller = volontariController;

        setTitle("Gestione Volontari");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Pannello superiore con titolo
        JPanel headerPanel = Costants.createHeaderPanel("Crea Nuovo Volontario");
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Pannello centrale per il form e la lista
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Pannello per il form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Costants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(new JLabel("Nome Volontario:"), gbc);
        nomeVolontarioField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nomeVolontarioField, gbc);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        // Lista dei volontari
        volontariListModel = new DefaultListModel<>();
        volontariList = new JList<>(volontariListModel);
        JScrollPane scrollPane = new JScrollPane(volontariList);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Carica volontari esistenti
        caricaVolontari();

        // Footer con i pulsanti
        JPanel footerPanel = Costants.createFooterPanel("");
        JButton salvaButton = Costants.createSimpleButton("Salva Volontario");
        salvaButton.addActionListener(e -> aggiungiVolontario());
        JButton annullaButton = Costants.createSimpleButton("Annulla");
        annullaButton.addActionListener(e -> chiudiEmandaIndietro());

        footerPanel.add(salvaButton, BorderLayout.CENTER);
        footerPanel.add(annullaButton, BorderLayout.EAST);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

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
        boolean aggiuntaConfermata = controller.aggiungiVolontario(new Volontario(nomeVolontario));
        if (!aggiuntaConfermata) {
            JOptionPane.showMessageDialog(this, "Errore: Esiste già un volontario con questo nome!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aggiorna la lista dei volontari
        volontariListModel.addElement(nomeVolontario);
        nomeVolontarioField.setText("");
        chiudiEmandaIndietro();
    }

    private void chiudiEmandaIndietro() {
        parent.aggiornaListaVolontari();
        dispose();
    }
}
