package view.login;

public interface ILoginView {
    String getUsername();
    String getPassword();
    void mostraErrore(String titolo, String messaggio);
    void mostraMessaggio(String titolo, String messaggio); 
    void chiudi(); 
    void pulisciCampi(); 
   
}