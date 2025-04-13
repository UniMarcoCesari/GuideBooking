package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Volontario;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisponibilitaManager {

    private static final String DISPONIBILITA_FILE = "disponibilita.json";
    private Gson gson;

    public DisponibilitaManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Map<LocalDate, Map<String, Boolean>> loadDisponibilita() {
        try {
            if (Files.exists(Paths.get(DISPONIBILITA_FILE))) {
                Type type = new TypeToken<Map<LocalDate, Map<String, Boolean>>>() {}.getType();
                return gson.fromJson(new FileReader(DISPONIBILITA_FILE), type);
            } else {
                return new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void saveDisponibilita(Map<LocalDate, Map<String, Boolean>> disponibilita) {
        try (FileWriter writer = new FileWriter(DISPONIBILITA_FILE)) {
            gson.toJson(disponibilita, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
