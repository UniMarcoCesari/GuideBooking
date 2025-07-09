package card;

import model.Visita;
import enumerations.Ruolo;
import costants.Costants;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

import model.Iscrizione;

public class VisitaCard extends JPanel {

    private final Visita visita;
    private JButton iscrivitiButton;
    private JButton disiscrivitiButton;
    private String codicePrenotazione;
    private boolean utenteIscritto = false;
    private String ruolo;

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

            // ruolo = CredenzialiWriter.getRuolo(currentUsername).name();
            utenteIscritto = iscrizione.isPresent();
            if (utenteIscritto) {
                codicePrenotazione = iscrizione.get().getCodicePrenotazione();
            }
        }

        // Layout a 2 colonne: sinistra per le informazioni e destra per il bottone
        setLayout(new BorderLayout(20, 0));
        setPreferredSize(new Dimension(800, 200));
        setBackground(Color.WHITE);

        // Se l'utente è iscritto, usa il bordo verde solo se la visita non è stata effettuata
        Color borderColor;
        if (visita.getStato() == Visita.STATO_VISITA.CANCELLATA) {
            borderColor = getColorForStato(Visita.STATO_VISITA.CANCELLATA); // Rosso fisso
        } else if (utenteIscritto) {
            borderColor = (visita.getStato() == Visita.STATO_VISITA.EFFETTUATA)
                ? getColorForStato(Visita.STATO_VISITA.EFFETTUATA) // Grigio
                : getColorForStato(Visita.STATO_VISITA.CONFERMATA); // Verde
        } else {
            borderColor = getColorForStato(visita.getStato());
        }


        setBorder(new CompoundBorder(
                new LineBorder(borderColor, 2, true),  // BORDO COLORATO
                new EmptyBorder(12, 20, 12, 20)
        ));

        Font labelFont = Costants.BUTTON_FONT.deriveFont(Font.PLAIN, 14f);
        Font boldFont = labelFont.deriveFont(Font.BOLD,16f);

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

        // Add description
        JLabel descriptionLabel = new JLabel("Descrizione: " + visita.getTipo().getDescrizione());
        descriptionLabel.setFont(labelFont);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(descriptionLabel);

        // Add meeting point
        JLabel meetingPointLabel = new JLabel("Punto di incontro: " + visita.getTipo().getPuntoIncontro());
        meetingPointLabel.setFont(labelFont);
        meetingPointLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(meetingPointLabel);

        JLabel dataLabel = new JLabel("Data: " + Costants.formatToItalian(visita.getData()));
        dataLabel.setFont(labelFont);
        dataLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(dataLabel);

        JLabel oraInizioLabel = new JLabel("Ora inizio: " + visita.getTipo().getOraInizio().toString());
        oraInizioLabel.setFont(labelFont);
        oraInizioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(oraInizioLabel);

        JLabel bigliettoLabel = new JLabel("Biglietto Ingresso: " + (visita.getTipo().isBigliettoNecessario() ? "Necessario" : "Non necessario"));
        bigliettoLabel.setFont(labelFont);
        bigliettoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(bigliettoLabel);

        JLabel guidaLabel = new JLabel("Guida: " + (visita.getGuidaAssegnata() != null ? visita.getGuidaAssegnata().getNome() : "N/A"));
        guidaLabel.setFont(labelFont);
        guidaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(guidaLabel);

        // STATO + BADGE COLORE
        JPanel statoPanel = new JPanel();
        statoPanel.setLayout(new BoxLayout(statoPanel, BoxLayout.X_AXIS));
        statoPanel.setOpaque(false);
        statoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Colore del badge:
        // - Grigio se utente iscritto e visita effettuata
        // - Verde se utente iscritto e visita non effettuata
        // - Colore dello stato se utente non iscritto
        Color statoBadgeColor;
        if (visita.getStato() == Visita.STATO_VISITA.CANCELLATA) {
            statoBadgeColor = getColorForStato(Visita.STATO_VISITA.CANCELLATA); // Rosso fisso
        } else if (utenteIscritto) {
            statoBadgeColor = (visita.getStato() == Visita.STATO_VISITA.EFFETTUATA)
                ? getColorForStato(Visita.STATO_VISITA.EFFETTUATA)  // Grigio
                : getColorForStato(Visita.STATO_VISITA.CONFERMATA); // Verde
        } else {
            statoBadgeColor = getColorForStato(visita.getStato());
        }
        

        JLabel statoBadge = new JLabel("● ");
        statoBadge.setFont(labelFont.deriveFont(Font.BOLD, 16f));
        statoBadge.setForeground(statoBadgeColor);

        // Testo stato:
        // - "Hai partecipato" se utente iscritto e visita effettuata
        // - "Sei iscritto" se utente iscritto e visita non effettuata
        String statoText;

        if (visita.getStato() == Visita.STATO_VISITA.CANCELLATA) {
            statoText = "Visita cancellata";
        } else if (utenteIscritto) {
            if (visita.getStato() == Visita.STATO_VISITA.EFFETTUATA) {
                statoText = "Hai partecipato";
            } else {
                statoText = "Sei iscritto";
            }
        } else {
            statoText = "Stato: " + visita.getStato().name();
        }
        
        JLabel statoLabel = new JLabel(statoText);
        statoLabel.setFont(labelFont);
        statoLabel.setForeground(statoBadgeColor);  // Utilizzo dello stesso colore del badge

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
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setBackground(Costants.BACKGROUND_COLOR);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        

        System.out.println("ruolo: " + ruolo);

        // SEZIONE PANEL DESTRO: gestione pulsanti e codice iscrizione
        boolean isVolontario = currentUsername != null && ruolo.equals(Ruolo.VOLONTARIO);
        boolean isFruitore = currentUsername != null && ruolo.equals(Ruolo.FRUITORE);

        // Volontario -> mostra elenco codici se visita proposta/completa/confermata
        if (isVolontario && visita.getStato() != Visita.STATO_VISITA.CANCELLATA && visita.getStato() != Visita.STATO_VISITA.EFFETTUATA) {
            List<String> codiciIscrizioni = visita.getIscrizioni().stream()
                    .map(Iscrizione::getCodicePrenotazione)
                    .collect(Collectors.toList());
            if (!codiciIscrizioni.isEmpty()) {
                JLabel codiciLista = new JLabel("<html>Codici iscrizioni:<br> " + String.join("<br> ", codiciIscrizioni) + "</html>");
                codiciLista.setFont(labelFont);
                rightPanel.add(codiciLista);
            }
            add(rightPanel, BorderLayout.EAST);
        }

        // Fruitore -> gestisce iscrizione/disiscrizione
        if (isFruitore) {
            boolean visitaIscrivibile = visita.getStato() == Visita.STATO_VISITA.PROPOSTA || visita.getStato() == Visita.STATO_VISITA.COMPLETA;
            boolean visitaCancellata = visita.getStato() == Visita.STATO_VISITA.CANCELLATA;

            if (visitaIscrivibile || (utenteIscritto && !visitaCancellata)) {
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.setOpaque(false);
                buttonPanel.setBackground(Costants.BACKGROUND_COLOR);

                if (utenteIscritto) {
                    // Se iscritto -> mostra "Disiscriviti" (solo se la visita non è già effettuata)
                    if (visita.getStato() != Visita.STATO_VISITA.EFFETTUATA) {
                        disiscrivitiButton = Costants.createSimpleButton("Disiscriviti");
                        buttonPanel.add(disiscrivitiButton);
                    }
                } else if (visitaIscrivibile) {
                    // Se NON iscritto e visita iscrivibile -> mostra "Iscriviti"
                    iscrivitiButton = Costants.createSimpleButton("Iscriviti");
                    buttonPanel.add(iscrivitiButton);
                }

                rightPanel.add(buttonPanel);

                // Se iscritto, mostra anche il codice di prenotazione
                if (utenteIscritto && codicePrenotazione != null) {
                    rightPanel.add(Box.createVerticalStrut(10));

                    JPanel codicePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    codicePanel.setOpaque(false);

                    JLabel codiceLabel = new JLabel("Codice prenotazione: ");
                    codiceLabel.setFont(labelFont);
                    codicePanel.add(codiceLabel);

                    Color codiceColor = (visita.getStato() == Visita.STATO_VISITA.EFFETTUATA) ?
                            new Color(127, 140, 141) : // Grigio
                            new Color(39, 174, 96);   // Verde

                    JLabel codiceValueLabel = new JLabel(codicePrenotazione);
                    codiceValueLabel.setFont(boldFont);
                    codiceValueLabel.setForeground(codiceColor);
                    codicePanel.add(codiceValueLabel);

                    rightPanel.add(codicePanel);
                }

                add(rightPanel, BorderLayout.EAST);
            }
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
                int currentIscritti = visita.getTotaleIscritti();
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
