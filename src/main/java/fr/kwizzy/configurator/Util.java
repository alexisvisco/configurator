package fr.kwizzy.configurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

class Util {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static Optional<JSONObject> getJsonFromFile(File f) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(f.toPath()));
            return Optional.of(new JSONObject(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    static void writeInFile(JSONObject o, File f) {
        try (FileWriter file = new FileWriter(f.getAbsolutePath())) {
            file.write(o.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void writeInFile(String o, File f) {
        try (FileWriter file = new FileWriter(f.getAbsolutePath())) {
            file.write(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
