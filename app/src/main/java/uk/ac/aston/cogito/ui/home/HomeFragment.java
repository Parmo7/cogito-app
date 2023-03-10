package uk.ac.aston.cogito.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentHomeBinding;
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

public class HomeFragment extends Fragment implements BottomDialogListener {

    private FragmentHomeBinding binding;
    private SessionConfig currentConfig;
    private ConfigsViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeModel();

        initializeAllDialogSelectors();
        binding.homeStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentConfig.getNumIntermediateBells() > currentConfig.getDuration() - 1) {
                    showValidationFailedDialog(v);
                } else {
                    model.setLatestConfig(currentConfig);

                    HomeFragmentDirections.ActionNavigationHomeToSession action =
                            HomeFragmentDirections.actionNavigationHomeToSession(currentConfig);
                    Navigation.findNavController(view)
                            .navigate(action);
                }
            }
        });
    }

    private void initializeAllDialogSelectors() {
        SelectDurationDialog selectDurationDialog = new SelectDurationDialog(this, getContext());
        SelectMusicDialog selectMusicDialog = new SelectMusicDialog(this, getContext());
        EnterNameDialog enterNameDialog = new EnterNameDialog(this, getContext());
        SelectBellDialog selectStartBellDialog = new SelectStartBellDialog(this, getContext());
        SelectBellDialog selectEndBellDialog = new SelectEndBellDialog(this, getContext());
        SelectNumIntBellsDialog selectNumIntBellsDialog = new SelectNumIntBellsDialog(this, getContext());
        SelectBellDialog selectIntermediateBellDialog = new SelectIntermediateBellDialog(this, getContext());


        binding.homeValueDuration.setText(currentConfig.getDuration() + " min" );
        binding.homeValueMusic.setText(currentConfig.getBgMusic().getName());
        binding.homeValueStartBell.setText(currentConfig.getStartBellSound().getName());
        binding.homeValueEndBell.setText(currentConfig.getEndBellSound().getName());
        binding.homeValueNumIntermediateBells.setText(String.valueOf(currentConfig.getNumIntermediateBells()));
        binding.homeValueIntermediateBell.setText(currentConfig.getIntermediateBellSound().getName());

        binding.homeSelectorDuration.setOnClickListener(v -> selectDurationDialog.show(currentConfig));
        binding.homeSelectorMusic.setOnClickListener(v -> selectMusicDialog.show(currentConfig));
        binding.homeSelectorStartBell.setOnClickListener(v -> selectStartBellDialog.show(currentConfig));
        binding.homeSelectorEndBell.setOnClickListener(v -> selectEndBellDialog.show(currentConfig));
        binding.homeSelectorNumIntermediateBells.setOnClickListener(v -> selectNumIntBellsDialog.show(currentConfig));
        binding.homeSelectorIntermediateBell.setOnClickListener(v -> selectIntermediateBellDialog.show(currentConfig));

        binding.homeSaveBtn.setOnClickListener(v -> {
            if (currentConfig.getNumIntermediateBells() > currentConfig.getDuration() - 1) {
                showValidationFailedDialog(v);
            } else {
                enterNameDialog.show(currentConfig);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onDoneBtnPressed(FormBottomDialog dialog) {
        if (dialog instanceof SelectDurationDialog) {
            Integer duration = ((SelectDurationDialog) dialog).getValue();
            resetConfig();
            currentConfig.setDuration(duration);
            binding.homeValueDuration.setText(duration.toString() + " min");
            setSaveBtnVisibility(true);
            model.setLatestConfig(currentConfig);

        } else if (dialog instanceof SelectMusicDialog) {
            AudioResource bgMusic = ((SelectMusicDialog) dialog).getValue();
            resetConfig();
            currentConfig.setBgMusic(bgMusic);
            binding.homeValueMusic.setText(bgMusic.getName());
            setSaveBtnVisibility(true);
            model.setLatestConfig(currentConfig);

        } else if (dialog instanceof EnterNameDialog) {
            String name = ((EnterNameDialog) dialog).getValue();
            if (name != null && !name.isEmpty()) {
                currentConfig.setName(name);
                model.setLatestConfig(currentConfig);
                model.addConfig(currentConfig);
                setSaveBtnVisibility(false);
            }

        } else if (dialog instanceof SelectStartBellDialog) {
            AudioResource startBell = ((SelectStartBellDialog) dialog).getValue();
            resetConfig();
            currentConfig.setStartBellSound(startBell);
            binding.homeValueStartBell.setText(startBell.getName());
            setSaveBtnVisibility(true);
            model.setLatestConfig(currentConfig);

        } else if (dialog instanceof SelectEndBellDialog) {
            AudioResource endBell = ((SelectEndBellDialog) dialog).getValue();
            resetConfig();
            currentConfig.setEndBellSound(endBell);
            binding.homeValueEndBell.setText(endBell.getName());
            setSaveBtnVisibility(true);
            model.setLatestConfig(currentConfig);

        } else if (dialog instanceof  SelectNumIntBellsDialog) {
            Integer numIntermediateBells = ((SelectNumIntBellsDialog) dialog).getValue();
            resetConfig();
            currentConfig.setNumIntermediateBells(numIntermediateBells);
            binding.homeValueNumIntermediateBells.setText(numIntermediateBells.toString());

            updateIntermediateBellSound();

            setSaveBtnVisibility(true);
            model.setLatestConfig(currentConfig);

        } else if (dialog instanceof SelectIntermediateBellDialog) {
            AudioResource intermediateBell = ((SelectIntermediateBellDialog) dialog).getValue();
            resetConfig();
            currentConfig.setIntermediateBellSound(intermediateBell);
            binding.homeValueIntermediateBell.setText(intermediateBell.getName());
            setSaveBtnVisibility(true);
            model.setLatestConfig(currentConfig);
        }
    }

    private void resetConfig() {
        SessionConfig tmpConfig = currentConfig;

        currentConfig = new SessionConfig();
        currentConfig.setDuration(tmpConfig.getDuration());
        currentConfig.setBgMusic(tmpConfig.getBgMusic());
        currentConfig.setStartBellSound(tmpConfig.getStartBellSound());
        currentConfig.setEndBellSound(tmpConfig.getEndBellSound());
        currentConfig.setNumIntermediateBells(tmpConfig.getNumIntermediateBells());
        currentConfig.setIntermediateBellSound(tmpConfig.getIntermediateBellSound());
    }

    private void setSaveBtnVisibility(boolean isVisible) {
        int visibilityId = isVisible? View.VISIBLE : View.GONE;
        binding.homeSaveBtn.setVisibility(visibilityId);
    }

    public SessionConfig getCurrentConfig() {
        return currentConfig;
    }

    private void initializeModel() {
        model = new ViewModelProvider(requireActivity()).get(ConfigsViewModel.class);
        currentConfig = model.getLatestConfig().getValue();

        if (currentConfig == null) {
            currentConfig = new SessionConfig();
            binding.homeSaveBtn.setVisibility(View.VISIBLE);

        } else {
            boolean isSaveBtnVisible = currentConfig.getName().isEmpty();
            setSaveBtnVisibility(isSaveBtnVisible);
        }

        final Observer<SessionConfig> latestConfigObserver = new Observer<SessionConfig>() {
            @Override
            public void onChanged(@Nullable final SessionConfig config) {
                if (config != null) {
                    currentConfig = config;

                    binding.homeValueDuration.setText(currentConfig.getDuration() + " min" );
                    binding.homeValueMusic.setText(currentConfig.getBgMusic().getName());
                    binding.homeValueStartBell.setText(currentConfig.getStartBellSound().getName());
                    binding.homeValueEndBell.setText(currentConfig.getEndBellSound().getName());
                    binding.homeValueNumIntermediateBells.setText(String.valueOf(currentConfig.getNumIntermediateBells()));
                    binding.homeValueIntermediateBell.setText(currentConfig.getIntermediateBellSound().getName());

                    updateIntermediateBellSound();
                }
            }
        };
        model.getLatestConfig().observe(getViewLifecycleOwner(), latestConfigObserver);
    }

    private void log(String msg) {
        Log.i("Parmi", msg);
    }

    private void updateIntermediateBellSound() {
        // Disable field 'Intermediate Bell Sound' if needed
        if (currentConfig.getNumIntermediateBells() == 0) {
            binding.homeSelectorIntermediateBell.setClickable(false);
            currentConfig.setIntermediateBellSound(BellSoundManager.BELL_NULL);
            binding.homeValueIntermediateBell.setText(BellSoundManager.BELL_NULL.getName());
            binding.homeValueIntermediateBell.setTextColor(getResources().getColor(R.color.grey_2, null));
            binding.homeArrowIntermediateBell.setColorFilter(getResources().getColor(R.color.grey_2, null));

        } else {
            binding.homeSelectorIntermediateBell.setClickable(true);
            binding.homeValueIntermediateBell.setTextColor(getResources().getColor(R.color.blue_2, null));
            binding.homeArrowIntermediateBell.setColorFilter(getResources().getColor(R.color.blue_2, null));
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
}