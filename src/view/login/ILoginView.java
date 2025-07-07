package view.login;

import enumerations.Ruolo;

public interface ILoginView {
    String getUsername();
    String getPassword();
    void mostraErrore(String titolo, String messaggio);
    void mostraMessaggio(String titolo, String messaggio); 
    void apriPannelloConfiguratore();
    void apriCorpoDatiFase1();
    void apriPannelloVolontario(String username);
    void apriPannelloFruitore(String username);
    void apriNewPassword(String username, Ruolo ruolo);
    void apriRegistrazioneFruitore();
    void chiudi(); 
    void pulisciCampi(); 
   
}