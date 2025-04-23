package controller;

import model.TipoVisita;
import model.Volontario;
import costants.Costants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TipiVisitaController {
    private List<TipoVisita> tipiVisita;

    public TipiVisitaController() {
        tipiVisita = caricaDati();
    }

    public List<TipoVisita> getTipiVisita() {
        caricaDati();
        return tipiVisita;
    }

    public void rimuoviTipoVisita(TipoVisita tipoVisita) {
        tipiVisita.remove(tipoVisita);
        salvaDati();
    }

    public TipoVisita getTipoVisita(String titolo) {
        caricaDati();

        for(TipoVisita tipoVisita: tipiVisita) {
            if(tipoVisita.getTitolo().equals(titolo)) {
                return tipoVisita;
            }
        }

        return null;
    }

    public void aggiungiVisita(TipoVisita visita) {
        tipiVisita.add(visita);
        salvaDati();
    }

    public void rimuoviVisita(TipoVisita visita) {
        tipiVisita.remove(visita);
        salvaDati();
    }

    public void salvaDati() {
        System.out.println("salvati i seguenti tipi di visita");
        for (int i = 0; i < tipiVisita.size(); i++) {
            System.out.println(tipiVisita.get(i).getTitolo());
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_tipi_visita))) {
            oos.writeObject(tipiVisita);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Ho salvato i seguenti tipi visita: "); 
        for (TipoVisita visita : tipiVisita) {
            System.out.print(visita.getTitolo());
            System.out.print(" Volontari: {");
            for (Volontario volontario : visita.getVolontari()) {
                System.out.print(volontario.getNome());
            }
            System.out.println("}");
        }
    }

    public boolean titoloEsiste(String titolo) {
        return tipiVisita.stream().anyMatch(visita -> visita.getTitolo().equalsIgnoreCase(titolo));
    }


    @SuppressWarnings("unchecked")
    private List<TipoVisita> caricaDati() {
        File file = new File(Costants.file_tipi_visita);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<TipoVisita>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<TipoVisita> getTipiVisitaPerVolontario(String username) {
        List<TipoVisita> tipiVisitaPerVolontario = new ArrayList<>();
        for (TipoVisita tipoVisita : tipiVisita) {
            if (tipoVisita.getVolontari().stream().anyMatch(volontario -> volontario.getNome().equalsIgnoreCase(username))) {
                tipiVisitaPerVolontario.add(tipoVisita);
            }
        }
        return tipiVisitaPerVolontario;
    }

    public void modificaTipoVisita(TipoVisita nuovaVisita) {
        for (TipoVisita tipoVisita : tipiVisita) {
            if (tipoVisita.getTitolo().equals(nuovaVisita.getTitolo())) {
                tipoVisita.setDescrizione(nuovaVisita.getDescrizione());
                tipoVisita.setPuntoIncontro(nuovaVisita.getPuntoIncontro());
                tipoVisita.setDataInizio(nuovaVisita.getDataInizio());
                tipoVisita.setDataFine(nuovaVisita.getDataFine());
                tipoVisita.setGiorniSettimana(nuovaVisita.getGiorniSettimana());
                tipoVisita.setOraInizio(nuovaVisita.getOraInizio());
                tipoVisita.setDurataMinuti(nuovaVisita.getDurataMinuti());
                tipoVisita.setBigliettoNecessario(nuovaVisita.isBigliettoNecessario());
                tipoVisita.setVolontari(nuovaVisita.getVolontari());
                tipoVisita.setMinPartecipanti(nuovaVisita.getMinPartecipanti());
                tipoVisita.setMaxPartecipanti(nuovaVisita.getMaxPartecipanti());
                break;
            }
        }
        salvaDati();
    }

    public void rimuoviVolonatario(Volontario volontario) {
        List<TipoVisita> tipiVisitaDaRimuovere = new ArrayList<>();
        for (TipoVisita tipoVisita : tipiVisita) {
            tipoVisita.rimuoviVolontario(volontario);
            if (tipoVisita.getVolontari().isEmpty()) {
                tipiVisitaDaRimuovere.add(tipoVisita);
            }
        }
        tipiVisita.removeAll(tipiVisitaDaRimuovere);
        LuoghiController luoghiController = new LuoghiController();
        luoghiController.rimuoviVolonatario(volontario,tipiVisitaDaRimuovere);
        salvaDati();
    }
}
