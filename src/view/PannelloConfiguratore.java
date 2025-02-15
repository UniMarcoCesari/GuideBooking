package view;

import costants.Costants;
import javax.swing.*;
import java.awt.*;

public class PannelloConfiguratore extends JFrame {

    public PannelloConfiguratore() {
        setTitle("Sistema Gestione Visite");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(30, 30));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Header (Logo + Titolo)
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setOpaque(true);
        headerPanel.setBackground(Costants.BACKGROUND_COLOR);

        JLabel logoLabel = new JLabel("🏥");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Costants.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Sistema di Gestione Visite");
        titleLabel.setFont(Costants.TITLE_FONT);
        titleLabel.setForeground(Costants.ACCENT_COLOR);

        JLabel subtitleLabel = new JLabel("Pannello di Controllo Amministratore");
        subtitleLabel.setFont(Costants.BUTTON_FONT);
        subtitleLabel.setForeground(Costants.BORDER_COLOR);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Pannello centrale con bottoni
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBackground(Costants.BACKGROUND_COLOR);

        JButton manageLocationsButton = Costants.createMenuButton("Gestione Luoghi", "🏢");
        JButton manageVisitsButton = Costants.createMenuButton("Gestione Visite", "📋");
        JButton logoutButton = Costants.createMenuButton("Logout", "🚪");

        buttonPanel.add(manageLocationsButton);
        buttonPanel.add(manageVisitsButton);
        buttonPanel.add(logoutButton);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Costants.BACKGROUND_COLOR);
        centerPanel.add(buttonPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Costants.BACKGROUND_COLOR);
        JLabel footerLabel = new JLabel("© 2025 Sistema Gestione Visite - v1.0");
        footerLabel.setFont(Costants.BUTTON_FONT);
        footerLabel.setForeground(Costants.BORDER_COLOR);
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Event Listeners
        manageLocationsButton.addActionListener(e -> {
            dispose();
            new LuoghiFrame().setVisible(true);
        });

        manageVisitsButton.addActionListener(e -> {
            dispose();
            //new VisiteFrame().setVisible(true);
            //TODO--> gestione tipo visita
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(mainPanel);
        setVisible(true);
    }
}
