package uk.ac.aston.cogito.ui.saved;

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
import uk.ac.aston.cogito.model.ConfigsViewModel;
import uk.ac.aston.cogito.model.entities.AudioResource;
import uk.ac.aston.cogito.model.entities.SessionConfig;
import uk.ac.aston.cogito.ui.home.dialogs.BottomDialogListener;
import uk.ac.aston.cogito.ui.home.dialogs.EnterNameDialog;
import uk.ac.aston.cogito.ui.home.dialogs.FormBottomDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectDurationDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectMusicDialog;
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
        config = ConfigSettingsFragmentArgs.fromBundle(getArguments()).getConfig();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ConfigsViewModel.class);

        if (config == null) {
            config = new SessionConfig();
            config.setDuration(SessionConfig.DEFAULT_DURATION);
            config.setBgMusic(SessionConfig.DEFAULT_BG_MUSIC);

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
        });
    }

    private void initialiseForm() {
        SelectDurationDialog selectDurationDialog = new SelectDurationDialog(this, getContext());
        SelectMusicDialog selectMusicDialog = new SelectMusicDialog(this, getContext());
        EnterNameDialog enterNameDialog = new EnterNameDialog(this, getContext());


        binding.configsettingsValueDuration.setText(String.valueOf(config.getDuration()) + " min" );
        binding.configsettingsValueMusic.setText(config.getBgMusic().getName());

        binding.configsettingsSelectorDuration.setOnClickListener(v -> selectDurationDialog.show(config));
        binding.configsettingsSelectorMusic.setOnClickListener(v -> selectMusicDialog.show(config));
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
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}