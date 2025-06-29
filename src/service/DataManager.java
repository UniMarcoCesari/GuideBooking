package service;

import model.Calendario;
import model.CorpoDati;
import model.Luogo;
import model.Visita;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import costants.Costants;
import costants.Credenziale;
import enumerations.Ruolo;

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
            T data = (T) ois.readObject();
            return data;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void salvaCorpoDati(CorpoDati dati, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dati);
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
            CorpoDati data = (CorpoDati) ois.readObject();
            return data;
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

    public static List<LocalDate> caricaDatePrecluse(String percorsoFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(percorsoFile))) {
            List<LocalDate> dates = (List<LocalDate>) ois.readObject();
            return dates;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void creaCredenzialiVolontari(List<Luogo> luoghi) {
        List<Credenziale> credenziali = new ArrayList<>();
        for (int i = 0; i < luoghi.size(); i++) {
            for (int j = 0; j < luoghi.get(i).getTipiVisita().size(); j++) {
                for (int k = 0; k < luoghi.get(i).getTipiVisita().get(j).getVolontari().size(); k++) {
                    credenziali.add(new Credenziale(luoghi.get(i).getTipiVisita().get(j).getVolontari().get(k).getNome(), "v", Ruolo.VOLONTARIO));
                }
            }
        }
        salvaCredenzialiVolontari(credenziali);
    }

    public static void salvaCredenzialiVolontari(List<Credenziale> nuoveCredenziali) {
        List<Credenziale> credenzialeList = caricaCredenziali();
        credenzialeList.addAll(nuoveCredenziali);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_credenziali))) {
            oos.writeObject(credenzialeList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Credenziale> caricaCredenziali() {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Costants.file_credenziali))) {
            List<Credenziale> credentials = (List<Credenziale>) ois.readObject();
            return credentials;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Visita> leggiDatiVisite(){
        File file = new File(Costants.file_visite);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Visita> visits = (List<Visita>) ois.readObject();
            return visits;
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void scriviDatiVisite(List<Visita> visite) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_visite))) {
            oos.writeObject(visite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int leggiNumMax() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Costants.file_corpo))) {
            CorpoDati corpoDati = (CorpoDati) ois.readObject();
            return Integer.parseInt(corpoDati.getMaxPersone());
        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            e.printStackTrace();
            return 20;
        }
    }
}

