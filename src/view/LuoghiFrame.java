package view;
import model.Luogo;
import controller.AppController;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import card.*;

public class LuoghiFrame extends JFrame {
    private JPanel listaPanel;
    private JTextField nomeField, descrizioneField, posizioneField;
    private AppController controller;

    public LuoghiFrame() {
        this.controller = new AppController();
        setTitle("Gestione Luoghi");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listaPanel);
        add(scrollPane, BorderLayout.CENTER);

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
        addButton.addActionListener(e -> aggiungiLuogo());
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.SOUTH);

        aggiornaLista();
    }

    public void aggiornaLista() {
        listaPanel.removeAll();
        List<Luogo> luoghi = controller.getLuoghi();
        if (luoghi.isEmpty()) {
            listaPanel.add(new JLabel("⚠️ Nessun luogo disponibile."));
        } else {
            for (Luogo l : luoghi) {
                listaPanel.add(new LuogoCard(l, controller, this));
            }
        }
        listaPanel.revalidate();
        listaPanel.repaint();
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
