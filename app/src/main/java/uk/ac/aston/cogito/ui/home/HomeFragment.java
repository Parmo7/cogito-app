package uk.ac.aston.cogito.ui.home;

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

import uk.ac.aston.cogito.databinding.FragmentHomeBinding;
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
                model.setLatestConfig(currentConfig);

                HomeFragmentDirections.ActionNavigationHomeToSession action =
                        HomeFragmentDirections.actionNavigationHomeToSession(currentConfig);
                Navigation.findNavController(view)
                        .navigate(action);
            }
        });
    }

    private void initializeAllDialogSelectors() {
        SelectDurationDialog selectDurationDialog = new SelectDurationDialog(this, getContext(), currentConfig);
        SelectMusicDialog selectMusicDialog = new SelectMusicDialog(this, getContext(), currentConfig);
        EnterNameDialog enterNameDialog = new EnterNameDialog(this, getContext(), currentConfig);
        SelectBellDialog selectStartBellDialog = new SelectStartBellDialog(this, getContext(), currentConfig);
        SelectBellDialog selectEndBellDialog = new SelectEndBellDialog(this, getContext(), currentConfig);
        SelectNumIntBellsDialog selectNumIntBellsDialog = new SelectNumIntBellsDialog(this, getContext(), currentConfig);
        SelectBellDialog selectIntermediateBellDialog = new SelectIntermediateBellDialog(this, getContext(), currentConfig);


        binding.homeValueDuration.setText(currentConfig.getDuration() + " min" );
        binding.homeValueMusic.setText(currentConfig.getBgMusic().getName());
        binding.homeValueStartBell.setText(currentConfig.getStartBellSound().getName());
        binding.homeValueEndBell.setText(currentConfig.getEndBellSound().getName());
        binding.homeValueNumIntermediateBells.setText(String.valueOf(currentConfig.getNumIntermediateBells()));
        binding.homeValueIntermediateBell.setText(currentConfig.getIntermediateBellSound().getName());

        binding.homeSelectorDuration.setOnClickListener(v -> selectDurationDialog.show());
        binding.homeSelectorMusic.setOnClickListener(v -> selectMusicDialog.show());
        binding.homeSelectorStartBell.setOnClickListener(v -> selectStartBellDialog.show());
        binding.homeSelectorEndBell.setOnClickListener(v -> selectEndBellDialog.show());
        binding.homeSelectorNumIntermediateBells.setOnClickListener(v -> selectNumIntBellsDialog.show());
        binding.homeSelectorIntermediateBell.setOnClickListener(v -> selectIntermediateBellDialog.show());
        binding.homeSaveBtn.setOnClickListener(v -> enterNameDialog.show());
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
                }
            }
        };
        model.getLatestConfig().observe(getViewLifecycleOwner(), latestConfigObserver);
    }

    private void log(String msg) {
        Log.i("Parmi", msg);
    }
}