package view.volontario;

import javax.swing.*;
import java.awt.*;

public class GestisciDisponibilitaFrame extends JFrame {

    private String username;

    public GestisciDisponibilitaFrame(String username) {
        this.username = username;
        setTitle("Gestisci Disponibilità - " + username);
        setSize(800, 600); // Larger frame for calendar potentially
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame

        // Placeholder content
        JLabel label = new JLabel("TODO: Implementare gestione disponibilità per " + username);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        // TODO: Add calendar component and logic to manage availability

        setVisible(true);
    }

    // Optional: Add a main method for testing this frame directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestisciDisponibilitaFrame("testVolontario"));
    }
}
