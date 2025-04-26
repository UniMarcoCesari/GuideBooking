package card;

import model.Visita;
import costants.Costants;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.CredenzialeWriter;

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

            ruolo = CredenzialeWriter.getRuolo(currentUsername);
            utenteIscritto = iscrizione.isPresent();
            if (utenteIscritto) {
                codicePrenotazione = iscrizione.get().getCodicePrenotazione();
            }
        }

        // Layout a 2 colonne: sinistra per le informazioni e destra per il bottone
        setLayout(new BorderLayout(20, 0));
        setPreferredSize(new Dimension(800, 200));
        setBackground(Color.WHITE);

        // Se l'utente è iscritto, usa il bordo verde indipendentemente dallo stato
        Color borderColor = utenteIscritto ?
                new Color(39, 174, 96) : // Verde per utente iscritto
                getColorForStato(visita.getStato());

        setBorder(new CompoundBorder(
                new LineBorder(borderColor, 2, true),  // BORDO COLORATO
                new EmptyBorder(12, 20, 12, 20)
        ));

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

        System.out.println("ruolo: " + ruolo);

        boolean isVolontario = currentUsername != null && ruolo.equals(Costants.ruolo_volontario);
        boolean isFruitore = currentUsername != null && ruolo.equals(Costants.ruolo_fruitore);

        // Then fix the List section in your code:
        if(isVolontario && ( visita.getStato() == Visita.STATO_VISITA.PROPOSTA || visita.getStato() == Visita.STATO_VISITA.COMPLETA) )
        {
            List<String> codiciIscrizioni = visita.getIscrizioni().stream()
                    .map(Iscrizione::getCodicePrenotazione)
                    .collect(Collectors.toList());
            if(!codiciIscrizioni.isEmpty()) {
                JLabel errorLabel = new JLabel("<html>Codici iscrizioni:<br> " + String.join("<br> ", codiciIscrizioni) + "</html>");
                errorLabel.setFont(labelFont);
                errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                rightPanel.add(errorLabel);
            }
            // Add this line to ensure the panel is added to the main layout
            add(rightPanel, BorderLayout.EAST);
        }

        else if(isFruitore) 
        {
            if(!utenteIscritto && visita.getStato() == Visita.STATO_VISITA.COMPLETA) {
                
            }
            else if(!utenteIscritto && visita.getStato() == Visita.STATO_VISITA.CANCELLATA) {
                
            }
            else if (visita.getStato() == Visita.STATO_VISITA.PROPOSTA || visita.getStato() == Visita.STATO_VISITA.COMPLETA) {
                if (utenteIscritto) {
                    disiscrivitiButton = Costants.createSimpleButton("Disiscriviti");
                    rightPanel.add(disiscrivitiButton);
    
                    codicePrenotazione = visita.getIscrizioni().stream()
                            .filter(i -> i.getUsernameFruitore().equals(currentUsername))
                            .findFirst()
                            .map(Iscrizione::getCodicePrenotazione)
                            .orElse(null);
                    JLabel nuovaLabel = new JLabel(codicePrenotazione);
                    nuovaLabel.setFont(labelFont);
                    nuovaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    rightPanel.add(Box.createVerticalStrut(5));
                    rightPanel.add(nuovaLabel);
                } 
                else {
                    iscrivitiButton = Costants.createSimpleButton("Iscriviti");
                    rightPanel.add(iscrivitiButton);
                }
                add(rightPanel, BorderLayout.EAST);
            }
        
        } else if (visita.getStato() == Visita.STATO_VISITA.CANCELLATA) {
            JLabel dataMancatoSvolgimentoLabel = new JLabel("Data (mancato) svolgimento: " + Costants.formatToItalian(visita.getData()));
            dataMancatoSvolgimentoLabel.setFont(labelFont);
            dataMancatoSvolgimentoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(dataMancatoSvolgimentoLabel);
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

