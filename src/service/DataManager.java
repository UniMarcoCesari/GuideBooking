package service;

import model.Calendario;
import model.CorpoDati;
import model.Luogo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import costants.Costants;
import costants.Credenziale;

public class DataManager {

    /**
     * Salva una lista di oggetti serializzabili in un file.
     *
     * @param dati     Lista di oggetti da salvare.
     * @param filename Nome del file in cui salvare i dati.
     * @param <T>      Tipo generico degli oggetti nella lista.
     */
    public static <T extends Serializable> void salvaDati(List<T> dati, String filename) {
        System.out.println("[DEBUG] Salvataggio dati su file: " + filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dati);
            System.out.println("[DEBUG] Dati salvati con successo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void salvaDati(Calendario dati, String filename) {
        System.out.println("[DEBUG] Salvataggio Calendario su file: " + filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dati);
            System.out.println("[DEBUG] Calendario salvato con successo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Serializable> T caricaDati(String filename) {
        System.out.println("[DEBUG] Caricamento dati da file: " + filename);
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("[INFO] File non trovato.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            T data = (T) ois.readObject();
            System.out.println("[DEBUG] Dati caricati con successo.");
            return data;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void salvaCorpoDati(CorpoDati dati, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
        System.out.println("[DEBUG] Salvataggio CorpoDati su file: " + filename);
            oos.writeObject(dati);
            System.out.println("[INFO] Dati salvati correttamente in " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static CorpoDati caricaCorpoDati(String filename) {
        File file = new File(filename);
        System.out.println("[DEBUG] Caricamento CorpoDati da file: " + filename);

        if (!file.exists()) {
            System.out.println("[INFO] File non trovato.");
            return null;
           
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            CorpoDati data = (CorpoDati) ois.readObject();
            System.out.println("[DEBUG] CorpoDati caricato con successo.");
            return data;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void salvaDatePrecluse(List<LocalDate> date, String percorsoFile) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(percorsoFile))) {
        System.out.println("[DEBUG] Salvataggio date precluse su file: " + percorsoFile);
            oos.writeObject(new ArrayList<>(date));
        } catch (IOException e) {
            System.out.println("[DEBUG] Date precluse salvate con successo.");
            e.printStackTrace();
        }
    }

    public static List<LocalDate> caricaDatePrecluse(String percorsoFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(percorsoFile))) {
        System.out.println("[DEBUG] Caricamento date precluse da file: " + percorsoFile);
            List<LocalDate> dates = (List<LocalDate>) ois.readObject();
            System.out.println("[DEBUG] Date precluse caricate con successo.");
            return dates;
        } catch (FileNotFoundException e) {
            System.out.println("[INFO] File date precluse non trovato. Restituzione di una lista vuota.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void creaCredenzialiVolontari(List<Luogo> luoghi) {
        List<Credenziale> credenziali = new ArrayList<>();
        System.out.println("[DEBUG] Creazione credenziali volontari.");
        for (int i = 0; i < luoghi.size(); i++) {
            for (int j = 0; j < luoghi.get(i).getTipiVisita().size(); j++) {
                for (int k = 0; k < luoghi.get(i).getTipiVisita().get(j).getVolontari().size(); k++) {
                    credenziali.add(new Credenziale(luoghi.get(i).getTipiVisita().get(j).getVolontari().get(k).getNome(), "v", Costants.ruolo_volontario));
                }
            }
        }
        salvaCredenzialiVolontari(credenziali);
    }

    public static void salvaCredenzialiVolontari(List<Credenziale> nuoveCredenziali) {
        List<Credenziale> credenzialeList = caricaCredenziali();
        credenzialeList.addAll(nuoveCredenziali);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_credenziali))) {
            System.out.println("[DEBUG] Salvataggio credenziali su file.");
            oos.writeObject(credenzialeList);
            System.out.println("✅ File credenziali.dat aggiornato con successo!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Credenziale> caricaCredenziali() {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Costants.file_credenziali))) {
        System.out.println("[DEBUG] Caricamento credenziali da file.");
            List<Credenziale> credentials = (List<Credenziale>) ois.readObject();
            System.out.println("[DEBUG] Credenziali caricate con successo.");
            return credentials;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

