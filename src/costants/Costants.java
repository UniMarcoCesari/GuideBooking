package costants;

import java.awt.*;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Costants
{

    public static final String file_luoghi = Paths.get("src", "data", "luoghi.dat").toString();
    public static final String file_credenziali = Paths.get("src", "data", "credenziali.dat").toString();
    public static final String file_tipi_visita = Paths.get("src", "data", "tipiVisita.dat").toString();
    public static final String file_volontari = Paths.get("src", "data", "volontari.dat").toString();
    public static final String file_corpo = Paths.get("src", "data", "corpo.dat").toString();
    public static String file_date = Paths.get("src", "data", "date.dat").toString();
    public static String file_disponibilita_volontari = Paths.get("src", "data", "disponibilitaVolontari.dat").toString();
    public static String file_visite = Paths.get("src", "data", "visite.dat").toString();


    // Header
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Color HEADER_BACK = new Color(100, 100, 200); // Default/Configuratore header
    public static final Color VOLONTARIO_HEADER_BACK = new Color(204, 102, 0); // Dark Orange for Volontario
    public static final Color FRUITORE_HEADER_BACK = new Color(102, 0, 204); // Dark Purple for Fruitore
    public static final Color CONFIGURATORE_HEADER_BACK = new Color(0, 127, 255); // BarkBlue for Configuratore


    // Colori principali
    public static final Color ACCENT_COLOR = new Color(55, 65, 81); // Grigio scuro testo
    public static final Color BORDER_COLOR = new Color(156, 163, 175); // Bordo grigio
    public static final Color HOVER_COLOR = Color.BLACK; // Nero hover
    public static final Color BUTTON_BACKGROUND = Color.WHITE; // Pulsanti bianchi
    public static final Color BACKGROUND_COLOR = new Color(240, 244, 248); // Sfondo generale

    // Font
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);

    // Margin e Padding
    public static final int SPACING = 20;

    // Date e simili

    // Formattatore di date per l'italiano
    public static final DateTimeFormatter ITALIAN_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALY);
    // Metodo per formattare una data in italiano
    public static String formatToItalian(LocalDate date) {
        return date.format(ITALIAN_DATE_FORMATTER);
    }


    public static JPanel createHeaderPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(HEADER_BACK);
        panel.setBorder(new EmptyBorder(10, SPACING, 10, SPACING));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    public static JPanel createFooterPanel(String title) {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Costants.HEADER_BACK);
        footerPanel.setPreferredSize(new Dimension(0, 40));
        if(!title.isEmpty()) {
            JLabel footerLabel = new JLabel(title);
            footerLabel.setFont(Costants.TITLE_FONT);
            footerLabel.setForeground(Color.WHITE);
            footerPanel.add(footerLabel);
        }
        return footerPanel;
    }

    public static JButton createSimpleButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setOpaque(true);
        button.setBackground(BACKGROUND_COLOR);
        return button;
    }


    public static JButton creaBottoneLogOut() {
        URL resource = Costants.class.getResource("/immagini/logout.png");
        ImageIcon iconaIndietro = resource != null ? new ImageIcon(resource) : null;
        if(iconaIndietro != null) {
            Image image = iconaIndietro.getImage(); // transform it 
            iconaIndietro = new ImageIcon(image);  // transform it back
        }

        JButton bottone = new JButton(iconaIndietro);
        bottone.setForeground(Color.WHITE);
        bottone.setFocusPainted(false);
        bottone.setContentAreaFilled(false);
        bottone.setBorderPainted(false);

        return bottone;
    }
    

    // Metodo per creare bottoni con lo stesso stile
    public static JButton createMenuButton(String text) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        button.setBackground(BUTTON_BACKGROUND);
        button.setLayout(new BorderLayout(15, 0));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(BUTTON_FONT);

        button.add(textLabel, BorderLayout.CENTER);

        button.setPreferredSize(new Dimension(300, 80));

        // Bordo iniziale
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 25)
        ));

        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effetti hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 240));
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(HOVER_COLOR, 2, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 25)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BACKGROUND);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_COLOR, 2, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 25)
                ));
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BACKGROUND);
            }
        });

        return button;
    }


    public static JButton createLogoutButton(String string) {
        JButton button = new JButton(string);
        button.setFont(new Font("Dialog", Font.BOLD, 12));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(BACKGROUND_COLOR);
            }
        });

        return button;
    }

}
