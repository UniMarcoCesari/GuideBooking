package service;

import model.Calendario;
import model.CorpoDati;

import java.io.*;
import java.time.LocalDate;
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
    private static <T extends Serializable> List<T> caricaDatiLista(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("[INFO] File dati");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void salvaDati(Calendario dati, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dati);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Serializable> T caricaDati(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void salvaCorpoDati(CorpoDati dati, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dati);
            System.out.println("[INFO] Dati salvati correttamente in " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static CorpoDati caricaCorpoDati(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (CorpoDati) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void salvaDatePrecluse(List<LocalDate> date, String percorsoFile) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(percorsoFile))) {
            oos.writeObject(new ArrayList<>(date));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<LocalDate> caricaDatePrecluse(String percorsoFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(percorsoFile))) {
            return (List<LocalDate>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Se il file non esiste, restituisci una lista vuota
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }


    }

    }
