package controller;

import costants.Costants;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Volontario;

public class VolontariController {
    private ArrayList<Volontario> listaVolontari;
    private Map<LocalDate, List<Volontario>> disponibilitaVolontari;

    public VolontariController() {
        listaVolontari = caricaDati();
        disponibilitaVolontari = new HashMap<>();
    }

    public ArrayList<Volontario> getListaVolontari() {
        caricaDati();
        return listaVolontari;
    }

    public Map<LocalDate, List<Volontario>> getDisponibilitaVolontari() {
        return disponibilitaVolontari;
    }

    public void setDisponibilitaVolontari(Map<LocalDate, List<Volontario>> disponibilitaVolontari) {
        this.disponibilitaVolontari = disponibilitaVolontari;
    }

    public boolean aggiungiVolontario(Volontario volontario) {

        // se esiste volontatio con stesso nome
        for (int i = 0; i < listaVolontari.size(); i++) {
            if (listaVolontari.get(i).getNome().equals(volontario.getNome())) {
                return false;
            }
        }

        listaVolontari.add(volontario);
        salvaDati();
        return true;
    }

    public void rimuoviVolonatario(Volontario volontario) {
        listaVolontari.remove(volontario);
        salvaDati();
    }

    public void aggiornaDisponibilita(Volontario volontario, LocalDate data, boolean disponibile) {
        // Se la data non è presente nella mappa, la inizializziamo con una lista vuota
        disponibilitaVolontari.putIfAbsent(data, new ArrayList<>());
    
        List<Volontario> volontariDisponibili = disponibilitaVolontari.get(data);
    
        if (disponibile) {
            // Aggiungiamo il volontario solo se non è già presente nella lista
            if (!volontariDisponibili.contains(volontario)) {
                volontariDisponibili.add(volontario);
            }
        } else {
            // Rimuoviamo il volontario dalla lista di disponibilità per quella data
            volontariDisponibili.remove(volontario);
            
            // Se la lista diventa vuota, possiamo rimuovere la chiave dalla mappa per ottimizzare la memoria
            if (volontariDisponibili.isEmpty()) {
                disponibilitaVolontari.remove(data);
            }
        }
    }    

    public void salvaDati() {
        System.out.println("salvati i seguenti tipi di visita");
        for (int i = 0; i < listaVolontari.size(); i++) {
            System.out.println(listaVolontari.get(i).getNome());
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_volontari))) {
            oos.writeObject(listaVolontari);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @SuppressWarnings("unchecked")
    private ArrayList<Volontario> caricaDati() {
        File file = new File(Costants.file_volontari);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<Volontario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
