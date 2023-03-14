package uk.ac.aston.cogito.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import uk.ac.aston.cogito.CogitoApp;
import uk.ac.aston.cogito.model.entities.DayRecord;
import uk.ac.aston.cogito.model.entities.Mood;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class DataManager {

    private enum FileType {
        ALL_CONFIGS,
        LATEST_CONFIG,
        HISTORY,
        APP_SETTINGS
    }

    private static DataManager _INSTANCE;
    private final Context context;
    private List<SessionConfig> allConfigs;
    private SessionConfig latestConfig;

    private List<DayRecord> history;

    private DataManager(Context context) {
        this.context = context;
        allConfigs = new ArrayList<>();
        history = new ArrayList<>();

        retrieveFromMemory(FileType.ALL_CONFIGS);
        retrieveFromMemory(FileType.LATEST_CONFIG);
        retrieveFromMemory(FileType.HISTORY);
    }

    protected static DataManager getInstance(Context context) {
        if (_INSTANCE == null) {
            _INSTANCE = new DataManager(context);
        }
        return _INSTANCE;
    }

    protected List<SessionConfig> getAllUpToDateConfigs() {
        return allConfigs;
    }

    protected SessionConfig getLatestConfig() {
        return latestConfig;
    }

    protected List<DayRecord> getUpToDateHistory() {
        return history;
    }

    private SessionConfig findConfigById(int id) {
        for (SessionConfig config : allConfigs) {
            if (config.getId() == id) {
                return config;
            }
        }

        return null;
    }

    protected boolean updateConfig(SessionConfig newConfig) {
        SessionConfig existingConfig = findConfigById(newConfig.getId());

        // If the config exists in the database...
        if (existingConfig != null) {

            // ... update it
            existingConfig.setName(newConfig.getName());
            existingConfig.setDuration(newConfig.getDuration());
            existingConfig.setBgMusic(newConfig.getBgMusic());

            // and then save it to memory
            saveToMemory(FileType.ALL_CONFIGS);
            showToast("Configuration updated.");
            return true;
        }

        return false;
    }

    protected boolean addConfig(SessionConfig newConfig) {

        // Make sure the config doesn't already have an ID
        if (newConfig.getId() == 0) {

            int assignedID;

            // Check if the configs list is empty
            if (allConfigs.isEmpty()) {
                assignedID = 0;

            } else {
                // Retrieve the latest ID
                int latestID = allConfigs.get(allConfigs.size() - 1).getId();
                assignedID = latestID + 1;
            }

            newConfig.setId(assignedID);

            allConfigs.add(newConfig);
            saveToMemory(FileType.ALL_CONFIGS);
            showToast("Configuration saved.");
            return true;
        }

        return false;
    }

    protected boolean removeConfig(int id) {
        SessionConfig config = findConfigById(id);

        if (config != null) {
            allConfigs.remove(config);
            saveToMemory(FileType.ALL_CONFIGS);
            return true;
        }

        return false;
    }

    private void saveToMemory(FileType fileType) {
        String filename = fileType.toString().toLowerCase();

        try {
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            Object obj = null;
            switch (fileType) {
                case ALL_CONFIGS:
                    obj = allConfigs;
                    break;

                case LATEST_CONFIG:
                    obj = latestConfig;
                    break;

                case HISTORY:
                    obj = history;
                    break;
            }

            String json = objectWriter.writeValueAsString(obj);

            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();

        } catch (Exception e) {
            showToast("Error while saving your data...");
        }
    }

    private void retrieveFromMemory(FileType fileType) {
        String filename = fileType.toString().toLowerCase();

        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Read the file
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
            String contents = stringBuilder.toString();
            ObjectMapper mapper = new ObjectMapper();

            switch (fileType) {
                case ALL_CONFIGS:
                    // Update the local copy of the config list
                    allConfigs = Arrays.asList(mapper.readValue(contents, SessionConfig[].class));
                    break;

                case LATEST_CONFIG:
                    // Update the local copy of the config
                    latestConfig = mapper.readValue(contents, SessionConfig.class);
                    break;

                case HISTORY:
                    // Update the local copy of the history
                    history = Arrays.asList(mapper.readValue(contents, DayRecord[].class));
                    break;
            }

            fileInputStream.close();
            inputStreamReader.close();
            reader.close();


        } catch (FileNotFoundException e) {
            // Do nothing, i.e. there is no backup to retrieve.
        } catch (IOException e) {
            showToast("Could not retrieve your data");
        }
    }

    protected void setLatestConfig(SessionConfig latestConfig) {
        this.latestConfig = latestConfig;
        saveToMemory(FileType.LATEST_CONFIG);
    }


    protected DayRecord findDayRecordByDate(String date) {
        for (DayRecord candidateRecord : history) {
            if (candidateRecord.getDate().equals(date)) {
                return candidateRecord;
            }
        }

        return null;
    }


    protected void recordSession(String date) {
        DayRecord existingDayRecord = findDayRecordByDate(date);

        // If a day record exists in the database...
        if (existingDayRecord != null) {
            int updatedNumSessions = existingDayRecord.getNumSessions() + 1;
            existingDayRecord.setNumSessions(updatedNumSessions);

        // ... else, create one
        } else {
            DayRecord newRecord = new DayRecord(date, 1, null);
            history.add(newRecord);
        }

        saveToMemory(FileType.HISTORY);
    }


    protected void recordMood(String date, Mood mood) {
        DayRecord existingDayRecord = findDayRecordByDate(date);

        // If a day record exists in the database...
        if (existingDayRecord != null) {
            existingDayRecord.setMood(mood);

        // ... else, create one
        } else {
            DayRecord newRecord = new DayRecord();
            newRecord.setDate(date);
            newRecord.setMood(mood);
            history.add(newRecord);
        }

        saveToMemory(FileType.HISTORY);
    }


    private void showToast(String text) {
        Toast toast = Toast.makeText(CogitoApp.getAppContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}

