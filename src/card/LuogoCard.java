package card;

import controller.LuoghiController;
import model.Luogo;
import model.TipoVisita;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class LuogoCard extends JPanel {
    private final Luogo luogo;
    private boolean isSelected = false;
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color SELECTED_COLOR = new Color(49, 130, 189);

    public LuogoCard(Luogo luogo) {
        this.luogo = luogo;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 5));
        setBackground(Color.WHITE);
        setBorder(createDefaultBorder());
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Titolo e descrizione
        JPanel infoPanel = new JPanel(new BorderLayout(5, 2));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(luogo.getNome());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setFocusable(false); // Prevent text selection
        
        JTextArea descriptionArea = new JTextArea(luogo.getDescrizione());
        descriptionArea.setEditable(false);
        descriptionArea.setFocusable(false); // Prevent text selection
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setBorder(null);
        
        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(descriptionArea, BorderLayout.CENTER);
        
        // Posizione
        JLabel positionLabel = new JLabel("Posizione: " + luogo.getPosizione());
        positionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        positionLabel.setFocusable(false); // Prevent text selection
        infoPanel.add(positionLabel, BorderLayout.SOUTH);
        
        // Tipi di visita
        JPanel tipiVisitaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        tipiVisitaPanel.setBackground(Color.WHITE);
        tipiVisitaPanel.setBorder(new EmptyBorder(2, 0, 0, 0));
        
        JLabel tipiLabel = new JLabel("Tipi di visita: ");
        tipiLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tipiLabel.setFocusable(false); // Prevent text selection
        tipiVisitaPanel.add(tipiLabel);
        
        List<TipoVisita> tipiVisita = luogo.getTipiVisita();
        if (tipiVisita != null && !tipiVisita.isEmpty()) {
            for (int i = 0; i < tipiVisita.size(); i++) {
                if (i > 0) {
                    tipiVisitaPanel.add(new JLabel(", "));
                }
                JLabel tagLabel = new JLabel(tipiVisita.get(i).getTitolo());
                tagLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                tagLabel.setFocusable(false); // Prevent text selection
                tipiVisitaPanel.add(tagLabel);
            }
        } else {
            tipiVisitaPanel.add(new JLabel("Nessuno"));
        }
        
        // Aggiungi tutto al pannello principale
        add(infoPanel, BorderLayout.CENTER);
        add(tipiVisitaPanel, BorderLayout.SOUTH);
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (selected) {
            setBorder(new CompoundBorder(
                new LineBorder(SELECTED_COLOR, 2),
                new EmptyBorder(8, 8, 8, 8)
            ));
        } else {
            setBorder(createDefaultBorder());
        }
        repaint();
    }
    
    private CompoundBorder createDefaultBorder() {
        return new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(10, 10, 10, 10)
        );
    }
    
    public boolean isSelected() {
        return isSelected;
    }

    public Luogo getLuogo() {
        return luogo;
    }
}
