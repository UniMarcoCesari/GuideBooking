package card;

import model.Visita;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class VisitaCard extends JPanel {

    private Visita visita;

    public VisitaCard(Visita visita) {
        this.visita = visita;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(0, 1));

        JLabel tipoLabel = new JLabel("Tipo: " + visita.getTipo().getNome());
        JLabel dataLabel = new JLabel("Data: " + visita.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        JLabel guidaLabel = new JLabel("Guida: " + visita.getGuidaAssegnata().getNome());
        JLabel statoLabel = new JLabel("Stato: " + visita.getStato());

        detailsPanel.add(tipoLabel);
        detailsPanel.add(dataLabel);
        detailsPanel.add(guidaLabel);
        detailsPanel.add(statoLabel);

        add(detailsPanel, BorderLayout.CENTER);
    }

    public Visita getVisita() {
        return visita;
    }
}
