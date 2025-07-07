package controller;

import costants.Credenziale;
import service.PersistentDataManager;
import enumerations.Ruolo;
import exception.InvalidPasswordException;
import exception.UserNotFoundException;

import java.util.List;

import static costants.Costants.file_credenziali;

public class AuthController
{
    private final PersistentDataManager dataManager;
    List<Credenziale> credenziali;

    public AuthController(PersistentDataManager dataManager) {
        this.dataManager = dataManager;
        credenziali = dataManager.caricaCredenziali();
    }
    

    public Ruolo getRuoloByCredential(String username, String password) throws Exception {
        if (credenziali == null) {
            throw new IllegalStateException("Impossibile caricare le credenziali.");
        }
        for (Credenziale credenziale : credenziali) {
           if (credenziale.getUsername().equals(username)) {
            // Username esistente
              if (credenziale.getPassword().equals(password)) {
                  // Password corretta
                  return credenziale.getRuolo();
              }
              // Password errata
              throw new InvalidPasswordException("Password errata per l'utente: " + username);
              
           }
        }
        throw new UserNotFoundException("Utente non trovato: " + username);
    }

    public void setNewPasswordAndRuolo(String username, String password, Ruolo ruolo) {
        for (Credenziale credenziale : credenziali) {
            if (credenziale.getUsername().equals(username)) {
                credenziale.setPassword(password);
                credenziale.setRuolo(ruolo); 
            }
        }
    }

    
    public boolean checkExistingCredentials(String username) {
        
        for (Credenziale credenziale : credenziali) {
            System.out.println("Credenziale: " + credenziale.getUsername());
            if (credenziale.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean creaNuovaCredenziale(String username, String password, Ruolo ruolo) {

        
        if(checkExistingCredentials(username))
        {   
            //esiste gia username
            return false;
        }

        credenziali.add(new Credenziale(username, password, ruolo));
        System.out.println("Credenziale aggiunta: " + username + " con ruolo " + ruolo);
        dataManager.salvaDati(credenziali, file_credenziali);
        return true;

    }


    public void disabilitaCredenziale(String nome) {
        for (Credenziale credenziale : credenziali) {
            if (credenziale.getUsername().equals(nome)) {
                credenziale.setRuolo(Ruolo.UTENTE_ELIMINATO);
            }
        }
        dataManager.salvaDati(credenziali, file_credenziali);
    }
}
