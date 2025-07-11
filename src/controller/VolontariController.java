package controller;

import costants.Costants;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Volontario;
import service.PersistentDataManager;
import enumerations.Ruolo;

public class VolontariController {

    AuthController authController;
    PersistentDataManager dataManager;
    private ArrayList<Volontario> listaVolontari;
    private HashMap<LocalDate, Map<String, Boolean>> disponibilitaVolontari;

    public VolontariController(AuthController authController,PersistentDataManager dataManager) {
        this.authController = authController;
        this.dataManager = dataManager;
        this.listaVolontari = dataManager.caricaOggetto(Costants.file_volontari);
        if (this.listaVolontari == null) {
            this.listaVolontari = new ArrayList<>();
        }
        this.disponibilitaVolontari = dataManager.caricaOggetto(Costants.file_disponibilita_volontari);
        if (this.disponibilitaVolontari == null) {
            this.disponibilitaVolontari = new HashMap<>();
        }
    }

    public boolean isDisponibile(Volontario volontario, LocalDate data) {
        Map<String, Boolean> disponibilitaGiorno = disponibilitaVolontari.get(data);
        if (disponibilitaGiorno == null) return false;
        return disponibilitaGiorno.getOrDefault(volontario.getNome(), false);
    }

    public ArrayList<Volontario> getListaVolontari() {
        return listaVolontari;
    }

    public HashMap<LocalDate, Map<String, Boolean>> getDisponibilitaVolontari() {
        return disponibilitaVolontari;
    }

    public void setDisponibilitaVolontari(HashMap<LocalDate, Map<String, Boolean>> disponibilitaVolontari) {
        this.disponibilitaVolontari = disponibilitaVolontari;
    }


    /**
     * Aggiunge un nuovo volontario alla lista controllando se esiste già.
     * Mostra un messaggio di errore se il volontario esiste già.
     *
     * @param username L'username del nuovo volontario.
     * @return true se il volontario è stato aggiunto con successo, false altrimenti.
     */
    public boolean aggiungiVolontario(String username) {
        // Controlla se esiste già un volontario con lo stesso username (ignorando maiuscole/minuscole)
        for (Volontario esistente : listaVolontari) {
            if (esistente.getNome().equalsIgnoreCase(username)) {
                // Utilizza System.err per stampare il messaggio di errore
                System.err.println("Esiste già un volontario con l'username '" + username + "'.");
                return false; // Volontario già esistente
            }
        }

        // Se non esiste, crea e aggiungi il nuovo volontario
        Volontario nuovoVolontario = new Volontario(username);
        listaVolontari.add(nuovoVolontario);
        dataManager.salvaOggetto(listaVolontari, Costants.file_volontari);
        authController.creaNuovaCredenziale(username, "test", Ruolo.PRE_VOLONTARIO);
        return true; // Volontario aggiunto con successo
    }

    public void rimuoviVolontario(Volontario volontario) {
        listaVolontari.remove(volontario);
        authController.disabilitaCredenziale(volontario.getNome());
        dataManager.salvaOggetto(listaVolontari, Costants.file_volontari);
    }

    public void aggiornaDisponibilita(Volontario volontario, LocalDate data, boolean disponibile) {
        disponibilitaVolontari.putIfAbsent(data, new HashMap<>());
        disponibilitaVolontari.get(data).put(volontario.getNome(), disponibile);
    }

    public void salvaDisponibilita() {
        dataManager.salvaOggetto(disponibilitaVolontari, Costants.file_disponibilita_volontari);
    }


    public boolean isDisponibile(LocalDate data, String username) {
        return disponibilitaVolontari.containsKey(data) &&
               disponibilitaVolontari.get(data).getOrDefault(username, false);
    }





    public boolean volontarioEsiste(String nomeVolontario) {
        return listaVolontari.stream().anyMatch(v -> v.getNome().equalsIgnoreCase(nomeVolontario));
    }
}
