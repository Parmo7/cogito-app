package uk.ac.aston.cogito.ui.session;

public class BellSchedule {

    private final long startTimeMillis;
    private final long endTimeMillis;
    private final int audioResId;

    public BellSchedule(int audioResId, long startTimeMillis, long endTimeMillis) {
        this.audioResId = audioResId;
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public long getEndTimeMillis() {
        return endTimeMillis;
    }

    public int getAudioResId() {
        return audioResId;
    }
}
