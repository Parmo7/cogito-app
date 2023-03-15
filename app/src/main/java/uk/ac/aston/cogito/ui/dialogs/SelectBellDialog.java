package uk.ac.aston.cogito.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.BellSoundManager;
import uk.ac.aston.cogito.model.entities.AudioResource;

public abstract class SelectBellDialog extends FormBottomDialog {

    protected NumberPicker bellPicker;
    protected AudioResource[] allBellSounds;
    protected String[] alLBellSoundNames;

    public SelectBellDialog(BottomDialogListener listener, @NonNull Context context) {
        super(listener, context, R.layout.dialog_select_bell_sound);
        allBellSounds = BellSoundManager.getAllBellsArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int titleResId = getTitleResId();
        TextView dialogTitle = findViewById(R.id.title);
        dialogTitle.setText(titleResId);
    }

    abstract int getTitleResId();

    @Override
    protected void initializeForm() {
        bellPicker = findViewById(R.id.bell_sound_picker);

        // Extract array of all bell names
        alLBellSoundNames = new String[allBellSounds.length];
        for (int idx = 0; idx < allBellSounds.length; idx++) {
            AudioResource audioRes = allBellSounds[idx];
            alLBellSoundNames[idx] = audioRes.getName();
        }

        bellPicker.setDisplayedValues(alLBellSoundNames);
        bellPicker.setMinValue(0);
        bellPicker.setMaxValue(alLBellSoundNames.length - 1);
    }

    public AudioResource getValue() {
        if (bellPicker != null) {
            int index = bellPicker.getValue();
            return allBellSounds[index];
        }
        return null;
    }
}

