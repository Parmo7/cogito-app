package uk.ac.aston.cogito.ui.settings;

import static android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM;
import static uk.ac.aston.cogito.model.SettingsViewModel.DEFAULT_MAX_REMINDERS;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.aston.cogito.MainActivity;
import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentSettingsBinding;
import uk.ac.aston.cogito.model.SettingsViewModel;
import uk.ac.aston.cogito.model.TimeManager;
import uk.ac.aston.cogito.model.entities.Settings;
import uk.ac.aston.cogito.notifications.NotificationManager;
import uk.ac.aston.cogito.notifications.NotificationReceiver;
import uk.ac.aston.cogito.ui.dialogs.BottomDialogListener;
import uk.ac.aston.cogito.ui.dialogs.FormBottomDialog;
import uk.ac.aston.cogito.ui.dialogs.PickReminderTimeDialog;
import uk.ac.aston.cogito.ui.dialogs.SelectFormalRemindersDialog;
import uk.ac.aston.cogito.ui.dialogs.SelectInformalRemindersDialog;

public class SettingsFragment extends Fragment implements BottomDialogListener {

    private FragmentSettingsBinding binding;
    private SettingsViewModel model;
    private Settings settings;

    private FormField fieldNumFormal;
    private FormField fieldNumInformal;
    private FormField[] fieldsFormalTime = new FormField[DEFAULT_MAX_REMINDERS];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFormFields();
        initModel();
        initSelectors();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }


    private void initFormFields() {
        fieldNumFormal = new FormField(
                binding.settingsSelectorNumFormal, binding.settingsValueNumFormal,
                binding.settingsArrowNumFormal, binding.dividerNumFormal
        );
        
        fieldNumInformal = new FormField(
                binding.settingsSelectorNumInformal, binding.settingsValueNumInformal,
                binding.settingsArrowNumInformal, binding.dividerNumInformal
        );
        
        fieldsFormalTime[0] = new FormField(
                binding.settingsSelectorFormal1, binding.settingsValueFormal1,
                binding.settingsArrowFormal1, binding.dividerReminder1
        );

        fieldsFormalTime[1] = new FormField(
                binding.settingsSelectorFormal2, binding.settingsValueFormal2,
                binding.settingsArrowFormal2, binding.dividerReminder2
        );
        
        fieldsFormalTime[2] = new FormField(
                binding.settingsSelectorFormal3, binding.settingsValueFormal3,
                binding.settingsArrowFormal3, binding.dividerReminder3
        );

        fieldsFormalTime[3] = new FormField(
                binding.settingsSelectorFormal4, binding.settingsValueFormal4,
                binding.settingsArrowFormal4, binding.dividerReminder4
        );

        fieldsFormalTime[4] = new FormField(
                binding.settingsSelectorFormal5, binding.settingsValueFormal5,
                binding.settingsArrowFormal5, binding.dividerReminder5
        );
    }
    
    
    private void initModel() {
        model = new ViewModelProvider(this).get(SettingsViewModel.class);
        settings = model.getSettings().getValue();

        if (settings == null) {
            settings = new Settings();
            model.setSettings(settings);
        }

        // Set-up the observer
        final Observer<Settings> settingsObserver = new Observer<Settings>() {
            @Override
            public void onChanged(Settings sets) {
                if (sets != null) {
                    settings = sets;

                    // Update fields for formal practice reminders
                    binding.settingsSwitchEnableFormal.setChecked(settings.isFormalEnabled());
                    binding.settingsValueNumFormal.setText(String.valueOf(settings.getNumFormal()));

                    String[] formalTimes12 = new String[DEFAULT_MAX_REMINDERS];
                    for (int i = 0; i < formalTimes12.length; i++) {
                        String time24 = String.valueOf(settings.getFormalTimes()[i]);
                        String time12 = TimeManager.date24to12(time24);
                        formalTimes12[i] = time12;
                    }

                    binding.settingsValueFormal1.setText(formalTimes12[0]);
                    binding.settingsValueFormal2.setText(formalTimes12[1]);
                    binding.settingsValueFormal3.setText(formalTimes12[2]);
                    binding.settingsValueFormal4.setText(formalTimes12[3]);
                    binding.settingsValueFormal5.setText(formalTimes12[4]);


                    // Update fields for informal practice reminders
                    binding.settingsSwitchEnableInformal.setChecked(settings.isInformalEnabled());
                    binding.settingsValueNumInformal.setText(String.valueOf(settings.getNumInformal()));

                    hideOrDisableFields();

                    // Schedule notifications, if required
                    NotificationManager.schedule(getContext(), settings);
                }
            }
        };
        model.getSettings().observe(getViewLifecycleOwner(), settingsObserver);
    }

    private void hideOrDisableFields() {
        // Check if formal practice notifications are on
        if (settings.isFormalEnabled()) {
            fieldNumFormal.enable();
            for (FormField field : fieldsFormalTime) {
                field.enable();
            }
        } else {
            fieldNumFormal.disable();
            for (FormField field : fieldsFormalTime) {
                field.disable();
            }
        }

        // Check if informal practice notifications are on
        if (settings.isInformalEnabled()) {
            fieldNumInformal.enable();
        } else {
            fieldNumInformal.disable();
        }

        // Check how many formal reminder fields we should show
        int numFormal = settings.getNumFormal();
        for (int i = 0; i < fieldsFormalTime.length; i++) {
            // for each field, decide whether to show/hide it
            if ( i < numFormal) {
                fieldsFormalTime[i].show();
            } else {
                fieldsFormalTime[i].hide();
            }
        }
    }


    private void initSelectors() {
        SelectFormalRemindersDialog selectFormalRemindersDialog = new SelectFormalRemindersDialog(this, getContext());
        SelectInformalRemindersDialog selectInformalRemindersDialog = new SelectInformalRemindersDialog(this, getContext());
        PickReminderTimeDialog[] pickReminderTimeDialogs = new PickReminderTimeDialog[DEFAULT_MAX_REMINDERS];
        for (int i = 0; i < pickReminderTimeDialogs.length; i++) {
            pickReminderTimeDialogs[i] = new PickReminderTimeDialog(this, getContext(), i);
        }

        fieldNumFormal.selector.setOnClickListener(v -> selectFormalRemindersDialog.show(settings.getNumFormal()));
        fieldNumInformal.selector.setOnClickListener(v -> selectInformalRemindersDialog.show(settings.getNumInformal()));
        for (int i = 0; i < fieldsFormalTime.length; i++) {
            FormField timeField = fieldsFormalTime[i];
            int finalI = i;
            timeField.selector.setOnClickListener(
                    v -> pickReminderTimeDialogs[finalI].show(
                            settings.getFormalTimes()[finalI]
                    )
            );
        }

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        binding.settingsSwitchEnableFormal.setOnCheckedChangeListener((buttonView, isChecked) -> {

            // Check if we have permission to show notifications
            if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                    && !alarmManager.canScheduleExactAlarms()) {

                binding.settingsSwitchEnableFormal.setChecked(false);
                startActivity(new Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM));

            } else {
                settings.setFormalEnabled(isChecked);
                model.setSettings(settings);
            }
        });

        binding.settingsSwitchEnableInformal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settings.setInformalEnabled(isChecked);
            model.setSettings(settings);
        });
    }


    @Override
    public void onDoneBtnPressed(FormBottomDialog dialog) {
        if (dialog instanceof SelectFormalRemindersDialog) {
            int numReminders = ((SelectFormalRemindersDialog) dialog).getValue();
            settings.setNumFormal(numReminders);
            model.setSettings(settings);
            binding.settingsValueNumFormal.setText(String.valueOf(numReminders));
            
        } else if (dialog instanceof  SelectInformalRemindersDialog) {
            int numReminders = ((SelectInformalRemindersDialog) dialog).getValue();
            settings.setNumInformal(numReminders);
            model.setSettings(settings);
            binding.settingsValueNumInformal.setText(String.valueOf(numReminders));

        } else if (dialog instanceof PickReminderTimeDialog) {
            String time24 = ((PickReminderTimeDialog) dialog).getValue24();
            String time12 = TimeManager.date24to12(time24);

            String[] formalTimes = settings.getFormalTimes();
            int index = ((PickReminderTimeDialog) dialog).getIndex();
            formalTimes[index] = time24;

            settings.setFormalTimes(formalTimes);
            model.setSettings(settings);
            fieldsFormalTime[index].value.setText(time12);
        }
    }



    private class FormField {
        final LinearLayout selector;
        final TextView value;
        final ImageView arrow;
        final View lineDivider;

        public FormField(LinearLayout selector, TextView value, ImageView arrow, View lineDivider) {
            this.selector = selector;
            this.value = value;
            this.arrow = arrow;
            this.lineDivider = lineDivider;
        }

        public void hide() {
            selector.setVisibility(View.GONE);
            lineDivider.setVisibility(View.GONE);
        }

        public void show() {
            selector.setVisibility(View.VISIBLE);
            lineDivider.setVisibility(View.VISIBLE);
        }

        public void disable() {
            selector.setClickable(false);
            value.setTextColor(getResources().getColor(R.color.grey_2, null));
            arrow.setColorFilter(getResources().getColor(R.color.grey_2, null));
        }

        public void enable() {
            selector.setClickable(true);
            value.setTextColor(getResources().getColor(R.color.blue_2, null));
            arrow.setColorFilter(getResources().getColor(R.color.blue_2, null));
        }
    }
}