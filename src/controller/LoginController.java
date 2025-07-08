package controller;
import enumerations.Ruolo;
import exception.InvalidPasswordException;
import exception.UserNotFoundException;
import view.login.ILoginView;
import view.login.MainController;

public class LoginController {
    private ILoginView view;
    private AuthController authController;
    private MainController mainController;
    

    public LoginController(ILoginView view, MainController mainController) {
        this.view = view;
        this.mainController = mainController;
        this.authController = mainController.getAuthController();
    }

    public void tentaLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.mostraErrore("Errore di Input", "Username e Password non possono essere vuoti.");
            return;
        }

        try {
            Ruolo ruolo = authController.getRuoloByCredential(username, password);

            switch (ruolo) {
                case PRE_CONFIGURATORE:
                    mainController.showNewPasswordFrame(username, ruolo);
                    break;
                case CONFIGURATORE:
                    mainController.showPannelloConfiguratore();
                    break;
                case VOLONTARIO:
                    mainController.showPannelloVolontario(username);
                    break;
                case FRUITORE:
                    mainController.showPannelloFruitore(username);
                    break;
                default:
                    view.mostraErrore("Errore di Autenticazione", "Ruolo non valido.");
                    view.pulisciCampi();
                    break;
            }
         } catch (UserNotFoundException e) {
            view.mostraErrore("Utente Non Trovato", e.getMessage());
            view.pulisciCampi();
        } catch (InvalidPasswordException e) {
            view.mostraErrore("Password Errata", e.getMessage());
            view.pulisciCampi();
        } catch (Exception e) {
            view.mostraErrore("Errore di Sistema", e.getMessage());
        } 

    }

    public void vaiARegistrazione() {
        mainController.showRegistrazioneFruitore();
    }

    public boolean registraNuovoFruitore(String username, String password) {
        if(username.isEmpty() || password.isEmpty()){
            view.mostraErrore("Errore Registrazione", "Username e password non possono essere vuoti.");
            return false;
        }
        boolean successo = authController.creaNuovaCredenziale(username, password, Ruolo.FRUITORE);
        if (successo) {
            view.mostraMessaggio("Registrazione Completata", "Utente registrato con successo. Ora puoi effettuare il login.");
        } else {
            view.mostraErrore("Errore Registrazione", "Username già esistente o errore durante la registrazione.");
        }
        return successo;
    }
}
