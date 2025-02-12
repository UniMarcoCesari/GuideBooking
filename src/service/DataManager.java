package service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    /**
     * Salva una lista di oggetti serializzabili in un file.
     *
     * @param dati     Lista di oggetti da salvare.
     * @param filename Nome del file in cui salvare i dati.
     * @param <T>      Tipo generico degli oggetti nella lista.
     */
    public static <T extends Serializable> void salvaDati(List<T> dati, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dati);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica una lista di oggetti da un file.
     * Se il file non esiste, lo crea e restituisce una lista vuota.
     *
     * @param filename Nome del file da cui caricare i dati.
     * @param <T>      Tipo generico degli oggetti da caricare.
     * @return Lista di oggetti caricati oppure una lista vuota in caso di errore.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> caricaDati(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("[INFO] File dati non trovato, creazione di un nuovo file...");
            salvaDati(new ArrayList<>(), filename);
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
