package card;
import model.Luogo;
import controller.AppController;
import view.LuoghiFrame;

import javax.swing.*;
import java.awt.*;

public class LuogoCard extends JPanel {
    private JLabel nomeLabel, posizioneLabel;
    private JButton modificaButton, eliminaButton;
    private Luogo luogo;
    private AppController controller;
    private LuoghiFrame frame;

    public LuogoCard(Luogo luogo, AppController controller, LuoghiFrame frame) {
        this.luogo = luogo;
        this.controller = controller;
        this.frame = frame;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(400, 100));

        nomeLabel = new JLabel("Nome: " + luogo.getNome());
        posizioneLabel = new JLabel("Posizione: " + luogo.getPosizione());

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(nomeLabel);
        infoPanel.add(posizioneLabel);

        JPanel buttonPanel = new JPanel();
        modificaButton = new JButton("Modifica");
        eliminaButton = new JButton("Elimina");

        modificaButton.addActionListener(e -> modificaLuogo());
        eliminaButton.addActionListener(e -> eliminaLuogo());

        buttonPanel.add(modificaButton);
        buttonPanel.add(eliminaButton);

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void modificaLuogo() {
        String nuovoNome = JOptionPane.showInputDialog(this, "Modifica Nome:", luogo.getNome());
        String nuovaPosizione = JOptionPane.showInputDialog(this, "Modifica Posizione:", luogo.getPosizione());

        if (nuovoNome != null && !nuovoNome.trim().isEmpty() && nuovaPosizione != null && !nuovaPosizione.trim().isEmpty()) {
            luogo.setNome(nuovoNome);
            luogo.setPosizione(nuovaPosizione);
            nomeLabel.setText("Nome: " + nuovoNome);
            posizioneLabel.setText("Posizione: " + nuovaPosizione);
            controller.salvaDati();  // Salva i dati aggiornati
            frame.aggiornaLista();  // Aggiorna l'elenco dei luoghi
        }
    }

    private void eliminaLuogo() {
        int conferma = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare questo luogo?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
        if (conferma == JOptionPane.YES_OPTION) {
            controller.getLuoghi().remove(luogo);
            controller.salvaDati();  // Salva i dati aggiornati
            frame.aggiornaLista();  // Aggiorna l'elenco dei luoghi
        }
    }
}
