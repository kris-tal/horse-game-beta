package data.race;

import race.MapObject;
import race.objects.EmptyObject;

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
        return map[lane][col];
    }

    public int getLanes() { return lanes; }

    public int getLength() { return length; }

    public void setObject(int lane, int col, MapObject obj) {
        if (lane >= 0 && lane < lanes && col >= 0 && col < length)
            map[lane][col] = obj;
    }
}
