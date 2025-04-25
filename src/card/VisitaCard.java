package card;

import model.Visita;
import costants.Costants;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Optional;
import model.Iscrizione;

public class VisitaCard extends JPanel {

    private final Visita visita;
    private JButton iscrivitiButton;
    private JButton disiscrivitiButton;
    private String codicePrenotazione;
    private boolean utenteIscritto = false;

    public VisitaCard(Visita visita) {
        this(visita, null);
    }

    public VisitaCard(Visita visita, String currentUsername) {
        this.visita = visita;

        // Verifica se l'utente corrente è già iscritto e ottiene il codice di prenotazione
        if (currentUsername != null) {
            Optional<Iscrizione> iscrizione = visita.getIscrizioni().stream()
                    .filter(i -> i.getUsernameFruitore().equals(currentUsername))
                    .findFirst();

            utenteIscritto = iscrizione.isPresent();
            if (utenteIscritto) {
                codicePrenotazione = iscrizione.get().getCodicePrenotazione();
            }
        }

        // Layout a 2 colonne: sinistra per le informazioni e destra per il bottone
        setLayout(new BorderLayout(20, 0));
        setBackground(Color.WHITE);

        // Se l'utente è iscritto, usa il bordo verde indipendentemente dallo stato
        Color borderColor = utenteIscritto ?
                new Color(39, 174, 96) : // Verde per utente iscritto
                getColorForStato(visita.getStato());

        setBorder(new CompoundBorder(
                new LineBorder(borderColor, 2, true),  // BORDO COLORATO
                new EmptyBorder(12, 20, 12, 20)
        ));
        setMaximumSize(new Dimension(1000, 150));
        setPreferredSize(new Dimension(700, 130));

        Font labelFont = Costants.BUTTON_FONT.deriveFont(Font.PLAIN, 14f);
        Font boldFont = labelFont.deriveFont(Font.BOLD);

        // PANEL SINISTRO
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel tipoLabel = new JLabel(visita.getTipo().getTitolo());
        tipoLabel.setFont(boldFont);
        tipoLabel.setForeground(Costants.ACCENT_COLOR);
        tipoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(tipoLabel);
        leftPanel.add(Box.createVerticalStrut(6));

        JLabel dataLabel = new JLabel("Data: " + Costants.formatToItalian(visita.getData()));
        dataLabel.setFont(labelFont);
        dataLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(dataLabel);

        JLabel guidaLabel = new JLabel("Guida: " + (visita.getGuidaAssegnata() != null ? visita.getGuidaAssegnata().getNome() : "N/A"));
        guidaLabel.setFont(labelFont);
        guidaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(guidaLabel);

        // STATO + BADGE COLORE
        JPanel statoPanel = new JPanel();
        statoPanel.setLayout(new BoxLayout(statoPanel, BoxLayout.X_AXIS));
        statoPanel.setOpaque(false);
        statoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statoBadge = new JLabel("● ");
        statoBadge.setFont(labelFont.deriveFont(Font.BOLD, 16f));
        statoBadge.setForeground(utenteIscritto ? new Color(39, 174, 96) : getColorForStato(visita.getStato()));

        // Cambia testo se l'utente è iscritto
        String statoText = utenteIscritto ?
                "Sei iscritto" :
                "Stato: " + visita.getStato().name();
        JLabel statoLabel = new JLabel(statoText);
        statoLabel.setFont(labelFont);
        statoLabel.setForeground(utenteIscritto ? new Color(39, 174, 96) : getColorForStato(visita.getStato()));

        statoPanel.add(statoBadge);
        statoPanel.add(statoLabel);
        statoPanel.add(Box.createHorizontalGlue());
        leftPanel.add(statoPanel);

        int maxPartecipanti = visita.getTipo().getMaxPartecipanti();
        int totaleIscritti = visita.getTotaleIscritti();
        String iscrizioniText = String.format("Iscrizioni: %d / %d", totaleIscritti, maxPartecipanti);
        JLabel iscrizioniLabel = new JLabel(iscrizioniText);
        iscrizioniLabel.setFont(labelFont);
        iscrizioniLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(iscrizioniLabel);

        add(leftPanel, BorderLayout.CENTER);

        // PANEL DESTRO: bottone "Iscriviti" o "Disiscriviti"
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // Allinea a destra
        rightPanel.setOpaque(false);

        if (visita.getStato() == Visita.STATO_VISITA.PROPOSTA || visita.getStato() == Visita.STATO_VISITA.COMPLETA) {
            if (!utenteIscritto) {
                iscrivitiButton = Costants.createSimpleButton("Iscriviti");
                rightPanel.add(iscrivitiButton);
            } else {
                disiscrivitiButton = Costants.createSimpleButton("Disiscriviti");
                rightPanel.add(disiscrivitiButton);
            }
            add(rightPanel, BorderLayout.EAST);
        }
    }

