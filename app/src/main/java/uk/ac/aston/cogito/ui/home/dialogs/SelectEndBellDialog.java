package uk.ac.aston.cogito.ui.home.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SelectEndBellDialog extends SelectBellDialog {

    public SelectEndBellDialog(BottomDialogListener listener, @NonNull Context context) {
        super(listener, context);
    }

    @Override
    int getTitleResId() {
        return R.string.dialog_bell_end_title;
    }

    @Override
    public void show(SessionConfig sessionConfig) {
        super.show();
        for (int idx = 0; idx < alLBellSoundNames.length; idx++) {
            String candidateName = alLBellSoundNames[idx];
            String bellSoundName = sessionConfig.getEndBellSound().getName();

            if (candidateName.equals(bellSoundName)) {
                bellPicker.setValue(idx);
                break;
            }
        }
    }
}
