package costants;

import java.io.Serializable;

public class Credenziale implements Serializable
{
    private static final long serialVersionUID = 1L;

    final String username;
    String password;
    String ruolo;


    public Credenziale(String username, String password, String ruolo) {
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

    public String getRuolo() {
        return ruolo;
    }

    public  void setPassword(String password) {
        this.password = password;
    }
    public  void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
