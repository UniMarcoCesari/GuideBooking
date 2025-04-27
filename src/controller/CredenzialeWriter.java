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
        List<Credenziale> credenzialeList = new ArrayList<>();
        credenzialeList.add(new Credenziale("pre", "test", Costants.ruolo_configuratore));
        credenzialeList.add(new Credenziale("marco", "test", Costants.ruolo_volontario));
        credenzialeList.add(new Credenziale("laura", "test", Costants.ruolo_volontario));
        credenzialeList.add(new Credenziale("gian", "test", Costants.ruolo_fruitore));
        credenzialeList.add(new Credenziale("filippo", "test", Costants.ruolo_fruitore));

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






    public static String getRuolo(String currentUsername) {

        System.out.println("Cerco ruolo per: " + currentUsername);

        List<Credenziale> listaCredenziali = caricaCredenziali();

        System.out.println("Lista credenziali:");
        for (Credenziale c : listaCredenziali) {
            System.out.println(c.getUsername() + " - " + c.getPassword() + " - " + c.getRuolo());
        }
        

        for (Credenziale c : listaCredenziali) {
            if (c.getUsername().equals(currentUsername)) {
                return c.getRuolo();
            }
        }
        return null;
        
    }
}
