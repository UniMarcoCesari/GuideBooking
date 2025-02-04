package view;
import model.Luogo;
import controller.AppController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LuoghiFrame extends JFrame {
    private DefaultListModel<String> luogoModel;
    private JList<String> listaLuoghi;
    private JTextField nomeField, descrizioneField, posizioneField;
    private AppController controller;

    public LuoghiFrame() {
        this.controller = new AppController();
        setTitle("Gestione Luoghi");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        luogoModel = new DefaultListModel<>();
        listaLuoghi = new JList<>(luogoModel);
        add(new JScrollPane(listaLuoghi), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        inputPanel.add(nomeField);

        inputPanel.add(new JLabel("Descrizione:"));
        descrizioneField = new JTextField();
        inputPanel.add(descrizioneField);

        inputPanel.add(new JLabel("Posizione:"));
        posizioneField = new JTextField();
        inputPanel.add(posizioneField);

        JButton addButton = new JButton("Aggiungi Luogo");
        addButton.addActionListener((ActionEvent e) -> aggiungiLuogo());
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.SOUTH);

        aggiornaLista();
    }

    private void aggiornaLista() {
        luogoModel.clear();

        if (controller.getLuoghi().isEmpty()) {
            luogoModel.addElement("⚠️ Nessun luogo disponibile.");
        } else {
            for (Luogo l : controller.getLuoghi()) {
                luogoModel.addElement(l.getNome() + " - " + l.getPosizione());
            }
        }
    }


    private void aggiungiLuogo() {
        String nome = nomeField.getText().trim();
        String descrizione = descrizioneField.getText().trim();
        String posizione = posizioneField.getText().trim();

        if (nome.isEmpty() || posizione.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Posizione sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        controller.aggiungiLuogo(new Luogo(nome, descrizione, posizione));
        aggiornaLista();
        JOptionPane.showMessageDialog(this, "Luogo aggiunto con successo!");

        nomeField.setText("");
        descrizioneField.setText("");
        posizioneField.setText("");
    }
}
