package uk.ac.aston.cogito.model.entities;

import java.io.Serializable;

public class AudioResource implements Serializable {

    private final String name;
    private final int resId;

    public AudioResource(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public int getResId() {
        return resId;
    }
}
