package uk.ac.aston.cogito.ui.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;
import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.MoodManager;
import uk.ac.aston.cogito.model.entities.DayRecord;
import uk.ac.aston.cogito.model.entities.Mood;
import uk.ac.aston.cogito.model.entities.SessionConfig;
import uk.ac.aston.cogito.ui.session.CheckInFragment;

public class SelectMoodDialog extends FormBottomDialog {

    private ChipGroup chipGroup;

    public SelectMoodDialog(BottomDialogListener listener, @NonNull Context context) {
        super(listener, context, R.layout.dialog_select_mood);
        chipGroup = findViewById(R.id.chip_group);
    }

    @Override
    protected void initializeForm() {
        // Enable/disable the done button based on the selection
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                doneBtn.setEnabled(false);
            } else {
                doneBtn.setEnabled(true);
            }
        });
    }

    @Override
    public void show(SessionConfig sessionConfig) {}


    public void show(DayRecord dayRecord) {
        super.show();

        Mood mood = dayRecord.getMood();
        int chipId;

        // If the mood has ben set
        if (mood != null) {
            switch (mood.getName()) {
                case "Happy":
                    chipId = R.id.chip_happy;
                    break;

                case "Grateful":
                    chipId = R.id.chip_grateful;
                    break;

                case "In Love":
                    chipId = R.id.chip_inlove;
                    break;

                case "Curious":
                    chipId = R.id.chip_curious;
                    break;

                case "Sad":
                    chipId = R.id.chip_sad;
                    break;

                case "Hurt":
                    chipId = R.id.chip_hurt;
                    break;

                case "Anxious":
                    chipId = R.id.chip_anxious;
                    break;

                case "Sleepy":
                    chipId = R.id.chip_sleepy;
                    break;

                case "Sick":
                    chipId = R.id.chip_sick;
                    break;

                case "Apathetic":
                    chipId = R.id.chip_apathetic;
                    break;

                case "Exhausted":
                    chipId = R.id.chip_exhausted;
                    break;

                default:
                    chipId = R.id.chip_none;
                    break;
            }

        // Otherwise
        } else {
            chipId = R.id.chip_none;
        }

        chipGroup.check(chipId);
    }

    public String getValue() {
        int id = chipGroup.getCheckedChipId();

        // If a chip has been selected
        if (id != View.NO_ID) {
            Chip selectedChip = chipGroup.findViewById(id);
            return selectedChip.getText().toString();
        }
        return null;
    }
}
