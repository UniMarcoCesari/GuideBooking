package view.configuratore;

import controller.CalendarioController;
import costants.Costants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DatePrecluseSezione extends JFrame {
    private static final Locale ITALIAN_LOCALE = Locale.ITALIAN;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", ITALIAN_LOCALE);

    private final LocalDate selectedMonth;
    private final DefaultListModel<String> dateListModel = new DefaultListModel<>();
    private JTextField dateInputField;
    private final CalendarioController calendarioController;

    public DatePrecluseSezione(CalendarioController calendarioController) {
        this.calendarioController = calendarioController;
        this.selectedMonth = calculateSelectedMonth();

        setupUserInterface();
        initializeFrame();

        // Carica immediatamente le date precluse quando la schermata viene aperta
        loadPrecludedDates();
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
        JPanel mainPanel = createMainPanel();
        add(mainPanel);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createMainContentPanel(), BorderLayout.CENTER);
        mainPanel.add(Costants.createFooterPanel("Footer"), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createHeaderPanel() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", ITALIAN_LOCALE);
        String monthName = selectedMonth.format(formatter);
        JPanel headerPanel = Costants.createHeaderPanel("Inserisci date per il mese di " + monthName);

        JButton backButton = Costants.createSimpleButton("Indietro");
        backButton.addActionListener(e -> navigateBack());
        headerPanel.add(backButton, BorderLayout.WEST);

        return headerPanel;
    }

   private JPanel createMainContentPanel() {
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(createInputPanel(), BorderLayout.NORTH);
        mainContentPanel.add(createDateListPanel(), BorderLayout.CENTER);
        return mainContentPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel dateLabel = new JLabel("Inserisci data (dd/MM/yyyy):");
        dateInputField = new JTextField(10);
        JButton addButton = Costants.createSimpleButton("Aggiungi");
        addButton.addActionListener(e -> addDate());

        inputPanel.add(dateLabel);
        inputPanel.add(dateInputField);
        inputPanel.add(addButton);

        return inputPanel;
    }

    private JScrollPane createDateListPanel() {
        JList<String> dateList = new JList<>(dateListModel);
        JScrollPane scrollPane = new JScrollPane(dateList);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        return scrollPane;
    }

    private void addDate() {
        String input = dateInputField.getText().trim();

        try {
            LocalDate inputDate = LocalDate.parse(input, DATE_FORMATTER);
            validateAndAddDate(inputDate);
        } catch (Exception e) {
            showDateFormatError();
        }
    }

    private void validateAndAddDate(LocalDate inputDate) {
        // Verifica se la data è nel mese selezionato
        if (inputDate.getMonth() != selectedMonth.getMonth()) {
            showMonthMismatchError();
            return;
        }

        // Controlla se la data è già presente nella lista
        String formattedDate = inputDate.format(DATE_FORMATTER);
        if (isDuplicateDate(formattedDate)) {
            showDuplicateDateError();
            return;
        }

        // Aggiungi la data alla lista e al controller
        dateListModel.addElement(formattedDate);
        calendarioController.aggiungiDataPreclusa(inputDate);
        dateInputField.setText("");
    }

    private boolean isDuplicateDate(String date) {
        return dateListModel.contains(date);
    }

    private void loadPrecludedDates() {
        // Pulisci prima la lista esistente
        dateListModel.clear();

        // Carica le date precluse per il mese selezionato
        List<LocalDate> precludedDates = calendarioController.getDatePrecluse(selectedMonth);
        precludedDates.forEach(date -> dateListModel.addElement(date.format(DATE_FORMATTER)));
    }

    private void navigateBack() {
        dispose();
        new PannelloConfiguratore().setVisible(true);
    }

    private void showDateFormatError() {
        JOptionPane.showMessageDialog(
                this,
                "Formato data non valido. Usa dd/MM/yyyy (es. 15/03/2025).",
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showMonthMismatchError() {
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", ITALIAN_LOCALE);
        String monthName = selectedMonth.format(formatter);
        JOptionPane.showMessageDialog(
                this,
                "La data non è nel mese di " + monthName,
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showDuplicateDateError() {
        JOptionPane.showMessageDialog(
                this,
                "Questa data è già presente nell'elenco.",
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
