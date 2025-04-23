package view.configuratore;

import controller.CalendarioController;
import controller.LuoghiController;
import controller.TipiVisitaController;
import controller.VisiteController;
import controller.VolontariController;
import costants.Costants;
import model.CorpoDati;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import card.VisitaCard;
import model.Visita;

public class Sezione1 extends JFrame {
    private final LocalDate mese;
    VisiteController visiteController;
    CalendarioController calendarioController;

    public Sezione1(VisiteController visiteController, CalendarioController calendarioController) {
        this.visiteController = visiteController;
        this.calendarioController = calendarioController;

        initializeFrame();
        mese = calendarioController.getNomeMesePrimoCheSiPuoModificare(); //prende mese
        JPanel mainPanel = new JPanel(new BorderLayout(Costants.SPACING, Costants.SPACING));
        mainPanel.setBackground(Costants.BACKGROUND_COLOR);

        // Header
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ITALIAN);
        JPanel headerPanel = Costants.createHeaderPanel("Visite per " + mese.format(formatter));
        JButton indietro = Costants.createSimpleButton("Indietro");
        indietro.addActionListener(e -> {
            dispose();
            new PannelloConfiguratore().setVisible(true);
        });
        headerPanel.add(indietro, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale
        JPanel mainContentPanel = new JPanel(new FlowLayout());
        List<Visita> visite = visiteController.getVisite();
        for (Visita visita : visite) {
            VisitaCard visitaCard = new VisitaCard(visita);
            mainContentPanel.add(visitaCard);
        }

        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        // Footer con genera visite
        JPanel footerPanel = Costants.createFooterPanel("");
        JButton generaVisiteButton = Costants.createSimpleButton("Genera Visite");
        generaVisiteButton.addActionListener(e -> {
            // genera le visite
            visiteController.generaVisite();
            // ricarica la pagina per vedere cambiamenti
            dispose();
            new Sezione1(visiteController, calendarioController).setVisible(true);
        });

        // disabilita il bottone se non siamo nel giorno di generazione
        if(!calendarioController.isGiornoDiGenerazioneVisite())
        {
            generaVisiteButton.setEnabled(false);
        }

        footerPanel.add(generaVisiteButton, BorderLayout.EAST);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initializeFrame() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
