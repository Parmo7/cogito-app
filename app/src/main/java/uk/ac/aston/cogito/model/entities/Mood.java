package uk.ac.aston.cogito.model.entities;

import java.io.Serializable;

public class Mood implements Serializable {

    private String name;
    private int emojiResId;

    public Mood() {}

    public Mood(String name, int resId) {
        this.name = name;
        this.emojiResId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmojiResId() {
        return emojiResId;
    }

    public void setEmojiResId(int emojiResId) {
        this.emojiResId = emojiResId;
    }
}
