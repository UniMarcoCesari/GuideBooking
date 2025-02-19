package card;

import model.Luogo;
import controller.LuoghiController;
import view.LuoghiFrame;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LuogoCard extends JPanel {

    private static final int CARD_WIDTH = 450;
    private static final int CARD_HEIGHT = 80;
    private static final int BORDER_RADIUS = 15;

    private final JLabel nomeLabel;
    private final JLabel posizioneLabel;
    private final Luogo luogo;
    private final LuoghiController controller;
    private final LuoghiFrame frame;

    public LuogoCard(Luogo luogo, LuoghiController controller, LuoghiFrame frame) {
        this.luogo = luogo;
        this.controller = controller;
        this.frame = frame;

        setupPanel();

        nomeLabel = createNomeLabel();
        posizioneLabel = createPosizioneLabel();

        add(createInfoPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.EAST);
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setBorder(createCardBorder());
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    private Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER_RADIUS),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)

        );
    }

    private JLabel createNomeLabel() {
        JLabel label = new JLabel("<html><b>" + luogo.getNome() + "</b></html>");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JLabel createPosizioneLabel() {
        return new JLabel(luogo.getPosizione());
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setOpaque(false);
        panel.add(nomeLabel);
        panel.add(posizioneLabel);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        JButton modificaButton = new JButton("✏️ Modifica");
        JButton eliminaButton = new JButton("🗑️ Elimina");

        modificaButton.addActionListener(e -> modificaLuogo());
        eliminaButton.addActionListener(e -> eliminaLuogo());

        panel.add(modificaButton);
        panel.add(eliminaButton);

        return panel;
    }

    private void modificaLuogo() {
        String nuovoNome = JOptionPane.showInputDialog(this, "Modifica Nome:", luogo.getNome());
        String nuovaPosizione = JOptionPane.showInputDialog(this, "Modifica Posizione:", luogo.getPosizione());

        if (isValidInput(nuovoNome, nuovaPosizione)) {
            updateLuogo(nuovoNome, nuovaPosizione);
            controller.salvaDati();
            frame.aggiornaLista();
        }
    }

    private boolean isValidInput(String nome, String posizione) {
        return nome != null && !nome.trim().isEmpty() &&
                posizione != null && !posizione.trim().isEmpty();
    }

    private void updateLuogo(String nome, String posizione) {
        luogo.setNome(nome);
        luogo.setPosizione(posizione);
        nomeLabel.setText("<html><b>" + nome + "</b></html>");
        posizioneLabel.setText(posizione);
    }

    private void eliminaLuogo() {
        int conferma = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare questo luogo?",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            controller.getLuoghi().remove(luogo);
            controller.salvaDati();
            frame.aggiornaLista();
        }
    }
}

class RoundedBorder extends AbstractBorder {
    private final int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }
}