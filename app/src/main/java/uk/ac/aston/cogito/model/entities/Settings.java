package uk.ac.aston.cogito.model.entities;

import static uk.ac.aston.cogito.model.SettingsViewModel.DEFAULT_TIME;
import static uk.ac.aston.cogito.model.SettingsViewModel.DEFAULT_MAX_REMINDERS;

import java.io.Serializable;

public class Settings implements Serializable {

    // Variables relating to formal practice
    private boolean formalEnabled;
    private int numFormal;
    private String[] formalTimes;

    // Variables relating to informal practice
    private boolean informalEnabled;
    private int numInformal;

    public Settings() {
        formalTimes = new String[DEFAULT_MAX_REMINDERS];
        for (int i = 0; i < formalTimes.length; i++) {
            formalTimes[i] = DEFAULT_TIME;
        }

        numFormal = 1;
        numInformal = 3;
    }


    public boolean isFormalEnabled() {
        return formalEnabled;
    }

    public void setFormalEnabled(boolean formalEnabled) {
        this.formalEnabled = formalEnabled;
    }

    public int getNumFormal() {
        return numFormal;
    }

    public void setNumFormal(int numFormal) {
        this.numFormal = numFormal;
    }

    public String[] getFormalTimes() {
        return formalTimes;
    }

    public void setFormalTimes(String[] formalTimes) {
        this.formalTimes = formalTimes;
    }

    public boolean isInformalEnabled() {
        return informalEnabled;
    }

    public void setInformalEnabled(boolean informalEnabled) {
        this.informalEnabled = informalEnabled;
    }

    public int getNumInformal() {
        return numInformal;
    }

    public void setNumInformal(int numInformal) {
        this.numInformal = numInformal;
    }
}
