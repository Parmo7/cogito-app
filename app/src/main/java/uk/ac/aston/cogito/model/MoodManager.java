package uk.ac.aston.cogito.model;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.Mood;

public class MoodManager {
    
    private static final Mood MOOD_HAPPY     = new Mood("Happy",     R.drawable.emoji_happy);
    private static final Mood MOOD_GRATEFUL  = new Mood("Grateful",  R.drawable.emoji_grateful);
    private static final Mood MOOD_IN_LOVE   = new Mood("In Love",   R.drawable.emoji_inlove);
    private static final Mood MOOD_CURIOUS   = new Mood("Curious",   R.drawable.emoji_curious);
    private static final Mood MOOD_SAD       = new Mood("Sad",       R.drawable.emoji_sad);
    private static final Mood MOOD_HURT      = new Mood("Hurt",      R.drawable.emoji_hurt);
    private static final Mood MOOD_ANXIOUS   = new Mood("Anxious",   R.drawable.emoji_anxious);
    private static final Mood MOOD_SLEEPY    = new Mood("Sleepy",    R.drawable.emoji_sleepy);
    private static final Mood MOOD_SICK      = new Mood("Sick",      R.drawable.emoji_sick);
    private static final Mood MOOD_APATHETIC = new Mood("Apathetic", R.drawable.emoji_apathetic);
    private static final Mood MOOD_EXHAUSTED = new Mood("Exhausted", R.drawable.emoji_exhausted);

    private static final List<Mood> ALL_MOODS = initMoodsList();

    private static List<Mood> initMoodsList() {
        List<Mood> list = new ArrayList<>();

        list.add(MOOD_HAPPY);
        list.add(MOOD_GRATEFUL);
        list.add(MOOD_IN_LOVE);
        list.add(MOOD_CURIOUS);
        list.add(MOOD_SAD);
        list.add(MOOD_HURT);
        list.add(MOOD_ANXIOUS);
        list.add(MOOD_SLEEPY);
        list.add(MOOD_SICK);
        list.add(MOOD_APATHETIC);
        list.add(MOOD_EXHAUSTED);

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
