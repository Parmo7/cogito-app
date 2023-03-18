package uk.ac.aston.cogito.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import uk.ac.aston.cogito.CogitoApp;
import uk.ac.aston.cogito.model.entities.Settings;

public class SettingsViewModel extends ViewModel {

    public static final String DEFAULT_TIME_FORMAT = "HH:mm";
    public static final String DEFAULT_TIME = "08:00";
    public static final int DEFAULT_MIN_REMINDERS = 1;
    public static final int DEFAULT_MAX_REMINDERS = 5;

    private final MutableLiveData<Settings> settings;
    private final DataManager dataManager;

    public SettingsViewModel() {
        super();

        settings = new MutableLiveData<>();
        dataManager = DataManager.getInstance(CogitoApp.getAppContext());

        updateLocalSettings();
    }


    public void setSettings(Settings settings) {
        dataManager.setSettings(settings);
        updateLocalSettings();
    }


    public LiveData<Settings> getSettings() {
        return settings;
    }


    private void updateLocalSettings() {
        settings.setValue(dataManager.getUpToDateSettings());
    }
}