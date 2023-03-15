package uk.ac.aston.cogito.ui.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SelectStartBellDialog extends SelectBellDialog{

    public SelectStartBellDialog(BottomDialogListener listener, @NonNull Context context) {
        super(listener, context);
    }

    @Override
    int getTitleResId() {
        return R.string.dialog_bell_start_title;
    }

    @Override
    public void show(SessionConfig sessionConfig) {
        super.show();
        for (int idx = 0; idx < alLBellSoundNames.length; idx++) {
            String candidateName = alLBellSoundNames[idx];
            String bellSoundName = sessionConfig.getStartBellSound().getName();

            if (candidateName.equals(bellSoundName)) {
                bellPicker.setValue(idx);
                break;
            }
        }
    }
}
