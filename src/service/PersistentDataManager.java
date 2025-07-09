package service;

import java.io.*;
import java.io.Serializable;
import java.util.List;

public class PersistentDataManager {

    public PersistentDataManager() {}

    /**
     * Salva un singolo oggetto serializzabile in un file, sovrascrivendolo.
     * @param oggetto L'oggetto da salvare.
     * @param filePath Il percorso del file.
     * @param <T> Il tipo dell'oggetto, che deve essere Serializable.
     */
    public <T extends Serializable> void salvaOggetto(T oggetto, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(oggetto);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio nel file: " + filePath);
            e.printStackTrace();
        }
    }


    /**
     * Salva una lista di oggetti serializzabili in un file, sovrascrivendolo.
     * @param lista La lista di oggetti da salvare.
     * @param filePath Il percorso del file.
     * @param <T> Il tipo degli oggetti, che deve essere Serializable.
     */
    public <T extends Serializable> void salvaListaOggetti(List<T> lista, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio nel file: " + filePath);
            e.printStackTrace();
        }
    }

/**
 * Carica una lista di oggetti serializzabili da un file.
 * @param filePath Il percorso del file.
 * @param <T> Il tipo atteso degli oggetti nella lista.
 * @return La lista di oggetti caricati, o null se il file non esiste o si verifica un errore.
 */
public <T extends Serializable> List<T> caricaListaOggetti(String filePath) {
    File file = new File(filePath);
    if (!file.exists()) {
        return null; // Il file non esiste
    }

    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        return (List<T>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
        System.err.println("Errore durante il caricamento dal file: " + filePath);
        e.printStackTrace();
        return null; // Restituisce null in caso di errore
    }
}


    /**
     * Carica un singolo oggetto serializzabile da un file.
     * @param filePath Il percorso del file.
     * @param <T> Il tipo atteso dell'oggetto.
     * @return L'oggetto caricato, o null se il file non esiste o si verifica un errore.
     */
    public <T extends Serializable> T caricaOggetto(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null; // Il file non esiste, quindi non c'è nessun oggetto da caricare
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento dal file: " + filePath);
            e.printStackTrace();
            return null; // Restituisce null in caso di errore
        }
    }
}