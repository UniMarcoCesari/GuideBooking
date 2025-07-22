package card;

import model.Visita;
import view.login.MainController;
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
    private Ruolo ruolo;
    private String currentUsername;
    private MainController mainController;
    private boolean showActions; // Controlla se mostrare i pulsanti di azione

    // Costruttore per uso con utente loggato ma senza azioni
    public VisitaCard(MainController mainController, Visita visita, String currentUsername) {
        this(mainController, visita, currentUsername, true);
    }

    // Costruttore completo con controllo delle azioni
    public VisitaCard(MainController mainController, Visita visita, String currentUsername, boolean showActions) {
        this.mainController = mainController;
        this.visita = visita;
        this.currentUsername = currentUsername;
        this.showActions = showActions;

        initializeUserContext();
        setupLayout();
        buildLeftPanel();
        buildRightPanel();
    }

    // Inizializza il contesto utente
    private void initializeUserContext() {
        if (currentUsername != null && mainController != null) {
            // Verifica se l'utente corrente è già iscritto
            Optional<Iscrizione> iscrizione = visita.getIscrizioni().stream()
                    .filter(i -> i.getUsernameFruitore().equals(currentUsername))
                    .findFirst();

            try {
                ruolo = mainController.getAuthController().getRuoloByUsername(currentUsername);
                System.out.println("Ruolo ottenuto: " + ruolo); // Debug
            } catch (Exception e) {
                System.err.println("Errore nell'ottenere il ruolo per l'utente: " + currentUsername);
                e.printStackTrace();
                ruolo = null;
            }
            
            utenteIscritto = iscrizione.isPresent();
            
            if (utenteIscritto) {
                codicePrenotazione = iscrizione.get().getCodicePrenotazione();
            }
        }
    }

    // Configura il layout di base
    private void setupLayout() {
        setLayout(new BorderLayout(20, 0));
        setPreferredSize(new Dimension(800, 200));
        setBackground(Color.WHITE);

        // Determina il colore del bordo
        Color borderColor = determineBorderColor();
        setBorder(new CompoundBorder(
                new LineBorder(borderColor, 2, true),
                new EmptyBorder(12, 20, 12, 20)
        ));
    }

    // Determina il colore del bordo in base allo stato e all'iscrizione
    private Color determineBorderColor() {
        if (visita.getStato() == Visita.STATO_VISITA.CANCELLATA) {
            return getColorForStato(Visita.STATO_VISITA.CANCELLATA);
        } else if (utenteIscritto) {
            return (visita.getStato() == Visita.STATO_VISITA.EFFETTUATA)
                ? getColorForStato(Visita.STATO_VISITA.EFFETTUATA)
                : getColorForStato(Visita.STATO_VISITA.CONFERMATA);
        } else {
            return getColorForStato(visita.getStato());
        }
    }

    // Costruisce il pannello sinistro con le informazioni
    private void buildLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        Font labelFont = Costants.BUTTON_FONT.deriveFont(Font.PLAIN, 14f);
        Font boldFont = labelFont.deriveFont(Font.BOLD, 16f);

        // Titolo
        JLabel titoloLabel = new JLabel(visita.getTipo().getTitolo());
        titoloLabel.setFont(boldFont);
        titoloLabel.setForeground(Costants.ACCENT_COLOR);
        titoloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(titoloLabel);
        leftPanel.add(Box.createVerticalStrut(6));

        // Informazioni dettagliate
        addInfoLabel(leftPanel, "Descrizione: " + visita.getTipo().getDescrizione(), labelFont);
        addInfoLabel(leftPanel, "Punto di incontro: " + visita.getTipo().getPuntoIncontro(), labelFont);
        addInfoLabel(leftPanel, "Data: " + Costants.formatToItalian(visita.getData()), labelFont);
        addInfoLabel(leftPanel, "Ora inizio: " + visita.getTipo().getOraInizio().toString(), labelFont);
        addInfoLabel(leftPanel, "Biglietto Ingresso: " + (visita.getTipo().isBigliettoNecessario() ? "Necessario" : "Non necessario"), labelFont);
        addInfoLabel(leftPanel, "Guida: " + (visita.getGuidaAssegnata() != null ? visita.getGuidaAssegnata().getNome() : "N/A"), labelFont);

        // Stato
        addStatoPanel(leftPanel, labelFont);

        // Iscrizioni
        addIscrizioniLabel(leftPanel, labelFont);

        add(leftPanel, BorderLayout.CENTER);
    }

    // Aggiunge una label informativa
    private void addInfoLabel(JPanel parent, String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
    }

    // Aggiunge il pannello dello stato
    private void addStatoPanel(JPanel parent, Font labelFont) {
        JPanel statoPanel = new JPanel();
        statoPanel.setLayout(new BoxLayout(statoPanel, BoxLayout.X_AXIS));
        statoPanel.setOpaque(false);
        statoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        Color statoBadgeColor = determineBorderColor();
        JLabel statoBadge = new JLabel("● ");
        statoBadge.setFont(labelFont.deriveFont(Font.BOLD, 16f));
        statoBadge.setForeground(statoBadgeColor);

        String statoText = determineStatoText();
        JLabel statoLabel = new JLabel(statoText);
        statoLabel.setFont(labelFont);
        statoLabel.setForeground(statoBadgeColor);

        statoPanel.add(statoBadge);
        statoPanel.add(statoLabel);
        statoPanel.add(Box.createHorizontalGlue());
        parent.add(statoPanel);
    }

    // Determina il testo dello stato
    private String determineStatoText() {
        if (visita.getStato() == Visita.STATO_VISITA.CANCELLATA) {
            return "Visita cancellata";
        } else if (utenteIscritto) {
            return (visita.getStato() == Visita.STATO_VISITA.EFFETTUATA) 
                ? "Hai partecipato" 
                : "Sei iscritto";
        } else {
            return "Stato: " + visita.getStato().name();
        }
    }

    // Aggiunge la label delle iscrizioni
    private void addIscrizioniLabel(JPanel parent, Font labelFont) {
        int maxPartecipanti = visita.getTipo().getMaxPartecipanti();
        int minPartecipanti = visita.getTipo().getMinPartecipanti();
        int totaleIscritti = visita.getTotaleIscritti();
        String iscrizioniText = String.format("Iscrizioni %d, Min: %d, Max: %d", totaleIscritti, minPartecipanti, maxPartecipanti);
        JLabel iscrizioniLabel = new JLabel(iscrizioniText);
        iscrizioniLabel.setFont(labelFont);
        iscrizioniLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(iscrizioniLabel);
    }

    // Costruisce il pannello destro con azioni e informazioni specifiche per ruolo
    private void buildRightPanel() {
        if (!showActions || currentUsername == null || ruolo == null) {
            System.out.println("Saltando pannello destro - showActions: " + showActions + 
                             ", currentUsername: " + currentUsername + ", ruolo: " + ruolo);
            return; // Non mostrare il pannello destro se non necessario
        }

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        Font labelFont = Costants.BUTTON_FONT.deriveFont(Font.PLAIN, 14f);
        Font boldFont = labelFont.deriveFont(Font.BOLD, 16f);

        System.out.println("Costruendo pannello per ruolo: " + ruolo);

        switch (ruolo) {
            case VOLONTARIO:
                buildVolontarioPanel(rightPanel, labelFont);
                break;
            case FRUITORE:
                buildFruitorePanel(rightPanel, labelFont, boldFont);
                break;
            case CONFIGURATORE:
                buildConfiguratorePanel(rightPanel, labelFont);
                break;
            default:
                System.out.println("Ruolo non riconosciuto: " + ruolo);
                break;
        }

        if (rightPanel.getComponentCount() > 0) {
            add(rightPanel, BorderLayout.EAST);
        }
    }

    // Pannello per volontario
    private void buildVolontarioPanel(JPanel rightPanel, Font labelFont) {
        if (visita.getStato() == Visita.STATO_VISITA.CANCELLATA || 
            visita.getStato() == Visita.STATO_VISITA.EFFETTUATA) {
            return;
        }

        List<String> codiciIscrizioni = visita.getIscrizioni().stream()
                .map(Iscrizione::getCodicePrenotazione)
                .collect(Collectors.toList());

        if (!codiciIscrizioni.isEmpty()) {
            JLabel codiciLabel = new JLabel("<html>Codici iscrizioni:<br>" + 
                String.join("<br>", codiciIscrizioni) + "</html>");
            codiciLabel.setFont(labelFont);
            rightPanel.add(codiciLabel);
        }
    }

    // Pannello per fruitore
    private void buildFruitorePanel(JPanel rightPanel, Font labelFont, Font boldFont) {
        boolean visitaIscrivibile = visita.getStato() == Visita.STATO_VISITA.PROPOSTA || 
                                   visita.getStato() == Visita.STATO_VISITA.COMPLETA;
        boolean visitaCancellata = visita.getStato() == Visita.STATO_VISITA.CANCELLATA;

        if (visitaIscrivibile || (utenteIscritto && !visitaCancellata)) {
            // Pannello bottoni
            if (shouldShowButtons()) {
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.setOpaque(false);

                if (utenteIscritto && visita.getStato() != Visita.STATO_VISITA.EFFETTUATA) {
                    disiscrivitiButton = Costants.createSimpleButton("Disiscriviti");
                    buttonPanel.add(disiscrivitiButton);
                } else if (!utenteIscritto && visitaIscrivibile) {
                    iscrivitiButton = Costants.createSimpleButton("Iscriviti");
                    buttonPanel.add(iscrivitiButton);
                }

                if (buttonPanel.getComponentCount() > 0) {
                    rightPanel.add(buttonPanel);
                }
            }

            // Codice prenotazione
            if (utenteIscritto && codicePrenotazione != null) {
                addCodicePrenotazionePanel(rightPanel, labelFont, boldFont);
            }
        }
    }

    // Pannello per configuratore
    private void buildConfiguratorePanel(JPanel rightPanel, Font labelFont) {
        // Pannello per gestire le visite (modificare, cancellare, confermare)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        
        // Pulsanti in base allo stato della visita
        switch (visita.getStato()) {
            case PROPOSTA:
                // Può confermare o cancellare la visita
                JButton confermaButton = Costants.createSimpleButton("Conferma");
                JButton cancellaButton = Costants.createSimpleButton("Cancella");
                actionPanel.add(confermaButton);
                actionPanel.add(cancellaButton);
                break;
                
            case COMPLETA:
            case CONFERMATA:
                // Può solo cancellare se non è ancora effettuata
                JButton cancellaButton2 = Costants.createSimpleButton("Cancella");
                actionPanel.add(cancellaButton2);
                break;
                
            case EFFETTUATA:
                // Mostra statistiche della visita
                JLabel statsLabel = new JLabel("<html>Visita completata<br>Partecipanti: " + 
                    visita.getTotaleIscritti() + "</html>");
                statsLabel.setFont(labelFont);
                rightPanel.add(statsLabel);
                return;
                
            case CANCELLATA:
                // Mostra solo lo stato
                JLabel cancelledLabel = new JLabel("Visita cancellata");
                cancelledLabel.setFont(labelFont);
                cancelledLabel.setForeground(new Color(192, 57, 43));
                rightPanel.add(cancelledLabel);
                return;
        }
        
        if (actionPanel.getComponentCount() > 0) {
            rightPanel.add(actionPanel);
        }
        
        // Mostra sempre l'elenco degli iscritti se ce ne sono
        if (visita.getTotaleIscritti() > 0) {
            rightPanel.add(Box.createVerticalStrut(10));
            List<String> iscritti = visita.getIscrizioni().stream()
                .map(i -> i.getUsernameFruitore())
                .collect(Collectors.toList());
            
            JLabel iscrittiLabel = new JLabel("<html>Iscritti:<br>" + 
                String.join("<br>", iscritti) + "</html>");
            iscrittiLabel.setFont(labelFont);
            rightPanel.add(iscrittiLabel);
        }
    }

    // Verifica se mostrare i bottoni
    private boolean shouldShowButtons() {
        boolean result = showActions && mainController != null && ruolo != null;
        System.out.println("shouldShowButtons: " + result + 
                         " (showActions: " + showActions + 
                         ", mainController: " + (mainController != null) + 
                         ", ruolo: " + ruolo + ")");
        return result;
    }

    // Aggiunge il pannello del codice prenotazione
    private void addCodicePrenotazionePanel(JPanel rightPanel, Font labelFont, Font boldFont) {
        rightPanel.add(Box.createVerticalStrut(10));

        JPanel codicePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        codicePanel.setOpaque(false);

        JLabel codiceLabel = new JLabel("Codice prenotazione: ");
        codiceLabel.setFont(labelFont);
        codicePanel.add(codiceLabel);

        Color codiceColor = (visita.getStato() == Visita.STATO_VISITA.EFFETTUATA) ?
                new Color(127, 140, 141) : new Color(39, 174, 96);

        JLabel codiceValueLabel = new JLabel(codicePrenotazione);
        codiceValueLabel.setFont(boldFont);
        codiceValueLabel.setForeground(codiceColor);
        codicePanel.add(codiceValueLabel);

        rightPanel.add(codicePanel);
    }

    // Metodi pubblici per la gestione degli event listener
    public void addIscrivitiActionListener(ActionListener listener) {
        if (iscrivitiButton != null) {
            clearActionListeners(iscrivitiButton);
            iscrivitiButton.addActionListener(e -> handleIscrivitiAction(e, listener));
        }
    }

    public void addDisiscrivitiActionListener(ActionListener listener) {
        if (disiscrivitiButton != null) {
            clearActionListeners(disiscrivitiButton);
            disiscrivitiButton.addActionListener(listener);
        }
    }

    // Gestisce l'azione di iscrizione con popup
    private void handleIscrivitiAction(ActionEvent e, ActionListener listener) {
        int maxPartecipanti = visita.getTipo().getMaxPartecipanti();
        int currentIscritti = visita.getTotaleIscritti();
        int postiDisponibili = maxPartecipanti - currentIscritti;

        if (postiDisponibili <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La visita è al completo. Non ci sono più posti disponibili.",
                    "Posti Esauriti", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this,
                String.format("In quanti desiderate iscrivervi? (Posti disponibili: %d)", postiDisponibili),
                "Numero Partecipanti", JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int numeroPartecipanti = Integer.parseInt(input.trim());
                if (numeroPartecipanti <= 0) {
                    showErrorMessage("Inserisci un numero positivo.");
                } else if (numeroPartecipanti > postiDisponibili) {
                    showErrorMessage("Hai richiesto più posti di quelli disponibili.\nPosti disponibili: " + postiDisponibili);
                } else {
                    ActionEvent newEvent = new ActionEvent(e.getSource(), e.getID(), String.valueOf(numeroPartecipanti));
                    listener.actionPerformed(newEvent);
                }
            } catch (NumberFormatException ex) {
                showErrorMessage("Inserisci un numero valido.");
            }
        }
    }

    // Utility per mostrare messaggi di errore
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Errore Input", JOptionPane.ERROR_MESSAGE);
    }

    // Utility per rimuovere tutti gli ActionListener
    private void clearActionListeners(JButton button) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
    }

    // Metodi per aggiornare dinamicamente la card
    public void addConfermaActionListener(ActionListener listener) {
        // Trova il pulsante "Conferma" nel pannello destro se esiste
        findAndSetButtonListener("Conferma", listener);
    }
    
    public void addCancellaActionListener(ActionListener listener) {
        // Trova il pulsante "Cancella" nel pannello destro se esiste
        findAndSetButtonListener("Cancella", listener);
    }
    
    // Utility per trovare e impostare listener sui pulsanti
    private void findAndSetButtonListener(String buttonText, ActionListener listener) {
        Component rightPanel = ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.EAST);
        if (rightPanel instanceof JPanel) {
            findButtonInPanel((JPanel) rightPanel, buttonText, listener);
        }
    }
    
    private void findButtonInPanel(JPanel panel, String buttonText, ActionListener listener) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                findButtonInPanel((JPanel) comp, buttonText, listener);
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals(buttonText)) {
                    clearActionListeners(button);
                    button.addActionListener(listener);
                    return;
                }
            }
        }
    }
    public void updateUserContext(String newUsername) {
        this.currentUsername = newUsername;
        initializeUserContext();
        refreshCard();
    }

    public void refreshCard() {
        removeAll();
        setupLayout();
        buildLeftPanel();
        buildRightPanel();
        revalidate();
        repaint();
    }

    // Getter pubblici
    public Visita getVisita() {
        return visita;
    }

    public boolean isUtenteIscritto() {
        return utenteIscritto;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    // Metodo per ottenere il colore dello stato
    private Color getColorForStato(Visita.STATO_VISITA stato) {
        return switch (stato) {
            case CONFERMATA -> new Color(39, 174, 96);      // Verde
            case COMPLETA -> new Color(41, 128, 185);       // Blu
            case CANCELLATA -> new Color(192, 57, 43);      // Rosso
            case EFFETTUATA -> new Color(127, 140, 141);    // Grigio
            case PROPOSTA -> new Color(243, 156, 18);       // Arancio
        };
    }
}