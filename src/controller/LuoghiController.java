package controller;
import costants.*;
import java.util.ArrayList;
import java.util.List;
import model.Luogo;
import model.TipoVisita;
import model.Volontario;
import service.PersistentDataManager;

public class LuoghiController {
    private List<Luogo> luoghi;
    private PersistentDataManager dataManager;

    public LuoghiController(PersistentDataManager dataManager) {
        this.dataManager = dataManager;
        this.luoghi = dataManager.caricaDati(Costants.file_luoghi);
        if (luoghi == null) {
            luoghi = new ArrayList<>();
            salvaDati();
        }
    }

    public void aggiungiLuogo(Luogo luogo) {
        luoghi.add(luogo);
        salvaDati();
    }

    public List<Luogo> getLuoghi() {
        //pulisciDati()
        return luoghi;
    }

    // private void pulisciDati() {
    //     // Carica i luoghi e i tipi visita dal file
    //     List<TipoVisita> tipiVisitaGlobali = PersistentDataManager.caricaDati(Costants.file_tipi_visita);
    
    //     if (luoghi == null || tipiVisitaGlobali == null) return;
    
    //     // Scorri i luoghi (copia per evitare ConcurrentModificationException)
    //     for (Luogo luogo : new ArrayList<>(luoghi)) {
    //         // Scorri i tipi visita del luogo (anche qui uso una copia)
    //         for (TipoVisita tipo : new ArrayList<>(luogo.getTipiVisita())) {
    //             // Trova il tipo visita completo nel file globale
    //             boolean esiste = false;
    //             for (TipoVisita tipoGlobale : tipiVisitaGlobali) {
    //                 if (tipo.equals(tipoGlobale)) {
    //                     esiste = true;
    //                     break;
    //                 }
    //             }
    //             // Se non esiste, rimuovi dal luogo
    //             if (!esiste) {
    //                 luogo.getTipiVisita().remove(tipo);
    //             }
    //         }
    
    //         // Se il luogo non ha più tipi visita, rimuovilo dalla lista
    //         if (luogo.getTipiVisita().isEmpty()) {
    //             System.out.println("luogo pulito");
    //             luoghi.remove(luogo);
    //         }
    //     }
    
    //     // Aggiorna l'attributo e salva
    //     this.luoghi = luoghi;
    //     salvaDati();
    // }
    
    

    public void salvaDati() {
        dataManager.salvaDati(luoghi, Costants.file_luoghi);
    }

    public Boolean aggiungiTipoVisita(Luogo luogoSelezionato, TipoVisita tipoVisita) {
        for (Luogo luogo : luoghi) {
            if(luogo.equals(luogoSelezionato)) {
                for (TipoVisita tipo : luogo.getTipiVisita()) {
                    if (tipo.equals(tipoVisita)) {
                        return false;
                    }
                }
                luogo.aggiungiTipoVisita(tipoVisita);
                salvaDati();
                return true;
            }
        }
        return false;
    
    }

    public void rimuoviVolonatario(Volontario volontario, List<TipoVisita> tipiVisitaDaRimuovere) {
        for (Luogo luogo : luoghi) {
            for (TipoVisita tipoVisita : luogo.getTipiVisita()) {
                tipoVisita.rimuoviVolontario(volontario);
                if (tipoVisita.getVolontari().isEmpty()) {
                    tipiVisitaDaRimuovere.add(tipoVisita);
                }
            }
            luogo.getTipiVisita().removeAll(tipiVisitaDaRimuovere);
        }


        // rimuovi luoghi senza tipi visita
        luoghi.removeIf(l -> l.getTipiVisita().isEmpty());

        salvaDati();
    }

    public void rimuoviTipoVisita(TipoVisita nuovaVisita) 
    {
        for (Luogo luogo : luoghi) {
            for (TipoVisita tipoVisita : luogo.getTipiVisita()) {
                if (tipoVisita.getTitolo().equals(nuovaVisita.getTitolo())) 
                {
                    luogo.rimuoviTipoVisita(tipoVisita);   
                }
                
            }
            
        }
        luoghi.removeIf(l -> l.getTipiVisita().isEmpty());
        salvaDati();
    }

    public void rimuoviLuogo(Luogo luogo) {
        luoghi.remove(luogo);
        salvaDati();
    }

    public void modificaTipoVisita(TipoVisita nuovaVisita) {
        for (Luogo luogo : luoghi) {
            for (TipoVisita tipoVisita : luogo.getTipiVisita()) {
                if (tipoVisita.getTitolo().equals(nuovaVisita.getTitolo())) {
                    tipoVisita.modifica(nuovaVisita);
                }
            }
        }
        salvaDati();
    }

    public void salvaOAggiornaLuogo(String nome, String descrizione, String posizione, ArrayList<TipoVisita> tipiVisita, Luogo luogoDaAggiornare) throws Exception {
        if (nome.isEmpty() || posizione.isEmpty()) {
            throw new Exception("I campi Nome e Posizione sono obbligatori");
        }

        if (tipiVisita.isEmpty()) {
            throw new Exception("Inserisci almeno un tipo visita");
        }

        if (luogoDaAggiornare != null) {
            // Aggiorna il luogo esistente
            luogoDaAggiornare.setNome(nome);
            luogoDaAggiornare.setDescrizione(descrizione);
            luogoDaAggiornare.setPosizione(posizione);
            luogoDaAggiornare.setTipiVisita(tipiVisita);
        } else {
            // Aggiungi un nuovo luogo
            aggiungiLuogo(new Luogo(nome, descrizione, posizione, tipiVisita));
        }
        salvaDati();
    }
}