    public void addIscrivitiActionListener(ActionListener listener) {
        if (iscrivitiButton != null) {
            // Rimuovi eventuali listener precedenti per evitare duplicazioni
            for (ActionListener al : iscrivitiButton.getActionListeners()) {
                iscrivitiButton.removeActionListener(al);
            }
            // Aggiungi il nuovo listener che include la logica del popup
            iscrivitiButton.addActionListener(e -> {
                // Get maximum allowed participants and current registrations
                int maxPartecipanti = visita.getTipo().getMaxPartecipanti();
                int currentIscritti = visita.getIscrizioni().size();
                int postiDisponibili = maxPartecipanti - currentIscritti;

                if (postiDisponibili <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "La visita è al completo. Non ci sono più posti disponibili.",
                            "Posti Esauriti",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                String messaggioPrompt = String.format(
                        "In quanti desiderate iscrivervi? (Posti disponibili: %d)",
                        postiDisponibili
                );

                String input = JOptionPane.showInputDialog(
                        this,
                        messaggioPrompt,
                        "Numero Partecipanti",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (input != null && !input.trim().isEmpty()) {
                    try {
                        int numeroPartecipanti = Integer.parseInt(input.trim());
                        if (numeroPartecipanti <= 0) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Inserisci un numero positivo.",
                                    "Errore Input",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        } else if (numeroPartecipanti > postiDisponibili) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Hai richiesto più posti di quelli disponibili.\\nPosti disponibili: " + postiDisponibili,
                                    "Troppi Partecipanti",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        } else {
                            // Crea un nuovo ActionEvent con il numero di partecipanti come command
                            ActionEvent newEvent = new ActionEvent(
                                    e.getSource(),
                                    e.getID(),
                                    String.valueOf(numeroPartecipanti)
                            );
                            // Chiama il listener originale passando il nuovo evento
                            listener.actionPerformed(newEvent);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Inserisci un numero valido.",
                                "Errore Input",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                // Se l'input è null o vuoto (es. l'utente ha premuto Annulla), non fare nulla
            });
        }
    }

    public void addDisiscrivitiActionListener(ActionListener listener) {
        if (disiscrivitiButton != null) {
            // Rimuovi eventuali listener precedenti per evitare duplicazioni
            for (ActionListener al : disiscrivitiButton.getActionListeners()) {
                disiscrivitiButton.removeActionListener(al);
            }
            // Aggiungi il nuovo listener
            disiscrivitiButton.addActionListener(e -> {
                listener.actionPerformed(e);
            });
        }
    }

    public Visita getVisita() {
        return visita;
    }

    public boolean isUtenteIscritto() {
        return utenteIscritto;
    }

    private Color getColorForStato(Visita.STATO_VISITA stato) {
        return switch (stato) {
            case CONFERMATA -> new Color(39, 174, 96);      // Verde
            case COMPLETA -> new Color(41, 128, 185);     // Blu
            case CANCELLATA -> new Color(192, 57, 43);      // Rosso
            case EFFETTUATA -> new Color(127, 140, 141);    // Grigio
            case PROPOSTA -> new Color(243, 156, 18);     // Arancio
        };
    }
}
