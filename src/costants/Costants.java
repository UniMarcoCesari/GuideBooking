package costants;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Costants
{

    //File salvataggio dati
//    public static final String  file_luoghi = "GuideBooking/src/data/luoghi.dat";
//    public static final String  file_credenziali = "GuideBooking/src/data/credenziali.dat";
//    public static final String  file_tipi_visita = "GuideBooking/src/data/tipiVisita.dat";
//    public static final String  file_volontari = "GuideBooking/src/data/volontari.dat";

      public static final String  file_luoghi = "src/data/luoghi.dat";
      public static final String  file_credenziali = "src/data/credenziali.dat";
      public static final String  file_tipi_visita = "src/data/tipiVisita.dat";
      public static final String  file_volontari = "src/data/volontari.dat";


    // Header
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Color HEADER_BACK = new Color(100, 100, 200);


    // Ruoli
    public static final String ruolo_PRE_configuratore =  "PRE-configuratore";
    public static final String ruolo_configuratore =  "configuratore";

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
        return button;
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

}
