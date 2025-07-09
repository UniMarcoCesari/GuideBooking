package view.configuratore;

import controller.CalendarioController;
import costants.Costants;
import view.login.MainController;
import controller.CalendarioController;
import costants.Costants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatePrecluseSezione extends JFrame {
    private static final Locale ITALIAN_LOCALE = Locale.ITALIAN;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", ITALIAN_LOCALE); // Keep for potential future use, but not primary now

    private LocalDate selectedMonth;
    private final CalendarioController calendarioController;
    private final MainController mainController;
    private JPanel calendarPanel;
    private JLabel meseLabel;
    private List<JToggleButton> dayButtons = new ArrayList<>();
    // Removed: DefaultListModel<String> dateListModel = new DefaultListModel<>();
    // Removed: JTextField dateInputField;


    public DatePrecluseSezione(MainController mainController) {
        this.mainController = mainController;
        this.calendarioController = mainController.getCalendarioController();
        this.selectedMonth = calculateSelectedMonth();

        setupUserInterface();
        initializeFrame();
        setupUserInterface(); // Call setupUI after initializing frame components

        // No need to call loadPrecludedDates separately, aggiornaCalendario will handle it
    }

     private LocalDate calculateSelectedMonth() {
        return calendarioController.getNomeMesePrimoCheSiPuoModificare().plusMonths(2);
    }

    private void initializeFrame() {
        setTitle("Configurazione Date");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setupUserInterface() {
        // Removed erroneous call: JPanel mainPanel = createMainPanel();
        // Main Panel setup
        // Removed duplicate declaration: JPanel mainPanel = new JPanel(...); -> Use the one below
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);


        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);

        // Titolo al centro
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", ITALIAN_LOCALE);
        String monthName = selectedMonth.format(formatter);
        JLabel titolo = new JLabel("Date precluse "+monthName, SwingConstants.CENTER);
        titolo.setForeground(Color.WHITE);
        titolo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titolo, BorderLayout.CENTER);

        // Bottone Logout a destra
        JButton logoutButton = Costants.creaBottoneLogOut();
        logoutButton.addActionListener(e -> {
            mainController.showPannelloConfiguratore();
        });
        
        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRightPanel.setOpaque(false);
        headerRightPanel.add(logoutButton);
        headerPanel.add(headerRightPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Panel (Calendar View)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Month Panel (Label only, no navigation buttons for now)
        JPanel monthPanel = new JPanel(new BorderLayout());
        monthPanel.setBackground(Costants.BACKGROUND_COLOR);

        String nomeMese = selectedMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
        String annoMese = String.valueOf(selectedMonth.getYear());
        meseLabel = new JLabel(nomeMese.toUpperCase() + " " + annoMese);
        meseLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        meseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthPanel.add(meseLabel, BorderLayout.CENTER);

        contentPanel.add(monthPanel, BorderLayout.NORTH);

        // Calendar Panel (Grid)
        calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5)); // 0 rows means flexible rows
        calendarPanel.setBackground(Costants.BACKGROUND_COLOR);
        calendarPanel.setBorder(new EmptyBorder(Costants.SPACING, 0, Costants.SPACING, 0)); // Add some vertical padding


        // Add Day Labels (Mon, Tue, etc.)
        String[] giorni = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
        for (String giorno : giorni) {
            JLabel label = new JLabel(giorno, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            calendarPanel.add(label);
        }

        // Populate calendar with days (will be done in aggiornaCalendario)
        aggiornaCalendario(); // Call this to fill the grid

        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);


        // Footer Panel
        JPanel footerPanel = Costants.createFooterPanel(""); // Updated footer text
        footerPanel.setBackground(Costants.CONFIGURATORE_HEADER_BACK);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel); // Add the main panel to the JFrame
    }


    // --- New methods for Calendar View ---

    private void aggiornaCalendario() {
        // Clear previous buttons and components after the day labels
        Component[] components = calendarPanel.getComponents();
        for (int i = 7; i < components.length; i++) { // Start from 7 to keep day labels
            calendarPanel.remove(components[i]);
        }
        dayButtons.clear();

        YearMonth yearMonth = YearMonth.from(selectedMonth);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

        // Add empty panels for days before the 1st of the month
        for (int i = 1; i < dayOfWeek; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(Costants.BACKGROUND_COLOR);
            calendarPanel.add(emptyPanel);
        }

        // Add buttons for each day of the month
        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            LocalDate date = yearMonth.atDay(i);
            final JToggleButton dayButton = new JToggleButton(String.valueOf(i)) {
                 // Custom painting for rounded background (copied logic)
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); // Adjust radius as needed
                    super.paintComponent(g);
                    g2.dispose();
                }
            };

            dayButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            dayButton.setFocusPainted(false); // Remove focus border

            boolean isPreclusa = calendarioController.isDataPreclusa(date);
            dayButton.setSelected(isPreclusa); // Select if precluded

            // Add listener to toggle preclusion status
            dayButton.addItemListener(e -> {
                JToggleButton source = (JToggleButton) e.getSource();
                togglePreclusa(date, source.isSelected()); // Pass date and new state
                updateButtonAppearance(source); // Update appearance immediately
            });

            updateButtonAppearance(dayButton); // Set initial appearance

            dayButtons.add(dayButton);
            calendarPanel.add(dayButton);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void updateButtonAppearance(JToggleButton button) {
        int radius = 10; // Radius for rounded corners
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(radius), // Use the custom rounded border
            BorderFactory.createEmptyBorder(4, 8, 4, 8) // Inner padding
        ));

        // Remove default button styling effects
        button.setContentAreaFilled(false); // Important for custom painting
        button.setOpaque(false); // Let background show through border/painting

        // Custom UI to prevent default pressed/selected visuals interfering
         button.setUI(new BasicToggleButtonUI() {
            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
                // Do nothing to avoid default gray overlay
            }
             // Optionally override paintFocus if focus ring is unwanted
        });


        if (button.isSelected()) { // Precluded date
            button.setBackground(new Color(220, 53, 69)); // Bootstrap danger red
            button.setForeground(Color.WHITE);
            button.setFont(button.getFont().deriveFont(Font.BOLD));
        } else { // Available date
            button.setBackground(Costants.BACKGROUND_COLOR); // Match panel background
            button.setForeground(Color.BLACK);
            button.setFont(button.getFont().deriveFont(Font.PLAIN));
        }
         // Ensure repaint happens if needed
         button.repaint();
    }

     private void togglePreclusa(LocalDate date, boolean isPreclusa) {
        if (isPreclusa) {
            calendarioController.aggiungiDataPreclusa(date);
            System.out.println("Added precluded date: " + date); // Log
        } else {
             calendarioController.rimuoviDataPreclusa(date);
             System.out.println("Removed precluded date: " + date); // Log
        }
    }

    // --- Keep navigateBack ---
    private void navigateBack() {
        mainController.showPannelloConfiguratore();
    }





    // --- Add RoundedBorder class (copied from GestisciDisponibilitaFrame) ---
    static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Use a subtle border color, maybe gray or derived from background
            g2.setColor(Color.GRAY); // Or c.getBackground().darker();
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            // Adjust insets based on radius to provide padding
            return new Insets(this.radius / 3, this.radius / 2, this.radius / 3, this.radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false; // Allows background to show through
        }
    }
}
