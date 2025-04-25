package controller;


import costants.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CredenzialeWriter {
    public static void main(String[] args) {
        // Creiamo una credenziale di esempio
        Credenziale admin = new Credenziale("pre", "a", Costants.ruolo_PRE_configuratore);
        List<Credenziale> credenzialeList = new ArrayList<>();
        credenzialeList.add(admin);

        // Scriviamo l'oggetto nel file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_credenziali))) {
            oos.writeObject(credenzialeList);
            System.out.println("✅ File credenziali.dat creato con successo!");
            System.out.println("Credenziali salvate:");
            for (Credenziale c : credenzialeList) {
                System.out.println(c.getUsername() + " - " + c.getPassword() + " - " + c.getRuolo());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void salvaCredenziali(List<Credenziale> credenzialeList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_credenziali))) {
            oos.writeObject(credenzialeList);
            System.out.println("✅ File credenziali.dat creato con successo!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Credenziale> caricaCredenziali() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Costants.file_credenziali))) {
            return (List<Credenziale>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //Imposta il ruolo di un volontario come "eliminato" in modo che al prossimo accesso non sia più possibile accedere al sistema
    public static void disabilitaCredenzialeVolontario(String nome) {
        List<Credenziale> listaCredenziali = caricaCredenziali();
        for (Credenziale c : listaCredenziali) {
            if (c.getUsername().equals(nome) && c.getRuolo().equals(Costants.ruolo_volontario)) {
                c.setRuolo(Costants.ruolo_eliminato);
            }
        }
        salvaCredenziali(listaCredenziali);
        System.out.println("✅ Credenziali per il ruolo 'volontario' marcate come 'eliminato' per l'utente " + nome);
    }


    public static boolean isFruitore(String currentUsername) {
        List<Credenziale> listaCredenziali = caricaCredenziali();
        for (Credenziale c : listaCredenziali) {
            if (c.getUsername().equals(currentUsername) && c.getRuolo().equals(Costants.ruolo_fruitore)) {
                return true;
            }
        }
        return false;
    }
}
