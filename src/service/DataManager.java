package service;

import model.Luogo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {


    //TODO--> Salvataggio manuale
    //        DataManager.salvaDati(luoghi, "luoghi.dat");


    public static void salvaDati(List<Luogo> dati, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dati);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



        public static List<?> caricaDati(String filename) {
            File file = new File(filename);

            // Se il file non esiste, crealo e restituisci una lista vuota
            if (!file.exists()) {
                System.out.println("[INFO] File dati non trovato, creazione di un nuovo file...");
                salvaDati(new ArrayList<>(), filename);
                return new ArrayList<>();
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<?>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
    }

