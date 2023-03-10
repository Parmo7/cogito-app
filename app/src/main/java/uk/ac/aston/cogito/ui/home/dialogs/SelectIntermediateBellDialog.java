package uk.ac.aston.cogito.ui.home.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SelectIntermediateBellDialog extends SelectBellDialog {

    public SelectIntermediateBellDialog(BottomDialogListener listener, @NonNull Context context, SessionConfig sessionConfig) {
        super(listener, context, sessionConfig);
    }

    @Override
    int getTitleResId() {
        return R.string.dialog_bell_intermediate_title;
    }

    @Override
    String getBellSoundName() {
        return getSessionConfig().getIntermediateBellSound().getName();
    }
}
