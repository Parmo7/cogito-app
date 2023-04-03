package uk.ac.aston.cogito.model;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.Mood;

public class MoodManager {

    private static final Mood MOOD_HAPPY     = new Mood("Happy",     R.drawable.emoji_happy);
    private static final Mood MOOD_EXCITED   = new Mood("Excited",   R.drawable.emoji_excited);
    private static final Mood MOOD_GRATEFUL  = new Mood("Grateful",  R.drawable.emoji_grateful);
    private static final Mood MOOD_RELAXED   = new Mood("Relaxed",   R.drawable.emoji_relaxed);
    private static final Mood MOOD_CONTENT   = new Mood("Content",   R.drawable.emoji_content);
    private static final Mood MOOD_TIRED     = new Mood("Tired",     R.drawable.emoji_tired);
    private static final Mood MOOD_UNSURE    = new Mood("Unsure",    R.drawable.emoji_unsure);
    private static final Mood MOOD_BORED     = new Mood("Bored",     R.drawable.emoji_bored);
    private static final Mood MOOD_ANXIOUS   = new Mood("Anxious",   R.drawable.emoji_anxious);
    private static final Mood MOOD_ANGRY     = new Mood("Angry",     R.drawable.emoji_angry);
    private static final Mood MOOD_STRESSED  = new Mood("Stressed",  R.drawable.emoji_stressed);
    private static final Mood MOOD_SAD       = new Mood("Sad",       R.drawable.emoji_sad);

    private static final List<Mood> ALL_MOODS = initMoodsList();

    private static List<Mood> initMoodsList() {
        List<Mood> list = new ArrayList<>();

        list.add(MOOD_HAPPY);
        list.add(MOOD_EXCITED);
        list.add(MOOD_GRATEFUL);
        list.add(MOOD_RELAXED);
        list.add(MOOD_CONTENT);
        list.add(MOOD_TIRED);
        list.add(MOOD_UNSURE);
        list.add(MOOD_BORED);
        list.add(MOOD_ANXIOUS);
        list.add(MOOD_ANGRY);
        list.add(MOOD_STRESSED);
        list.add(MOOD_SAD);

        return list;
    }
    
    public static Mood getMoodByName(String name) {
        for (Mood mood : ALL_MOODS) {
            if (mood.getName().equals(name)) {
                return mood;
            }
        }

        return null;
    }
}
