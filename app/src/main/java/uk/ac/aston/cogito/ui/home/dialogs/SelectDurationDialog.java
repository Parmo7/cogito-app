package uk.ac.aston.cogito.ui.home.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.ui.home.HomeFragment;

public class SelectDurationDialog extends FormBottomDialog {

    NumberPicker durationPicker;

    public SelectDurationDialog(BottomDialogListener listener, @NonNull Context context) {
        super(listener, context, R.layout.dialog_select_duration);
    }

    protected void initializeForm() {
        durationPicker = findViewById(R.id.duration_picker);

        durationPicker.setMinValue(1);
        durationPicker.setMaxValue(60);
        durationPicker.setValue(HomeFragment._DEFAULT_DURATION);
    }

    public Integer getValue() {
        if (durationPicker != null) {
            return durationPicker.getValue();
        }
        return null;
    }
}
