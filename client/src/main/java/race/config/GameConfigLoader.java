package race.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class GameConfigLoader {
    
    private static JsonObject config = null;
    private static boolean loaded = false;
    
    public static void ensureLoaded() {
        if (!loaded) {
            try {
                loadConfig();
                loaded = true;
            } catch (IOException e) {
                throw new RuntimeException("Nie można załadować konfiguracji: " + e.getMessage(), e);
            }
        }
    }
    
    private static void loadConfig() throws IOException {
        String[] candidates = new String[]{
                "config/gameConfig.json",
                "assets/config/gameConfig.json"
        };

        String jsonContent = readFirstAvailable(candidates);
        if (jsonContent == null) {
            throw new IOException("Plik konfiguracyjny nie istnieje w żadnej lokalizacji");
        }

        JsonObject root = JsonParser.parseString(jsonContent).getAsJsonObject();
        config = root.getAsJsonObject("gameConfig");
    }

    private static String readFirstAvailable(String... candidates) {
        for (String path : candidates) {
            String content = readClasspath(path);
            if (content != null) {
                return content;
            }
        }

        for (String path : candidates) {
            String content = readFilesystem(path);
            if (content != null) {
                return content;
            }
        }
        
        return null;
    }

    private static String readClasspath(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = GameConfigLoader.class.getClassLoader();
        }
        
        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            if (inputStream == null) {
                return null;
            }
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line).append('\n');
                }
                return jsonContent.toString();
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static String readFilesystem(String path) {
        try {
            java.nio.file.Path filePath = java.nio.file.Path.of(path);
            if (java.nio.file.Files.exists(filePath)) {
                return java.nio.file.Files.readString(filePath, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            // Ignoruj błędy filesystem
        }
        return null;
    }
    
    public static JsonObject getConfig() {
        ensureLoaded();
        return config;
    }
}
