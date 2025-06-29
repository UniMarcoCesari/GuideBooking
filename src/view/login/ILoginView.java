package view.login;


public interface ILoginView {
    String getUsername();
    String getPassword();
    void mostraErrore(String titolo, String messaggio);
    void mostraMessaggio(String titolo, String messaggio); 
    void apriPannelloConfiguratore();
    void apriCorpoDatiFase1();
    void apriPannelloVolontario(String username);
    void apriPannelloFruitore(String username);
    void apriNewPasswordConf(String username, String tipoUtente);
    void apriRegistrazioneFruitore();
    void chiudi(); 
    void pulisciCampi(); 
   
}