package uk.ac.aston.cogito.ui.dialogs;

import static uk.ac.aston.cogito.model.SettingsViewModel.DEFAULT_MAX_REMINDERS;
import static uk.ac.aston.cogito.model.SettingsViewModel.DEFAULT_MIN_REMINDERS;

import android.content.Context;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;
import uk.ac.aston.cogito.model.entities.Settings;

public class SelectInformalRemindersDialog extends FormBottomDialog {

    private NumberPicker remindersPicker;

    public SelectInformalRemindersDialog(BottomDialogListener listener, @NonNull Context context) {
        super(listener, context, R.layout.dialog_numreminders);
    }

    @Override
    protected void initializeForm() {
        // Set the title
        TextView titleTextView = findViewById(R.id.title);
        titleTextView.setText(R.string.dialog_numinformal_title);

        // Set the subtitle
        TextView subtitleTextView = findViewById(R.id.subtitle);
        subtitleTextView.setText(R.string.dialog_numinformal_subtitle);

        // Initialise the picker
        remindersPicker = findViewById(R.id.num_reminders_picker);
        remindersPicker.setMinValue(DEFAULT_MIN_REMINDERS);
        remindersPicker.setMaxValue(DEFAULT_MAX_REMINDERS);
    }


    public Integer getValue() {
        if (remindersPicker != null) {
            return remindersPicker.getValue();
        }
        return null;
    }


    @Override
    public void show(SessionConfig sessionConfig) {}


    public void show(int numReminders) {
        super.show();

        if (numReminders < DEFAULT_MIN_REMINDERS || numReminders > DEFAULT_MAX_REMINDERS) {
            numReminders = 1;
        }

        remindersPicker.setValue(numReminders);
    }
}
