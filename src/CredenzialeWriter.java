
import costants.Costants;
import costants.Credenziale;
import java.io.FileOutputStream;
import java.io.IOException;
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
}
