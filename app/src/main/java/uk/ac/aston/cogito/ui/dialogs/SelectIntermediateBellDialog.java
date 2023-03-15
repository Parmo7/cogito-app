package uk.ac.aston.cogito.ui.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SelectIntermediateBellDialog extends SelectBellDialog {

    public SelectIntermediateBellDialog(BottomDialogListener listener, @NonNull Context context) {
        super(listener, context);
    }

    @Override
    int getTitleResId() {
        return R.string.dialog_bell_intermediate_title;
    }

    @Override
    public void show(SessionConfig sessionConfig) {
        super.show();
        for (int idx = 0; idx < alLBellSoundNames.length; idx++) {
            String candidateName = alLBellSoundNames[idx];
            String bellSoundName = sessionConfig.getIntermediateBellSound().getName();

            if (candidateName.equals(bellSoundName)) {
                bellPicker.setValue(idx);
                break;
            }
        }
    }
}
