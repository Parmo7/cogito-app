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
import uk.ac.aston.cogito.MainActivity;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class DataManager {

    private enum FileType {
        ALL_CONFIGS,
        LATEST_CONFIG,
        APP_SETTINGS
    }

    private static DataManager _INSTANCE;
    private final Context context;
    private List<SessionConfig> allConfigs;
    private SessionConfig latestConfig;

    private DataManager(Context context) {
        this.context = context;
        allConfigs = new ArrayList<SessionConfig>();

        retrieveFromMemory(FileType.ALL_CONFIGS);
        retrieveFromMemory(FileType.LATEST_CONFIG);
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
            return true;
        }

        return false;
    }

    protected boolean addConfig(SessionConfig newConfig) {

        // TODO The following line has been commented out for the time being, because IDs are not
        // always incremented for some reason. This results in the config not being saved.
        // SessionConfig existingConfig = findConfigById(newConfig.getId());
        SessionConfig existingConfig = null;

        // Only add the config if a config with the same ID does not exist already
        if (existingConfig == null) {

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
            if (fileType == FileType.ALL_CONFIGS) {
                obj = allConfigs;
            } else if (fileType == FileType.LATEST_CONFIG) {
                obj = latestConfig;
            }

            String json = objectWriter.writeValueAsString(obj);

            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();

        } catch (Exception e) {
            showToast("Could not save your configurations");
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

            if (fileType == FileType.ALL_CONFIGS) {
                List<SessionConfig> configs = Arrays.asList(mapper.readValue(contents, SessionConfig[].class));

                // Update the local copy of the config list
                allConfigs = new ArrayList<>(configs);

            } else if (fileType == FileType.LATEST_CONFIG) {
                if (contents != null) {
                    SessionConfig config = mapper.readValue(contents, SessionConfig.class);

                    // Update the local copy of the config
                    latestConfig = config;
                }
            }

            fileInputStream.close();
            inputStreamReader.close();
            reader.close();


        } catch (FileNotFoundException e) {
            // Do nothing, i.e. there is no backup to retrieve.
        } catch (IOException e) {
            showToast("Could not retrieve your configurations");
        }
    }

    protected void setLatestConfig(SessionConfig latestConfig) {
        this.latestConfig = latestConfig;
        saveToMemory(FileType.LATEST_CONFIG);
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(CogitoApp.getAppContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}

