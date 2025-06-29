package costants;

import java.io.Serializable;
import enumerations.Ruolo;

public class Credenziale implements Serializable
{
    private static final long serialVersionUID = 1L;

    final String username;
    String password;
    Ruolo ruolo;


    public Credenziale(String username, String password, Ruolo ruolo) {
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public  void setPassword(String password) {
        this.password = password;
    }
    public  void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
}
