package animations;

import data.horse.HorseType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class HorseAnimationCache {
    private static final ConcurrentMap<Integer, HorseAnimationSet> CACHE = new ConcurrentHashMap<>();

    private HorseAnimationCache() {
    }

    public static HorseAnimationSet get(HorseType type) {
        if (type == null) return null;
        return CACHE.computeIfAbsent(type.getId(), id -> HorseAnimationAssembler.assemble(type));
    }

    public static boolean contains(int typeId) {
        return CACHE.containsKey(typeId);
    }

    public static int size() {
        return CACHE.size();
    }
}