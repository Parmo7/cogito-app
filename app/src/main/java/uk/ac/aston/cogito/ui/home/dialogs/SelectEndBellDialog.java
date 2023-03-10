package uk.ac.aston.cogito.ui.home.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SelectEndBellDialog extends SelectBellDialog {

    public SelectEndBellDialog(BottomDialogListener listener, @NonNull Context context, SessionConfig sessionConfig) {
        super(listener, context, sessionConfig);
    }

    @Override
    int getTitleResId() {
        return R.string.dialog_bell_end_title;
    }

    @Override
    String getBellSoundName() {
        return getSessionConfig().getEndBellSound().getName();
    }
}
