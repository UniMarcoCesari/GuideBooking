package controller;

import costants.Costants;
import costants.Credenziale;
import service.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static costants.Costants.file_credenziali;

public class AuthController
{
    /**
     *
     * @param username
     * @param password
     * @return
     * -1 --> credenziale errate
     * 0 --> login configuratore gia autenticato
     * 1 --> login PRE-configuratore
     */
    public static int checkCredentials(String username, String password) {
        List<Credenziale> credenzialeList = CredenzialeWriter.caricaCredenziali();
        if (credenzialeList == null) {
            return -2;
        }
        for (Credenziale credenziale : credenzialeList) {
           if (credenziale.getUsername().equals(username) && credenziale.getPassword().equals(password)) {
               if (Objects.equals(credenziale.getRuolo(), Costants.ruolo_PRE_configuratore))
                   return 1;
               if (Objects.equals(credenziale.getRuolo(), Costants.ruolo_configuratore))
                   return 0;
               if (Objects.equals(credenziale.getRuolo(), Costants.ruolo_volontario))
                   return 2;
               if (Objects.equals(credenziale.getRuolo(), Costants.ruolo_pre_volontario))
                   return 3;
               if (Objects.equals(credenziale.getRuolo(), Costants.ruolo_eliminato)) {  
                    return 4;
                }
                if (Objects.equals(credenziale.getRuolo(), Costants.ruolo_fruitore)) {
                    return 5;
                }
           }
        }
        return -1;
    }

    public static void setNewPassConfiguratore(String username, String password) {
        List<Credenziale> credenzialeList = DataManager.caricaDati(file_credenziali);
        if (credenzialeList == null) {
            return;
        }
        for (Credenziale credenziale : credenzialeList) {
            if (credenziale.getUsername().equals(username)) {
                credenziale.setPassword(password);
                credenziale.setRuolo(Costants.ruolo_configuratore);  // ruolo nuovo
            }
        }

        DataManager.salvaDati(credenzialeList, file_credenziali);

    }

    public static void setNewPassVolontario(String username, String password) {
        List<Credenziale> credenzialeList = CredenzialeWriter.caricaCredenziali();
        for (Credenziale credenziale : credenzialeList) {
            if (credenziale.getUsername().equals(username)) {
                credenziale.setPassword(password);
                credenziale.setRuolo(Costants.ruolo_volontario);  // ruolo nuovo
            }
        }

        DataManager.salvaDati(credenzialeList, file_credenziali);

    }

    public static List<Credenziale> getCredenziali() {
        List<Credenziale> credenzialeList = DataManager.caricaDati(file_credenziali);
        if (credenzialeList == null) {
            return new ArrayList<Credenziale>();
        }
        return DataManager.caricaDati(file_credenziali);
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
        credenzialeList.add(new Credenziale(username, password, Costants.ruolo_fruitore));
        System.out.println("aggiunto fruitore");
        CredenzialeWriter.salvaCredenziali(credenzialeList);
        return true;

    }
}
