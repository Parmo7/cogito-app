package uk.ac.aston.cogito.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.ac.aston.cogito.R;

public class BackgroundMusicManager {

    private static final Map<String, Integer> BG_MUSIC_DATA = initializeBgMusicMap();

    private static Map<String, Integer> initializeBgMusicMap() {
        Map<String, Integer> map = new HashMap<>();

        map.put("Forest", R.raw.forest);
        map.put("Piano", R.raw.piano);
        map.put("River", R.raw.river);
        map.put("Spa", R.raw.spa);
        map.put("Zen", R.raw.zen);

        return map;
    }

    public static String[] getMusicTitles() {
        Set<String> keySet = BG_MUSIC_DATA.keySet();
        return keySet.toArray(new String[BG_MUSIC_DATA.size()]);
    }
}
