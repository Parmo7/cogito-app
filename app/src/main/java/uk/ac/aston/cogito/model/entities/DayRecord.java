package uk.ac.aston.cogito.model.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DayRecord implements Serializable {

    private String date;
    private int numSessions;
    private Mood mood;

    public DayRecord(String date, int numSessions, Mood mood) {
        this.date = date;
        this.numSessions = numSessions;
        this.mood = mood;
    }

    public DayRecord() {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumSessions() {
        return numSessions;
    }

    public void setNumSessions(int numSessions) {
        this.numSessions = numSessions;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }
}
