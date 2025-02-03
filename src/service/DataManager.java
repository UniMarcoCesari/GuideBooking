package service;

import model.Luogo;

import java.io.*;
import java.util.List;

public class DataManager {


    //TODO--> Salvataggio manuale
    //        DataManager.salvaDati(luoghi, "luoghi.dat");


    public static void salvaDatiLuogo(List<Luogo> dati, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dati);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Luogo> caricaDatiLuogo(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Luogo>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
