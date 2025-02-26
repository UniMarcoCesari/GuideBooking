package controller;

import costants.Costants;
import java.io.*;
import java.util.ArrayList;
import model.Volontario;

public class VolontariController {
    private ArrayList<Volontario> listaVolontari;

    public VolontariController() {
        listaVolontari = caricaDati();
    }

    public ArrayList<Volontario> getListaVolontari() {
        caricaDati();
        return listaVolontari;
    }

    public boolean aggiungiVolontario(Volontario volontario) {

        // se esiste volontatio con stesso nome
        for (int i = 0; i < listaVolontari.size(); i++) {
            if (listaVolontari.get(i).getNome().equals(volontario.getNome())) {
                return false;
            }
        }

        listaVolontari.add(volontario);
        salvaDati();
        return true;
    }

    public void rimuoviVolonatario(Volontario volontario) {
        listaVolontari.remove(volontario);
        salvaDati();
    }

    public void salvaDati() {
        System.out.println("salvati i seguenti tipi di visita");
        for (int i = 0; i < listaVolontari.size(); i++) {
            System.out.println(listaVolontari.get(i).getNome());
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Costants.file_volontari))) {
            oos.writeObject(listaVolontari);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @SuppressWarnings("unchecked")
    private ArrayList<Volontario> caricaDati() {
        File file = new File(Costants.file_volontari);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<Volontario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
