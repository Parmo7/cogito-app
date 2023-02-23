package uk.ac.aston.cogito.model.entities;

import java.io.Serializable;

import uk.ac.aston.cogito.model.BackgroundMusicManager;

public class SessionConfig implements Serializable {

    public static final int DEFAULT_DURATION = 10;
    public static final AudioResource DEFAULT_BG_MUSIC = BackgroundMusicManager.getAllMusic().get(0);

    private int id;
    private String name;
    private int duration;
    private AudioResource bgMusic;
    private AudioResource startBellSound;
    private AudioResource endBellSound;
    private int numIntermediateBells;
    private AudioResource intermediateBellSound;


    public SessionConfig() {
        name = "";
        duration = DEFAULT_DURATION;
        bgMusic = DEFAULT_BG_MUSIC;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public AudioResource getBgMusic() {
        return bgMusic;
    }

    public void setBgMusic(AudioResource bgMusic) {
        this.bgMusic = bgMusic;
    }

    public AudioResource getStartBellSound() {
        return startBellSound;
    }

    public void setStartBellSound(AudioResource startBellSound) {
        this.startBellSound = startBellSound;
    }

    public AudioResource getEndBellSound() {
        return endBellSound;
    }

    public void setEndBellSound(AudioResource endBellSound) {
        this.endBellSound = endBellSound;
    }

    public int getNumIntermediateBells() {
        return numIntermediateBells;
    }

    public void setNumIntermediateBells(int numIntermediateBells) {
        this.numIntermediateBells = numIntermediateBells;
    }

    public AudioResource getIntermediateBellSound() {
        return intermediateBellSound;
    }

    public void setIntermediateBellSound(AudioResource intermediateBellSound) {
        this.intermediateBellSound = intermediateBellSound;
    }
}
