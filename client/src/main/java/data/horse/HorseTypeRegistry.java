package data.horse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import data.horse.dto.HorseDTO;
import data.horse.dto.HorseTypesConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class HorseTypeRegistry {
    private static final Map<Integer, HorseType> BY_ID = new HashMap<>();
    private static final Map<String, HorseType> BY_KEY = new HashMap<>();
    private static final Gson GSON = new Gson();
    private static final String TAG = "HorseTypeRegistry";

    private HorseTypeRegistry() {
    }

    public static void loadDefault() {
        log("Loading horse types...");
        String[] candidates = new String[]{
                "horses/horseTypes.json",          // classpath (assets packed)
                "assets/horses/horseTypes.json"    // filesystem (dev)
        };

        final String json;
        try {
            json = readFirstAvailable(candidates);
        } catch (IllegalStateException e) {
            error("Horse types JSON not found in candidates.", e);
            BY_ID.clear();
            BY_KEY.clear();
            return;
        }

        log("JSON loaded. length=" + json.length());

        HorseTypesConfig cfg;
        try {
            cfg = GSON.fromJson(json, HorseTypesConfig.class);
        } catch (Exception e) {
            error("Failed to parse horse types JSON.", e);
            BY_ID.clear();
            BY_KEY.clear();
            return;
        }

        BY_ID.clear();
        BY_KEY.clear();

        int count = 0;
        if (cfg != null && cfg.horses != null) {
            for (HorseDTO dto : cfg.horses) {
                HorsePalette palette = null;
                if (dto.palette != null) {
                    try {
                        palette = new HorsePalette(
                                Color.valueOf(dto.palette.body),
                                Color.valueOf(dto.palette.mane),
                                Color.valueOf(dto.palette.tail)
                        );
                    } catch (Exception e) {
                        error("Invalid palette for horse key=" + dto.key + " id=" + dto.id, e);
                    }
                }
                HorseType type = new HorseType(dto.id, dto.key, dto.name, palette, dto.color);
                BY_ID.put(type.getId(), type);
                BY_KEY.put(type.getKey(), type);
                count++;
                log("Loaded type id=" + type.getId() + " key=" + type.getKey());
            }
        }

        log("Horse types loaded: " + count);
    }

    public static HorseType getById(int id) {
        HorseType t = BY_ID.get(id);
        if (t == null) {
            log("getById(" + id + ") -> null");
        } else {
            log("getById(" + id + ") -> " + t.getKey());
        }
        return t;
    }

    public static HorseType getByKey(String key) {
        HorseType t = BY_KEY.get(key);
        if (t == null) {
            log("getByKey(" + key + ") -> null");
        } else {
            log("getByKey(" + key + ") -> id=" + t.getId());
        }
        return t;
    }

    public static Collection<HorseType> all() {
        log("all() -> " + BY_ID.size() + " types");
        return Collections.unmodifiableCollection(BY_ID.values());
    }

    private static String readFirstAvailable(String... candidates) {
        for (String c : candidates) {
            log("Trying classpath: " + c);
            String s = readClasspath(c);
            if (s != null) {
                log("Found in classpath: " + c);
                return s;
            }
        }
        for (String c : candidates) {
            Path p = Path.of(c);
            log("Trying filesystem: " + p.toAbsolutePath());
            if (Files.exists(p)) {
                try {
                    String content = Files.readString(p, StandardCharsets.UTF_8);
                    log("Found in filesystem: " + p.toAbsolutePath());
                    return content;
                } catch (IOException e) {
                    error("Failed reading file: " + p.toAbsolutePath(), e);
                }
            }
        }
        throw new IllegalStateException("Could not find horse types JSON in classpath or file system.");
    }

    private static String readClasspath(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) cl = HorseTypeRegistry.class.getClassLoader();
        try (InputStream in = cl.getResourceAsStream(path)) {
            if (in == null) return null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line).append('\n');
                return sb.toString();
            }
        } catch (IOException e) {
            error("Classpath read failed for: " + path, e);
            return null;
        }
    }

    private static void log(String msg) {
        if (Gdx.app != null) Gdx.app.log(TAG, msg);
        else System.out.println(TAG + ": " + msg);
    }

    private static void error(String msg, Throwable t) {
        if (Gdx.app != null) Gdx.app.error(TAG, msg, t);
        else {
            System.err.println(TAG + " ERROR: " + msg);
            if (t != null) t.printStackTrace();
        }
    }
}