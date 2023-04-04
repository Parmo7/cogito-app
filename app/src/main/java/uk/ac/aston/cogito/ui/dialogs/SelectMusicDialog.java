package uk.ac.aston.cogito.ui.dialogs;


import android.content.Context;
import android.media.MediaPlayer;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.BackgroundMusicManager;
import uk.ac.aston.cogito.model.entities.AudioResource;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SelectMusicDialog extends FormBottomDialog {

    private NumberPicker musicPicker;
    private AudioResource[] allBgMusic;
    private String[] allBgMusicNames;

    private MediaPlayer player;

    public SelectMusicDialog(BottomDialogListener listener, @NonNull Context context) {
        super(listener, context, R.layout.dialog_select_bg_music);

        allBgMusic = BackgroundMusicManager.getAllMusicArray();
    }

    @Override
    protected void initializeForm() {
        musicPicker = findViewById(R.id.music_picker);

        // Extract array of all bg music titles
        allBgMusicNames = new String[allBgMusic.length];
        for (int idx = 0; idx < allBgMusic.length; idx++) {
            AudioResource audioRes = allBgMusic[idx];
            allBgMusicNames[idx] = audioRes.getName();
        }

        musicPicker.setDisplayedValues(allBgMusicNames);
        musicPicker.setMinValue(0);
        musicPicker.setMaxValue(allBgMusicNames.length - 1);

        musicPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            AudioResource selectedBell = allBgMusic[newVal];

            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ignored) {
                }
            }

            if (selectedBell.getResId() != 0) {
                player = MediaPlayer.create(getContext(), selectedBell.getResId());
                player.start();
            }
        });
    }

    public AudioResource getValue() {
        if (musicPicker != null) {
            int index = musicPicker.getValue();
            return allBgMusic[index];
        }
        return null;
    }

    @Override
    public void show(SessionConfig sessionConfig) {
        super.show();
        for (int idx = 0; idx < allBgMusicNames.length; idx++) {
            String candidateName = allBgMusicNames[idx];
            String configuredName = sessionConfig.getBgMusic().getName();

            if (candidateName.equals(configuredName)) {
                musicPicker.setValue(idx);
                break;
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
        }
    }
}
