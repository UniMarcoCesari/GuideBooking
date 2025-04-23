package card;

import model.Visita;
import costants.Costants; // Importa le costanti

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
// Rimosso import non necessario: java.time.format.DateTimeFormatter;

public class VisitaCard extends JPanel {

    private Visita visita;

    public VisitaCard(Visita visita) {
        this.visita = visita;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Layout verticale
        setBackground(Costants.BUTTON_BACKGROUND); // Sfondo bianco (o un altro colore se preferisci)
        setBorder(new CompoundBorder(
                new LineBorder(Costants.BORDER_COLOR, 1, true), // Bordo grigio arrotondato
                new EmptyBorder(10, 15, 10, 15) // Padding interno
        ));
        setAlignmentX(Component.LEFT_ALIGNMENT); // Allinea a sinistra nel contenitore
        setMaximumSize(new Dimension(350, 120)); // Imposta dimensione massima
        setPreferredSize(new Dimension(300, 100)); // Imposta dimensione preferita

        // Usa font e colori dalle costanti
        Font labelFont = Costants.BUTTON_FONT.deriveFont(Font.PLAIN, 14f); // Usa font bottone ma più piccolo
        Font boldFont = labelFont.deriveFont(Font.BOLD);

        // Titolo (Tipo Visita) in grassetto
        JLabel tipoLabel = new JLabel(visita.getTipo().getTitolo());
        tipoLabel.setFont(boldFont);
        tipoLabel.setForeground(Costants.ACCENT_COLOR);
        add(tipoLabel);

        add(Box.createVerticalStrut(5)); // Spazio verticale

        // Dettagli
        JLabel dataLabel = new JLabel("Data: " + Costants.formatToItalian(visita.getData())); // Usa formattatore italiano
        dataLabel.setFont(labelFont);
        dataLabel.setForeground(Costants.ACCENT_COLOR);
        add(dataLabel);

        JLabel guidaLabel = new JLabel("Guida: " + (visita.getGuidaAssegnata() != null ? visita.getGuidaAssegnata().getNome() : "N/A"));
        guidaLabel.setFont(labelFont);
        guidaLabel.setForeground(Costants.ACCENT_COLOR);
        add(guidaLabel);

        JLabel statoLabel = new JLabel("Stato: " + visita.getStato());
        statoLabel.setFont(labelFont);
        // Potresti cambiare colore in base allo stato
        statoLabel.setForeground(getColorForStato(visita.getStato()));
        add(statoLabel);
    }

    // Metodo helper per colorare lo stato (opzionale)
    private Color getColorForStato(Visita.STATO_VISITA stato) {
        switch (stato) {
            case CONFERMATA:
                return new Color(0, 150, 0); // Verde scuro
            case COMPLETA:
                 return new Color(0, 100, 200); // Blu
            case CANCELLATA:
                return Color.RED;
            case EFFETTUATA:
                return Color.GRAY;
            case PROPOSTA:
            default:
                return Costants.ACCENT_COLOR; // Colore di default
        }
    }

    public Visita getVisita() {
        return visita;
    }
}
