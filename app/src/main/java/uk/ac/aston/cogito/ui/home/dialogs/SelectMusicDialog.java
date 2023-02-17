package uk.ac.aston.cogito.ui.home.dialogs;


import android.content.Context;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.BackgroundMusicManager;

public class SelectMusicDialog extends FormBottomDialog {

    private NumberPicker musicPicker;
    private String[] allBgMusicTracks;

    public SelectMusicDialog(BottomDialogListener listener, @NonNull Context context) {
        super(listener, context, R.layout.dialog_select_bg_music);

        allBgMusicTracks = BackgroundMusicManager.getMusicTitles();
    }

    @Override
    protected void initializeForm() {
        musicPicker = findViewById(R.id.music_picker);

        musicPicker.setDisplayedValues(allBgMusicTracks);
        musicPicker.setMinValue(0);
        musicPicker.setMaxValue(allBgMusicTracks.length - 1);
    }

    public String getValue() {
        if (musicPicker != null) {
            int index = musicPicker.getValue();
            return allBgMusicTracks[index];
        }
        return null;
    }
}
