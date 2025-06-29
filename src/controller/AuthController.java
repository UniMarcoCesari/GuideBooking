package controller;

import costants.Credenziale;
import service.DataManager;
import enumerations.Ruolo;

import java.util.List;

import static costants.Costants.file_credenziali;

public class AuthController
{
    public static Ruolo getRuoloByCredential(String username, String password) {
        List<Credenziale> credenzialeList = CredenzialeWriter.caricaCredenziali();
        if (credenzialeList == null) {
            throw new IllegalStateException("Impossibile caricare le credenziali.");
        }
        for (Credenziale credenziale : credenzialeList) {
           if (credenziale.getUsername().equals(username) && credenziale.getPassword().equals(password)) {
              return credenziale.getRuolo();
           }
        }
        throw new IllegalStateException("Credenziali errate.");
    }

    public static void setNewPassConfiguratore(String username, String password) {
        List<Credenziale> credenzialeList = DataManager.caricaDati(file_credenziali);
        if (credenzialeList == null) {
            return;
        }
        for (Credenziale credenziale : credenzialeList) {
            if (credenziale.getUsername().equals(username)) {
                credenziale.setPassword(password);
                credenziale.setRuolo(Ruolo.CONFIGURATORE);  // ruolo nuovo
            }
        }

        DataManager.salvaDati(credenzialeList, file_credenziali);
    }

    public static void setNewPassVolontario(String username, String password) {
        List<Credenziale> credenzialeList = CredenzialeWriter.caricaCredenziali();
        for (Credenziale credenziale : credenzialeList) {
            if (credenziale.getUsername().equals(username)) {
                credenziale.setPassword(password);
                credenziale.setRuolo(Ruolo.VOLONTARIO);  // ruolo nuovo
            }
        }

        DataManager.salvaDati(credenzialeList, file_credenziali);
    }

    

    
    public static boolean checkExistingCredentials(String username) {
        List<Credenziale> credenzialeList = CredenzialeWriter.caricaCredenziali();
        for (Credenziale credenziale : credenzialeList) {
            System.out.println("Credenziale: " + credenziale.getUsername());
            if (credenziale.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean creaFruitoreCredenziali(String username, String password) {
        
        if(checkExistingCredentials(username))
        {   
            //esiste gia username
            return false;
        }

        List<Credenziale> credenzialeList = CredenzialeWriter.caricaCredenziali();
        credenzialeList.add(new Credenziale(username, password, Ruolo.FRUITORE));
        System.out.println("aggiunto fruitore");
        CredenzialeWriter.salvaCredenziali(credenzialeList);
        return true;

    }
}
