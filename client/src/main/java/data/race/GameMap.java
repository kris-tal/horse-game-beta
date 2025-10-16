package data.race;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import race.MapObject;
import race.objects.*;

import java.util.HashMap;
import java.util.Map;

public class GameMap {
    private final int lanes;
    private final int length;
    private final MapObject[][] map;  // [lane][column]

    public GameMap(int lanes, int length, MapObject[][] mapData) {
        this.lanes = lanes;
        this.length = length;
        this.map = mapData;
    }

    public MapObject getObject(int lane, int col) {
        if (lane < 0 || lane >= lanes || col < 0 || col >= length)
            return new EmptyObject();
        if (map == null) {
            return new EmptyObject();
        }
        return map[lane][col];
    }

    public int getLanes() {
        return lanes;
    }

    public int getLength() {
        return length;
    }

    public void setObject(int lane, int col, MapObject obj) {
        if (lane >= 0 && lane < lanes && col >= 0 && col < length)
            map[lane][col] = obj;
    }


    public String Serialize() {
        JsonObject mapJson = new JsonObject();
        mapJson.addProperty("lanes", this.getLanes());
        mapJson.addProperty("length", this.getLength());
        mapJson.addProperty("map_data", this.getEncodedMapData()); // Just the string!
        return new Gson().toJson(mapJson);
    }

    public static GameMap deserializeGameMap(String mapJson) {
        try {
            JsonObject outerJson = JsonParser.parseString(mapJson).getAsJsonObject();
            String innerJsonString = outerJson.get("map_data").getAsString();
            JsonObject innerJson = JsonParser.parseString(innerJsonString).getAsJsonObject();

            int lanes = innerJson.get("lanes").getAsInt();
            int length = innerJson.get("length").getAsInt();
            String encodedData = innerJson.get("map_data").getAsString();

            if (encodedData.length() != lanes * length) {
                Gdx.app.error("GameMap", "Invalid map data length. Expected: " +
                        (lanes * length) + ", Got: " + encodedData.length());
                return null;
            }

            MapObject[][] array = new MapObject[lanes][length];
            for (int i = 0; i < lanes; i++) {
                for (int j = 0; j < length; j++) {
                    array[i][j] = createMapObjectFromChar(encodedData.charAt(i * length + j));
                }
            }

            return new GameMap(lanes, length, array);
        } catch (Exception e) {
            Gdx.app.error("GameMap", "Failed to deserialize map: " + e.getMessage());
            return null;
        }
    }

    private String getEncodedMapData() {
        StringBuilder sb = new StringBuilder(lanes * length);
        for (int lane = 0; lane < lanes; lane++) {
            for (int col = 0; col < length; col++) {
                MapObject obj = map[lane][col];
                String type = obj.getClass().getSimpleName();
                Character encoded = TYPE_TO_CHAR.get(type);
                sb.append(encoded != null ? encoded : 'E'); // Default to empty if unknown
            }
        }
        return sb.toString();
    }

    private static final Map<String, Character> TYPE_TO_CHAR = new HashMap<String, Character>() {{
        put("BushObject", 'b');
        put("CoinObject", 'c');
        put("DeathStoneObject", 'd');
        put("ElectricBarrierObject", 'e');
        put("EmptyObject", 'E');
        put("MetaObject", 'm');
        put("PuddleObject", 'p');
        put("BoostObject", 'B');
    }};


    private static MapObject createMapObjectFromChar(char c) {
        switch (c) {
            case 'b':
                return new BushObject();
            case 'c':
                return new CoinObject();
            case 'd':
                return new DeathStoneObject();
            case 'e':
                return new ElectricBarrierObject();
            case 'E':
                return new EmptyObject();
            case 'm':
                return new MetaObject();
            case 'p':
                return new PuddleObject();
            case 'B':
                return new BoostObject();
            default:
                Gdx.app.log("createMapObjectFromChar", "Unknown object!");
                return new EmptyObject();
        }
    }

}
