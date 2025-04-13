package view.volontario;

import controller.CalendarioController;
import controller.VolontariController;
import costants.Costants;
import model.Volontario;
import view.PannelloVolontario;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GestisciDisponibilitaFrame extends JFrame {

    private String username;
    private CalendarioController calendarioController;
    private VolontariController volontariController;
    private Volontario volontarioCorrente;
    private LocalDate meseSelezionato; // Primo giorno del mese selezionato
    private JPanel calendarPanel;
    private JLabel meseLabel;
    private List<JToggleButton> dayButtons = new ArrayList<>();
    
    public GestisciDisponibilitaFrame(String username) {
        this.username = username;
        this.calendarioController = new CalendarioController();
        this.volontariController = new VolontariController();
        
        // Trova il volontario corrente
        for (Volontario v : volontariController.getListaVolontari()) {
            if (v.getNome().equals(username)) {
                this.volontarioCorrente = v;
                break;
            }
        }
        
        if (this.volontarioCorrente == null) {
            JOptionPane.showMessageDialog(this, 
                "Volontario non trovato: " + username, 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        
        // Determina il mese da visualizzare
        determinaMeseSelezionato();
        
        initializeFrame();
        setupUI();
        
        setVisible(true);
    }
    
    private void determinaMeseSelezionato() {
        

        LocalDate dataCorrente = calendarioController.getDatacDateCorrenteLocalDate();
        
        
        // Se siamo dopo il 15 del mese, mostra il mese dopo il prossimo
        if (dataCorrente.getDayOfMonth() > 15) {
            meseSelezionato = dataCorrente.plusMonths(2);
            System.out.println("meseSelezionato: " + meseSelezionato.getMonthValue() + "/" + meseSelezionato.getYear());
        } else {
            // Altrimenti mostra il prossimo mese
            meseSelezionato = dataCorrente.plusMonths(1);
            System.out.println("meseSelezionato: " + meseSelezionato.getMonthValue() + "/" + meseSelezionato.getYear());
        }
    }

    private void initializeFrame() {
        setTitle("Gestisci Disponibilità - " + username);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = Costants.createHeaderPanel("Gestisci Disponibilità - " + username);
        headerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);
        
        // Pulsante indietro
        JButton indietroButton = Costants.createSimpleButton("Indietro");
        indietroButton.addActionListener(e -> {
            dispose();
            new PannelloVolontario(username).setVisible(true);
        });
        headerPanel.add(indietroButton, BorderLayout.WEST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Contenuto principale
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Costants.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(Costants.SPACING, Costants.SPACING, Costants.SPACING, Costants.SPACING));
        
        // Pannello per il titolo del mese e i controlli di navigazione
        JPanel monthPanel = new JPanel(new BorderLayout());
        monthPanel.setBackground(Costants.BACKGROUND_COLOR);
        
        // Formatta il nome del mese
        String nomeMese = meseSelezionato.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
        String annoMese = meseSelezionato.getYear() + "";
        meseLabel = new JLabel(nomeMese.toUpperCase() + " " + annoMese);
        meseLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        meseLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Rimuovi i pulsanti di navigazione mese
        // JButton prevMonthButton = new JButton("◀");
        // prevMonthButton.addActionListener(e -> cambioMese(-1));
        // JButton nextMonthButton = new JButton("▶");
        // nextMonthButton.addActionListener(e -> cambioMese(1));
        // monthPanel.add(prevMonthButton, BorderLayout.WEST);
        // monthPanel.add(nextMonthButton, BorderLayout.EAST);

        monthPanel.add(meseLabel, BorderLayout.CENTER); // Aggiungi solo l'etichetta del mese

        contentPanel.add(monthPanel, BorderLayout.NORTH);
        
        // Pannello per il calendario
        calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        calendarPanel.setBackground(Costants.BACKGROUND_COLOR);
        
        // Aggiungi i giorni della settimana
        String[] giorni = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
        for (String giorno : giorni) {
            JLabel label = new JLabel(giorno, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            calendarPanel.add(label);
        }
        
        // Aggiungi i giorni del mese
        aggiornaCalendario();
        
        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        
        // Pannello per i pulsanti di azione
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.setBackground(Costants.BACKGROUND_COLOR);
        
        JButton salvaButton = Costants.createSimpleButton("Salva Disponibilità");
        salvaButton.addActionListener(e -> salvaDisponibilita());
        
        actionPanel.add(salvaButton);
        
        contentPanel.add(actionPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = Costants.createFooterPanel("");
        footerPanel.setBackground(Costants.VOLONTARIO_HEADER_BACK);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    

    

    private void salvaDisponibilita() {
        // Ottieni il mese corrente
        YearMonth yearMonth = YearMonth.of(meseSelezionato.getYear(), meseSelezionato.getMonth());
        
        // Per ogni giorno del mese
        for (int i = 0; i < dayButtons.size(); i++) {
            JToggleButton button = dayButtons.get(i);
            LocalDate date = yearMonth.atDay(i + 1);

            // Aggiorna la disponibilità solo se il pulsante è abilitato (non è una data preclusa)
            if (button.isEnabled()) {
                volontariController.aggiornaDisponibilita(volontarioCorrente, date, button.isSelected());
            }
            volontariController.salvaDisponibilita();
        }

        JOptionPane.showMessageDialog(this, 
            "Disponibilità salvata con successo!", 
            "Successo", 
            JOptionPane.INFORMATION_MESSAGE);
    }


    private void updateButtonAppearance(JToggleButton button) {
        // Setup rounded borders
        int radius = 10; // Radius for rounded corners
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(radius),
            BorderFactory.createEmptyBorder(4, 8, 4, 8) // Inner padding
        ));
        
        // Remove default button styling
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        if (!button.isEnabled()) {
            // Precluded date: Red background with white text
            button.setBackground(new Color(178, 34, 34)); // dark red background
            button.setForeground(Color.WHITE);
            button.setFont(button.getFont().deriveFont(Font.BOLD));
        } else if (button.isSelected()) {
            // Selected date: Green background with white text
            button.setBackground(new Color(46, 139, 87)); // dark green background
            button.setForeground(Color.WHITE);
            button.setFont(button.getFont().deriveFont(Font.BOLD));
        } else {
            // Normal date: Same background as the panel with black text
            button.setBackground(Costants.BACKGROUND_COLOR); // Match the background color of the panel
            button.setForeground(Color.BLACK);
            button.setFont(button.getFont().deriveFont(Font.PLAIN));
        }
        
        // Override the UI to prevent gray overlay
        button.setUI(new BasicToggleButtonUI() {
            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
                // Do not paint the default pressed state
            }
        });
    }
    
    private void aggiornaCalendario() {


        // Rimuovi i pulsanti esistenti
        for (JToggleButton button : dayButtons) {
            calendarPanel.remove(button);
        }
        dayButtons.clear();
        
        // Ottieni il mese corrente
        YearMonth yearMonth = YearMonth.of(meseSelezionato.getYear(), meseSelezionato.getMonth());
        LocalDate firstOfMonth = yearMonth.atDay(1);
        
        // Determina il giorno della settimana del primo giorno del mese (1 = Lunedì, 7 = Domenica)
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        
        // Aggiungi spazi vuoti per i giorni prima del primo del mese
        for (int i = 1; i < dayOfWeek; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(Costants.BACKGROUND_COLOR); // Match background color
            calendarPanel.add(emptyPanel);
        }
        
        // Aggiungi i pulsanti per ogni giorno del mese
        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            LocalDate date = yearMonth.atDay(i);
            final JToggleButton dayButton = new JToggleButton(String.valueOf(i)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    
                    // Draw rounded rectangle as background
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                    
                    // Let the button's UI paint the text and other components
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            
            dayButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    
            // Controlla se la data è preclusa
            if (calendarioController.isDataPreclusa(date)) {
                dayButton.setEnabled(false);
                dayButton.setToolTipText("Data Preclusa");
                dayButton.setSelected(false); // Assicura che non sia selezionato
            } else {
                // Se non è preclusa, gestisci la disponibilità normale
                // Controlla se il volontario è disponibile per questa data
                boolean isDisponibile = volontariController.isDisponibile(volontarioCorrente, date);
                dayButton.setSelected(isDisponibile);
                
                // Aggiungi un listener per cambiare colore e testo al click
                dayButton.addItemListener(e -> {
                    updateButtonAppearance((JToggleButton) e.getSource());
                });
            }


            if(volontariController.isDisponibile(date, username)) {
                dayButton.setSelected(true);
            }

    
            // Aggiorna l'aspetto del pulsante in base al suo stato
            updateButtonAppearance(dayButton);
            
            // Aggiungi il pulsante alla lista e al pannello
            dayButtons.add(dayButton);
            calendarPanel.add(dayButton);
        }
        
        // Imposta il colore di sfondo del pannello calendario
        calendarPanel.setBackground(Costants.BACKGROUND_COLOR);
        
        // Aggiorna l'interfaccia
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
    
    
    
    

    
}

// Custom rounded border class
class RoundedBorder implements Border {
    private int radius;
    
    RoundedBorder(int radius) {
        this.radius = radius;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getBackground().darker());
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius / 2, this.radius / 2, this.radius / 2, this.radius / 2);
    }
    
    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
