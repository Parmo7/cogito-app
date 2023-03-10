package uk.ac.aston.cogito.ui.home.dialogs;

import android.content.Context;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SelectNumIntBellsDialog extends FormBottomDialog{

    private NumberPicker intermediateBellsPicker;

    public SelectNumIntBellsDialog(BottomDialogListener listener, @NonNull Context context, SessionConfig sessionConfig) {
        super(listener, context, sessionConfig, R.layout.dialog_num_intbells);
    }

    @Override
    protected void initializeForm() {
        intermediateBellsPicker = findViewById(R.id.intbells_picker);

        intermediateBellsPicker.setMinValue(0);
        intermediateBellsPicker.setMaxValue(29);
        intermediateBellsPicker.setValue(sessionConfig.getNumIntermediateBells());
    }

    public Integer getValue() {
        if (intermediateBellsPicker != null) {
            return intermediateBellsPicker.getValue();
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        intermediateBellsPicker.setValue(sessionConfig.getDuration());
    }
}
