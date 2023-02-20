package uk.ac.aston.cogito.model.entities;

import java.io.Serializable;

public class AudioResource implements Serializable {

    private String name;
    private int resId;

    public AudioResource() {}

    public AudioResource(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
