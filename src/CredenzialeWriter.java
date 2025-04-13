
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
        Credenziale gia = new Credenziale("a", "a", Costants.ruolo_configuratore);

        List<Credenziale> credenzialeList = new ArrayList<>();
        credenzialeList.add(admin);
        credenzialeList.add(gia);

        // Scriviamo l'oggetto nel file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_credenziali))) {
            oos.writeObject(credenzialeList);
            System.out.println("✅ File credenziali.dat creato con successo!");
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
}
