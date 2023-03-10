package uk.ac.aston.cogito.model;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.AudioResource;

public class BellSoundManager {

    private static final int NULL_RES_ID = 0;

    private static final AudioResource BELL_1 = new AudioResource("Chakra", R.raw.bell_chakra);
    private static final AudioResource BELL_2 = new AudioResource("Classic", R.raw.bell_classic);
    private static final AudioResource BELL_3 = new AudioResource("Heavy", R.raw.bell_heavy);
    private static final AudioResource BELL_4 = new AudioResource("Hit", R.raw.bell_hit);
    private static final AudioResource BELL_5 = new AudioResource("Tibetan", R.raw.bell_tibetan);
    private static final AudioResource BELL_6 = new AudioResource("None", NULL_RES_ID);
    public static final AudioResource DEFAULT_START_BELL = BELL_2;
    public static final AudioResource DEFAULT_END_BELL = BELL_2;
    public static final AudioResource DEFAULT_INTERMEDIATE_BELL = BELL_5;

    private static final List<AudioResource> BELLS_LIST = initBellsList();


    private static List<AudioResource> initBellsList() {
        List<AudioResource> data = new ArrayList<>();

        data.add(BELL_1);
        data.add(BELL_2);
        data.add(BELL_3);
        data.add(BELL_4);
        data.add(BELL_5);
        data.add(BELL_6);

        return data;
    }

    public static AudioResource[] getAllBellsArray() {
        AudioResource[] allBellsArray = BELLS_LIST.toArray(new AudioResource[BELLS_LIST.size()]);
        return allBellsArray;
    }

    public static List<AudioResource> getAllBells() {
        return BELLS_LIST;
    }    
}
