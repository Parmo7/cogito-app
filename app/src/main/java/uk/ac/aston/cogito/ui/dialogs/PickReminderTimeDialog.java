package uk.ac.aston.cogito.ui.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class PickReminderTimeDialog extends FormBottomDialog {

    private TimePicker timePicker;
    private int index;

    public PickReminderTimeDialog(BottomDialogListener listener, @NonNull Context context, int index) {
        super(listener, context, R.layout.dialog_pick_time);

        timePicker = findViewById(R.id.time_picker);
        this.index = index;
    }


    @Override
    protected void initializeForm() {
        timePicker.setIs24HourView(true);
    }


    public String getValue() {
        int hour = timePicker.getHour();
        int min = timePicker.getMinute();

        String hourString = String.format("%02d", hour);
        String minString = String.format("%02d", min);

        return hourString + ":" + minString;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void show(SessionConfig sessionConfig) {}


    public void show(String timeString) {
        // Set the value of the timer
        int hour = Integer.parseInt(timeString.substring(0, 2));
        int min = Integer.parseInt(timeString.substring(3, 5));

        timePicker.setHour(hour);
        timePicker.setMinute(min);

        // Make sure the dialog displays in full height
        setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        super.show();
    }
}
