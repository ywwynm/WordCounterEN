package utils;

import java.util.*;

/**
 * Created by 张启 on 2015/11/28.
 * Kinds of algorithms.
 */
public class Algorithms {

    private Algorithms() {
        // avoid instantiating this class.
    }

    /**
     * Sort a map by value in positive of inverted sequence.
     * @param map map to sort.
     * @param reverseOrder {@code true} if we should use reverse order.
     *                      {@code false} otherwise.
     * @return sorted map entries.
     */
    public static List<Map.Entry> sortMapByValue(Map map, boolean reverseOrder) {
        return reverseOrder ? sortMapByValueReversely(map) : sortMapByValue(map);
    }

    private static List<Map.Entry> sortMapByValue(Map map) {
        List<Map.Entry> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> {
            int v1 = (int) o1.getValue();
            int v2 = (int) o2.getValue();
            if (v1 == v2) {
                return 0;
            } return v1 < v2 ? -1 : 1;
        });
        return list;
    }

    private static List<Map.Entry> sortMapByValueReversely(Map map) {
        List<Map.Entry> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> {
            int v1 = (int) o1.getValue();
            int v2 = (int) o2.getValue();
            if (v1 == v2) {
                return 0;
            } return v1 < v2 ? 1 : -1;
        });
        return list;
    }

}
