package controller;

import model.TipoVisita;
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
}
