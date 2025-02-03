package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    public MainFrame() {

        setTitle("Pannello Configuratore");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(220, 240, 250));

        JLabel welcomeLabel = new JLabel("Benvenuto nel sistema di gestione visite", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton manageLocationsButton = new JButton("Gestisci Luoghi");
        JButton manageVisitsButton = new JButton("Gestisci Visite");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(manageLocationsButton);
        buttonPanel.add(manageVisitsButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.CENTER);
        /*
        Gestione bottoni
         */

        //TO VISITE
        manageLocationsButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               dispose();
               new LuoghiFrame().setVisible(true);
           }
        });


        //LOGOUT BUTTON
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        add(panel);
        setVisible(true);
    }
}
