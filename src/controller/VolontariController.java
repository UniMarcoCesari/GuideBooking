package controller;

import costants.Costants;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Volontario;
import costants.Credenziale;
import java.util.List;

public class VolontariController {

    public static void creaCredenzialiVolontari(String username) {
        List<Credenziale> credenziali = CredenzialeWriter.caricaCredenziali();
        Credenziale nuovaCredenziale = new Credenziale(username, "pw", Costants.ruolo_pre_volontario);
        credenziali.add(nuovaCredenziale);
        CredenzialeWriter.salvaCredenziali(credenziali);
    }
    private ArrayList<Volontario> listaVolontari;
    private Map<LocalDate, Map<String, Boolean>> disponibilitaVolontari;

    public VolontariController() {
        listaVolontari = caricaDati();
        disponibilitaVolontari = new HashMap<>();
    }

    public boolean isDisponibile(Volontario volontario, LocalDate data) {
        if (!disponibilitaVolontari.containsKey(data)) return false;
        return disponibilitaVolontari.get(data).getOrDefault(volontario.getNome(), false);
    }

    public ArrayList<Volontario> getListaVolontari() {
        caricaDati();
        return listaVolontari;
    }

    public Map<LocalDate, Map<String, Boolean>> getDisponibilitaVolontari() {
        return disponibilitaVolontari;
    }

    public void setDisponibilitaVolontari(Map<LocalDate, Map<String, Boolean>> disponibilitaVolontari) {
        this.disponibilitaVolontari = disponibilitaVolontari;
    }


    /**
     * Aggiunge un nuovo volontario alla lista controllando se esiste già.
     * Mostra un messaggio di errore se il volontario esiste già.
     *
     * @param username L'username del nuovo volontario.
     * @return true se il volontario è stato aggiunto con successo, false altrimenti.
     */
    public boolean aggiungiVolontario(String username) {
        // Controlla se esiste già un volontario con lo stesso username (ignorando maiuscole/minuscole)
        for (Volontario esistente : listaVolontari) {
            if (esistente.getNome().equalsIgnoreCase(username)) {
                // Utilizza System.err per stampare il messaggio di errore
                System.err.println("Esiste già un volontario con l'username '" + username + "'.");
                return false; // Volontario già esistente
            }
        }

        // Se non esiste, crea e aggiungi il nuovo volontario
        Volontario nuovoVolontario = new Volontario(username);
        listaVolontari.add(nuovoVolontario);
        salvaVolontari(); // Salva la lista aggiornata
        creaCredenzialiVolontari(nuovoVolontario.getNome());
        return true; // Volontario aggiunto con successo
    }

    public void rimuoviVolonatario(Volontario volontario) {
        listaVolontari.remove(volontario);
        salvaVolontari();
    }

    public void aggiornaDisponibilita(Volontario volontario, LocalDate data, boolean disponibile) {
        disponibilitaVolontari.putIfAbsent(data, new HashMap<>());
        disponibilitaVolontari.get(data).put(volontario.getNome(), disponibile);
    }

    public void salvaDisponibilita() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_disponibilita_volontari))) {
            oos.writeObject(disponibilitaVolontari);
            System.out.println("salvata disponibilita");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void caricaDisponibilita() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Costants.file_disponibilita_volontari))) {
            disponibilitaVolontari = (Map<LocalDate, Map<String, Boolean>>) ois.readObject();
            System.out.println("Disponibilita caricata");
        } catch (IOException | ClassNotFoundException e) {
            disponibilitaVolontari = new HashMap<>();
            System.out.println("Disponibilita vuota inizializata");
        }
    }

    public boolean isDisponibile(LocalDate data, String username) {
        if (disponibilitaVolontari == null || disponibilitaVolontari.isEmpty()) caricaDisponibilita();
        return disponibilitaVolontari.containsKey(data) &&
               disponibilitaVolontari.get(data).getOrDefault(username, false);
    }

    public void salvaVolontari() {
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

    public void rimuoviVolontario(Volontario volontario) {
        listaVolontari.remove(volontario);
        salvaVolontari();
    }
}
