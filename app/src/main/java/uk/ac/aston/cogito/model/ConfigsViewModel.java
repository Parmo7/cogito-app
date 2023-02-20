package uk.ac.aston.cogito.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cogito.CogitoApp;
import uk.ac.aston.cogito.MainActivity;
import uk.ac.aston.cogito.model.entities.SessionConfig;


public class ConfigsViewModel extends ViewModel {

    private MutableLiveData<List<SessionConfig>> allConfigs;
    private MutableLiveData<SessionConfig> latestConfig;

    private DataManager dataManager;

    public ConfigsViewModel() {
        super();
        allConfigs = new MutableLiveData<>(new ArrayList<>());
        latestConfig = new MutableLiveData<>();

        dataManager = DataManager.getInstance(CogitoApp.getAppContext());

        updateAllConfigs();
        updateLatestConfig();
    }

    public LiveData<List<SessionConfig>> getAllConfigs() {
        return allConfigs;
    }

    public boolean updateConfig(SessionConfig newConfig) {
        if (dataManager.updateConfig(newConfig)) {
            updateAllConfigs();
            return true;
        }
        return false;
    }


    public boolean addConfig(SessionConfig newConfig) {
        if (dataManager.addConfig(newConfig)) {
            updateAllConfigs();
            return true;
        }
        return false;
    }


    public boolean removeConfig(int id) {
        if (dataManager.removeConfig(id)) {
            updateAllConfigs();
            return true;
        }
        return false;
    }

    private void updateAllConfigs() {
        allConfigs.setValue(dataManager.getAllUpToDateConfigs());
    }

    public LiveData<SessionConfig> getLatestConfig() {
        return latestConfig;
    }

    public void setLatestConfig(SessionConfig latestConfig) {
        dataManager.setLatestConfig(latestConfig);
        updateLatestConfig();
    }

    private void updateLatestConfig() {
        latestConfig.setValue(dataManager.getLatestConfig());
    }
}

