package test;

import org.junit.Test;
import static org.junit.Assert.*;

import model.TipoVisita;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class TipoVisitaTest {

    @Test
    public void testIsProgrammabilePerData_QuandoGiornoEDataSonoValidi() {
        // 1. PREPARAZIONE (Arrange)
        // Creiamo un TipoVisita che è valido dal 1 al 31 Luglio 2024,
        // solo il Lunedì e il Mercoledì.
        Set<DayOfWeek> giorniValidi = new HashSet<>();
        giorniValidi.add(DayOfWeek.MONDAY);
        giorniValidi.add(DayOfWeek.WEDNESDAY);

        TipoVisita tipoVisita = new TipoVisita(
            "Tour Estivo", "Descrizione...", "Punto Incontro A",
            LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31),
            giorniValidi, LocalTime.of(10, 0), 90, false, 20, 5, null
        );
        
        // La data che vogliamo testare: Mercoledì 10 Luglio 2024 (è un giorno valido)
        LocalDate dataDaTestare = LocalDate.of(2024, 7, 10);

        // 2. ESECUZIONE (Act)
        boolean risultato = tipoVisita.isProgrammabilePerData(dataDaTestare);

        // 3. VERIFICA (Assert)
        assertTrue("La visita dovrebbe essere programmabile in un giorno e data validi", risultato);
    }

    @Test
    public void testIsProgrammabilePerData_QuandoGiornoNonValido() {
        // 1. PREPARAZIONE (Arrange)
        // Usiamo lo stesso TipoVisita di prima (valido solo Lun/Mer)
        Set<DayOfWeek> giorniValidi = new HashSet<>();
        giorniValidi.add(DayOfWeek.MONDAY);
        giorniValidi.add(DayOfWeek.WEDNESDAY);

        TipoVisita tipoVisita = new TipoVisita(
            "Tour Estivo", "Descrizione...", "Punto Incontro A",
            LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31),
            giorniValidi, LocalTime.of(10, 0), 90, false, 20, 5, null
        );

        // La data che vogliamo testare: Martedì 9 Luglio 2024 (un giorno NON valido)
        LocalDate dataDaTestare = LocalDate.of(2024, 7, 9);

        // 2. ESECUZIONE (Act)
        boolean risultato = tipoVisita.isProgrammabilePerData(dataDaTestare);

        // 3. VERIFICA (Assert)
        assertFalse("La visita non dovrebbe essere programmabile in un giorno non valido", risultato);
    }

    @Test
    public void testIsProgrammabilePerData_QuandoDataFuoriIntervallo() {
        // 1. PREPARAZIONE (Arrange)
        // Usiamo lo stesso TipoVisita di prima (valido solo a Luglio)
        Set<DayOfWeek> giorniValidi = new HashSet<>();
        giorniValidi.add(DayOfWeek.MONDAY);
        giorniValidi.add(DayOfWeek.WEDNESDAY);
        
        TipoVisita tipoVisita = new TipoVisita(
            "Tour Estivo", "Descrizione...", "Punto Incontro A",
            LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31),
            giorniValidi, LocalTime.of(10, 0), 90, false, 20, 5, null
        );
        
        // La data che vogliamo testare: Mercoledì 14 Agosto 2024 (giorno settimana ok, ma data fuori intervallo)
        LocalDate dataDaTestare = LocalDate.of(2024, 8, 14);

        // 2. ESECUZIONE (Act)
        boolean risultato = tipoVisita.isProgrammabilePerData(dataDaTestare);

        // 3. VERIFICA (Assert)
        assertFalse("La visita non dovrebbe essere programmabile in una data fuori dall'intervallo", risultato);
    }
}