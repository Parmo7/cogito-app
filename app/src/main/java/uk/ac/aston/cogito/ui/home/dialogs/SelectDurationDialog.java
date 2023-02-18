package uk.ac.aston.cogito.ui.home.dialogs;

import android.content.Context;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SelectDurationDialog extends FormBottomDialog {

    private NumberPicker durationPicker;

    public SelectDurationDialog(BottomDialogListener listener, @NonNull Context context, SessionConfig sessionConfig) {
        super(listener, context, sessionConfig, R.layout.dialog_select_duration);
    }

    @Override
    protected void initializeForm() {
        durationPicker = findViewById(R.id.duration_picker);

        durationPicker.setMinValue(1);
        durationPicker.setMaxValue(60);
        durationPicker.setValue(sessionConfig.getDuration());
    }

    public Integer getValue() {
        if (durationPicker != null) {
            return durationPicker.getValue();
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        durationPicker.setValue(sessionConfig.getDuration());
    }
}
