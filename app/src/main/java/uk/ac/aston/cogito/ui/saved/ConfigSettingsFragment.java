package uk.ac.aston.cogito.ui.saved;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentConfigSettingsBinding;
import uk.ac.aston.cogito.databinding.FragmentSessionBinding;
import uk.ac.aston.cogito.model.BellSoundManager;
import uk.ac.aston.cogito.model.ConfigsViewModel;
import uk.ac.aston.cogito.model.entities.AudioResource;
import uk.ac.aston.cogito.model.entities.SessionConfig;
import uk.ac.aston.cogito.ui.home.dialogs.BottomDialogListener;
import uk.ac.aston.cogito.ui.home.dialogs.EnterNameDialog;
import uk.ac.aston.cogito.ui.home.dialogs.FormBottomDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectBellDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectDurationDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectEndBellDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectIntermediateBellDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectMusicDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectNumIntBellsDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectStartBellDialog;
import uk.ac.aston.cogito.ui.session.SessionFragmentArgs;

public class ConfigSettingsFragment extends Fragment implements BottomDialogListener {

    private FragmentConfigSettingsBinding binding;
    private SessionConfig config;
    private ConfigsViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentConfigSettingsBinding.inflate(inflater, container, false);
        config = cloneSessionConfig();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ConfigsViewModel.class);

        if (config == null) {
            config = new SessionConfig();

            // Set the title and subtitle
            binding.title.setText(R.string.configsettings_title_add);
            binding.subtitle.setText(R.string.configsettings_subtitle_add);

        } else {
            // Set the title and subtitle
            binding.title.setText(getResources().getString(R.string.configsettings_title_edit) + " '" + config.getName() + "'");
            binding.subtitle.setText(R.string.configsettings_subtitle_edit);
        }

        initialiseForm();


        binding.configsettingsCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack(v);
            }
        });


        binding.configsettingsSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (config.getNumIntermediateBells() > config.getDuration() - 1) {
                    showValidationFailedDialog(v);

                } else {
                    // If this is a new configuration
                    if (config.getName().isEmpty()) {
                        // show the enter-name dialog
                        EnterNameDialog enterNameDialog = new EnterNameDialog(ConfigSettingsFragment.this, getContext());
                        enterNameDialog.show(config);

                    } else {
                        model.updateConfig(config);
                        navigateBack(v);
                    }
                }
            }
        });
    }

    private void initialiseForm() {
        SelectDurationDialog selectDurationDialog = new SelectDurationDialog(this, getContext());
        SelectMusicDialog selectMusicDialog = new SelectMusicDialog(this, getContext());
        SelectBellDialog selectStartBellDialog = new SelectStartBellDialog(this, getContext());
        SelectBellDialog selectEndBellDialog = new SelectEndBellDialog(this, getContext());
        SelectNumIntBellsDialog selectNumIntBellsDialog = new SelectNumIntBellsDialog(this, getContext());
        SelectBellDialog selectIntermediateBellDialog = new SelectIntermediateBellDialog(this, getContext());

        binding.configsettingsValueDuration.setText(config.getDuration() + " min" );
        binding.configsettingsValueMusic.setText(config.getBgMusic().getName());
        binding.configsettingsValueStartBell.setText(config.getStartBellSound().getName());
        binding.configsettingsValueEndBell.setText(config.getEndBellSound().getName());
        binding.configsettingsValueNumIntermediateBells.setText(String.valueOf(config.getNumIntermediateBells()));
        binding.configsettingsValueIntermediateBell.setText(config.getIntermediateBellSound().getName());
        
        binding.configsettingsSelectorDuration.setOnClickListener(v -> selectDurationDialog.show(config));
        binding.configsettingsSelectorMusic.setOnClickListener(v -> selectMusicDialog.show(config));
        binding.configsettingsSelectorStartBell.setOnClickListener(v -> selectStartBellDialog.show(config));
        binding.configsettingsSelectorEndBell.setOnClickListener(v -> selectEndBellDialog.show(config));
        binding.configsettingsSelectorNumIntermediateBells.setOnClickListener(v -> selectNumIntBellsDialog.show(config));
        binding.configsettingsSelectorIntermediateBell.setOnClickListener(v -> selectIntermediateBellDialog.show(config));
        
        updateIntermediateBellSound();
    }

    private void navigateBack(View view) {
        NavDirections action = ConfigSettingsFragmentDirections.actionConfigSettingsFragmentToNavigationSaved();
        Navigation.findNavController(view).navigate(action);
    }


    @Override
    public void onDoneBtnPressed(FormBottomDialog dialog) {
        if (dialog instanceof SelectDurationDialog) {
            Integer duration = ((SelectDurationDialog) dialog).getValue();
            config.setDuration(duration);
            binding.configsettingsValueDuration.setText(duration.toString() + " min");

        } else if (dialog instanceof SelectMusicDialog) {
            AudioResource bgMusic = ((SelectMusicDialog) dialog).getValue();
            config.setBgMusic(bgMusic);
            binding.configsettingsValueMusic.setText(bgMusic.getName());

        } else if (dialog instanceof EnterNameDialog) {
            String name = ((EnterNameDialog) dialog).getValue();
            if (name != null && !name.isEmpty()) {
                config.setName(name);
                model.addConfig(config);
                navigateBack(getView());
            }

        } else if (dialog instanceof SelectStartBellDialog) {
            AudioResource startBell = ((SelectStartBellDialog) dialog).getValue();
            config.setStartBellSound(startBell);
            binding.configsettingsValueStartBell.setText(startBell.getName());

        } else if (dialog instanceof SelectEndBellDialog) {
            AudioResource endBell = ((SelectEndBellDialog) dialog).getValue();
            config.setEndBellSound(endBell);
            binding.configsettingsValueEndBell.setText(endBell.getName());

        } else if (dialog instanceof SelectNumIntBellsDialog) {
            Integer numIntermediateBells = ((SelectNumIntBellsDialog) dialog).getValue();
            config.setNumIntermediateBells(numIntermediateBells);
            binding.configsettingsValueNumIntermediateBells.setText(numIntermediateBells.toString());
            updateIntermediateBellSound();

        } else if (dialog instanceof SelectIntermediateBellDialog) {
            AudioResource intermediateBell = ((SelectIntermediateBellDialog) dialog).getValue();
            config.setIntermediateBellSound(intermediateBell);
            binding.configsettingsValueIntermediateBell.setText(intermediateBell.getName());

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void updateIntermediateBellSound() {
        // Disable field 'Intermediate Bell Sound' if needed
        if (config.getNumIntermediateBells() == 0) {
            binding.configsettingsSelectorIntermediateBell.setClickable(false);
            config.setIntermediateBellSound(BellSoundManager.BELL_NULL);
            binding.configsettingsValueIntermediateBell.setText(BellSoundManager.BELL_NULL.getName());
            binding.configsettingsValueIntermediateBell.setTextColor(getResources().getColor(R.color.grey_2, null));
            binding.configsettingsArrowIntermediateBell.setColorFilter(getResources().getColor(R.color.grey_2, null));

        } else {
            binding.configsettingsSelectorIntermediateBell.setClickable(true);
            binding.configsettingsValueIntermediateBell.setTextColor(getResources().getColor(R.color.blue_2, null));
            binding.configsettingsArrowIntermediateBell.setColorFilter(getResources().getColor(R.color.blue_2, null));
        }
    }

    private void showValidationFailedDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.validation_failed_title);
        builder.setMessage(R.string.validation_failed_message);
        builder.setPositiveButton(R.string.validation_failed_fix, (dialog, id) -> dialog.cancel());

        // Build the AlertDialog
        AlertDialog validationFailedDialog = builder.create();
        validationFailedDialog.show();
    }

    private SessionConfig cloneSessionConfig() {
        SessionConfig original = ConfigSettingsFragmentArgs.fromBundle(getArguments()).getConfig();

        if (original != null) {
            SessionConfig clone = new SessionConfig();

            clone.setId(original.getId());
            clone.setName(original.getName());

            clone.setDuration(original.getDuration());
            clone.setBgMusic(original.getBgMusic());

            clone.setStartBellSound(original.getStartBellSound());
            clone.setEndBellSound(original.getEndBellSound());
            clone.setNumIntermediateBells(original.getNumIntermediateBells());
            clone.setIntermediateBellSound(original.getIntermediateBellSound());

            return clone;
        }

        return null;
    }
}