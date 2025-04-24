package controller;

import costants.Costants;
import costants.Credenziale;
import service.DataManager;

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
        List<Credenziale> credenzialeList = DataManager.caricaDati(file_credenziali);
        if (credenzialeList == null) {
            return -1;
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
           }
        }
        return -1;
    }

    public static void setNewPassConfiguratore(String username, String password) {
        List<Credenziale> credenzialeList = DataManager.caricaDati(file_credenziali);
        for (Credenziale credenziale : credenzialeList) {
            if (credenziale.getUsername().equals(username)) {
                credenziale.setPassword(password);
                credenziale.setRuolo(Costants.ruolo_configuratore);  // ruolo nuovo
            }
        }

        DataManager.salvaDati(credenzialeList, file_credenziali);

    }

    public static void setNewPassVolontario(String username, String password) {
        List<Credenziale> credenzialeList = DataManager.caricaDati(file_credenziali);
        for (Credenziale credenziale : credenzialeList) {
            if (credenziale.getUsername().equals(username)) {
                credenziale.setPassword(password);
                credenziale.setRuolo(Costants.ruolo_volontario);  // ruolo nuovo
            }
        }

        DataManager.salvaDati(credenzialeList, file_credenziali);

    }

    public List<Credenziale> getCredenziali() {
        return DataManager.caricaDati(file_credenziali);
    }
}
