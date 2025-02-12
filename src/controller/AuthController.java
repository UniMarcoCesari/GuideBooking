package controller;

import costants.Costants;
import model.Credenziale;
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
        for (Credenziale credenziale : credenzialeList) {
           if (credenziale.getUsername().equals(username) && credenziale.getPassword().equals(password)) {
               if (Objects.equals(credenziale.getRuolo(), Costants.ruolo_PRE_configuratore))
                   return 1;
               if (Objects.equals(credenziale.getRuolo(), Costants.ruolo_configuratore))
                   return 0;
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

    public List<Credenziale> getCredenziali() {
        return DataManager.caricaDati(file_credenziali);
    }
}
