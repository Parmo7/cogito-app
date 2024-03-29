package uk.ac.aston.cogito.model.entities;

import java.io.Serializable;

import uk.ac.aston.cogito.model.BackgroundMusicManager;
import uk.ac.aston.cogito.model.BellSoundManager;

public class SessionConfig implements Serializable {

    public static final int DEFAULT_DURATION = 10;
    public static final AudioResource DEFAULT_BG_MUSIC = BackgroundMusicManager.DEFAULT_BG_MUSIC;

    public static final AudioResource DEFAULT_START_BELL = BellSoundManager.DEFAULT_START_BELL;
    public static final AudioResource DEFAULT_END_BELL = BellSoundManager.DEFAULT_END_BELL;

    public static final int DEFAULT_NUM_INTERMEDIATE_BELLS = 1;
    public static final AudioResource DEFAULT_INTERMEDIATE_BELL = BellSoundManager.DEFAULT_INTERMEDIATE_BELL;


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
        startBellSound = DEFAULT_START_BELL;
        endBellSound = DEFAULT_END_BELL;
        numIntermediateBells = DEFAULT_NUM_INTERMEDIATE_BELLS;
        intermediateBellSound = DEFAULT_INTERMEDIATE_BELL;
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
