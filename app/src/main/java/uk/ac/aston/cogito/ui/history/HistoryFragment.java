package uk.ac.aston.cogito.ui.history;

import static uk.ac.aston.cogito.model.HistoryViewModel.SESSION_DATE_FORMAT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentHistoryBinding;
import uk.ac.aston.cogito.model.HistoryViewModel;
import uk.ac.aston.cogito.model.MoodManager;
import uk.ac.aston.cogito.model.entities.DayRecord;
import uk.ac.aston.cogito.model.entities.Mood;
import uk.ac.aston.cogito.ui.dialogs.BottomDialogListener;
import uk.ac.aston.cogito.ui.dialogs.FormBottomDialog;
import uk.ac.aston.cogito.ui.dialogs.SelectDurationDialog;
import uk.ac.aston.cogito.ui.dialogs.SelectMoodDialog;
import uk.ac.aston.cogito.ui.session.CheckInFragment;

public class HistoryFragment extends Fragment implements BottomDialogListener {

    private FragmentHistoryBinding binding;
    private HistoryViewModel model;
    private CalendarView calendarView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initModel();
        initCalendar();
    }


    private void initCalendar() {
        calendarView = binding.calendarView;

        // Provide custom layout
        calendarView.setCalendarDayLayout(R.layout.calendar_day_cell);

        // Set the minimum and maximum dates as: { 1st Jan 2023 - Today }
        try {
            String startOf2023 = "01/01/2023";
            Date date = SESSION_DATE_FORMAT.parse(startOf2023);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            calendarView.setMinimumDate(cal);
        } catch (Exception ignored) {}
        calendarView.setMaximumDate(Calendar.getInstance());


        // Highlights days with data
        List<EventDay> events = new ArrayList<>();
        try {
            int dot = R.drawable.blue_rounded_rectangle;
            for (DayRecord dayRecord : model.getAllHistory().getValue()) {
                String dateString = dayRecord.getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(SESSION_DATE_FORMAT.parse(dateString));
                events.add(new EventDay(calendar, dot));
            }
            this.calendarView.setEvents(events);

        } catch (Exception ignored) {}

        // Update the fields at the bottom UI for the currently selected date (i.e. today)
        Calendar today = calendarView.getFirstSelectedDate();
        updateBottomUI(today);

        // Set the date click listener
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                if (!eventDay.getCalendar().after(today)) {
                    updateBottomUI(eventDay.getCalendar());
                }
            }

        });

        // If the mood selector is pressed, open a bottom dialog
        binding.historySelectorMood.setOnClickListener(v -> {

            Date selectedDate = calendarView.getFirstSelectedDate().getTime();
            String selectedDateString = SESSION_DATE_FORMAT.format(selectedDate);

            for (DayRecord dayRecord : model.getAllHistory().getValue()) {
                if (selectedDateString.equals(dayRecord.getDate())) {
                    SelectMoodDialog dialog = new SelectMoodDialog(this, getContext() );
                    dialog.show(dayRecord);
                    break;
                }
            }
        });
    }


    private void updateBottomUI(Calendar calendar) {
        Date date = calendar.getTime();
        String dateString = SESSION_DATE_FORMAT.format(date);

        for (DayRecord dayRecord : model.getAllHistory().getValue()) {
            if (dateString.equals(dayRecord.getDate())) {
                binding.dayRecordContainer.setVisibility(View.VISIBLE);

                // Set date field
                binding.historyDay.setText(dateString);

                // Set num_sessions field
                String numSessions = String.valueOf(dayRecord.getNumSessions());
                binding.historyValueNumSessions.setText(numSessions);

                // Set mood field
                Mood mood = dayRecord.getMood();
                if (mood != null) {
                    binding.historyValueMood.setVisibility(View.GONE);
                    binding.historyImageEmoji.setVisibility(View.VISIBLE);
                    binding.historyImageEmoji.setImageResource(mood.getEmojiResId());

                } else {
                    binding.historyValueMood.setVisibility(View.VISIBLE);
                    binding.historyImageEmoji.setVisibility(View.GONE);
                }
                return;
            }
        }

        binding.dayRecordContainer.setVisibility(View.GONE);
    }


    private void initModel() {
        model = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);

        final Observer<List<DayRecord>> historyObserver = new Observer<List<DayRecord>>() {
            @Override
            public void onChanged(@Nullable final List<DayRecord> history) {
                updateBottomUI(calendarView.getFirstSelectedDate());
            }
        };
        model.getAllHistory().observe(getViewLifecycleOwner(), historyObserver);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onDoneBtnPressed(FormBottomDialog dialog) {
        if (dialog instanceof SelectMoodDialog) {
            // Get the date
            String date = ((SelectMoodDialog) dialog).getDayRecord().getDate();

            // Get the selected mood
            String chipName = ((SelectMoodDialog) dialog).getValue();
            Mood associatedMood = MoodManager.getMoodByName(chipName);

            // Update the model
            model.updateMood(date, associatedMood);

            Toast.makeText(getContext(), "Check-in updated.", Toast.LENGTH_SHORT).show();
        }
    }
}