package view.volontario;

import javax.swing.*;
import java.awt.*;

public class VisualizzaTipiVisitaVolontarioFrame extends JFrame {

    private String username;

    public VisualizzaTipiVisitaVolontarioFrame(String username) {
        this.username = username;
        setTitle("Visualizza Tipi Visita Associati - " + username);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame

        // Placeholder content
        JLabel label = new JLabel("TODO: Implementare visualizzazione tipi visita per " + username);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        // TODO: Add logic to fetch and display visit types associated with the volunteer

        setVisible(true);
    }

    // Optional: Add a main method for testing this frame directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VisualizzaTipiVisitaVolontarioFrame("testVolontario"));
    }
}
