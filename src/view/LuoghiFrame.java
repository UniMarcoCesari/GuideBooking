package view;
import model.Luogo;
import service.DataManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class LuoghiFrame extends JFrame {
    private DefaultListModel<String> luogoModel;
    private JList<String> listaLuoghi;
    private JTextField nomeField, descrizioneField, posizioneField;
    private List<Luogo> luoghi;

    public LuoghiFrame() {
        setTitle("Gestione Luoghi");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inizializza la lista
        luogoModel = new DefaultListModel<>();
        listaLuoghi = new JList<>(luogoModel);
        add(new JScrollPane(listaLuoghi), BorderLayout.CENTER);

        // Form per aggiungere nuovi luoghi
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

        caricaLuoghi(); // Carica i luoghi salvati
    }

    private void caricaLuoghi() {
        luoghi = (List<Luogo>) DataManager.caricaDatiLuogo("luoghi.dat");
        if (luoghi == null) {
            luoghi = new ArrayList<>();
        }
        aggiornaLista();
    }

    private void aggiornaLista() {
        luogoModel.clear();
        for (Luogo l : luoghi) {
            luogoModel.addElement(l.getNome() + " - " + l.getPosizione());
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

        Luogo nuovoLuogo = new Luogo(nome, descrizione, posizione);
        luoghi.add(nuovoLuogo);
        DataManager.salvaDatiLuogo(luoghi, "luoghi.dat");

        aggiornaLista();
        JOptionPane.showMessageDialog(this, "Luogo aggiunto con successo!");

        // Pulisce i campi dopo l'inserimento
        nomeField.setText("");
        descrizioneField.setText("");
        posizioneField.setText("");
    }
}
