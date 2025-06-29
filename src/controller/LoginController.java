package controller;
import enumerations.Ruolo;
import view.login.ILoginView;

public class LoginController {
    private ILoginView view;

    public LoginController(ILoginView view) {
        this.view = view;
    }

    public void tentaLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.mostraErrore("Errore di Input", "Username e Password non possono essere vuoti.");
            return;
        }

        try {
            Ruolo ruolo = AuthController.getRuoloByCredential(username, password);

            switch (ruolo) {
                case CONFIGURATORE:
                    view.chiudi();
                    view.apriPannelloConfiguratore();
                    break;
                case VOLONTARIO:
                    view.chiudi();
                    view.apriPannelloVolontario(username);
                    break;
                case FRUITORE:
                    view.chiudi();
                    view.apriPannelloFruitore(username);
                    break;
                default:
                    view.mostraErrore("Errore di Autenticazione", "Credenziali errate.");
                    view.pulisciCampi();
                    break;
            }
        } catch (Exception e) {
            view.mostraErrore("Errore di Autenticazione", e.getMessage());
            view.pulisciCampi();
            return; 
        }

        
    }

    public void vaiARegistrazione() {
        view.chiudi();
        view.apriRegistrazioneFruitore();
    }

    public boolean registraNuovoFruitore(String username, String password) {
        if(username.isEmpty() || password.isEmpty()){
            view.mostraErrore("Errore Registrazione", "Username e password non possono essere vuoti.");
            return false;
        }
        boolean successo = AuthController.creaFruitoreCredenziali(username, password);
        if (successo) {
            view.mostraMessaggio("Registrazione Completata", "Utente registrato con successo. Ora puoi effettuare il login.");
        } else {
            view.mostraErrore("Errore Registrazione", "Username già esistente o errore durante la registrazione.");
        }
        return successo;
    }
}