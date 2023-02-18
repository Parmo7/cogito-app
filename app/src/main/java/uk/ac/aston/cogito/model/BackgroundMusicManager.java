package uk.ac.aston.cogito.model;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.AudioResource;

public class BackgroundMusicManager {

    private static final AudioResource TRACK_1 = new AudioResource("Forest", R.raw.forest);
    private static final AudioResource TRACK_2 = new AudioResource("Piano", R.raw.piano);
    private static final AudioResource TRACK_3 = new AudioResource("River", R.raw.river);
    private static final AudioResource TRACK_4 = new AudioResource("Spa", R.raw.spa);
    private static final AudioResource TRACK_5 = new AudioResource("Zen", R.raw.zen);

    private static final List<AudioResource> BG_MUSIC_LIST = initializeBgMusicList();


    private static List<AudioResource> initializeBgMusicList() {
        List<AudioResource> data = new ArrayList<>();

        data.add(TRACK_1);
        data.add(TRACK_2);
        data.add(TRACK_3);
        data.add(TRACK_4);
        data.add(TRACK_5);

        return data;
    }

    public static AudioResource[] getAllMusicArray() {
        AudioResource[] allBgMusicArray = BG_MUSIC_LIST.toArray(new AudioResource[BG_MUSIC_LIST.size()]);
        return allBgMusicArray;
    }

    public static List<AudioResource> getAllMusic() {
        return BG_MUSIC_LIST;
    }
}
