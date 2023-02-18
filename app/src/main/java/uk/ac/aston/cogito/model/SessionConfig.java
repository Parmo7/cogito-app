package uk.ac.aston.cogito.model;

import java.io.Serializable;

public class SessionConfig implements Serializable {

    private int duration;
    private int bgMusicId;
    private int startBellSoundId;
    private int endBellSoundId;
    private int intermediateBellSoundId;
    private int numIntermediateBells;



    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBgMusicId() {
        return bgMusicId;
    }

    public void setBgMusicId(int bgMusicId) {
        this.bgMusicId = bgMusicId;
    }

    public int getStartBellSoundId() {
        return startBellSoundId;
    }

    public void setStartBellSoundId(int startBellSoundId) {
        this.startBellSoundId = startBellSoundId;
    }

    public int getEndBellSoundId() {
        return endBellSoundId;
    }

    public void setEndBellSoundId(int endBellSoundId) {
        this.endBellSoundId = endBellSoundId;
    }

    public int getIntermediateBellSoundId() {
        return intermediateBellSoundId;
    }

    public void setIntermediateBellSoundId(int intermediateBellSoundId) {
        this.intermediateBellSoundId = intermediateBellSoundId;
    }

    public int getNumIntermediateBells() {
        return numIntermediateBells;
    }

    public void setNumIntermediateBells(int numIntermediateBells) {
        this.numIntermediateBells = numIntermediateBells;
    }
}
