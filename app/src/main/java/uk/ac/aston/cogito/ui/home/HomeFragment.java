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
import uk.ac.aston.cogito.ui.home.dialogs.SelectDurationDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectMusicDialog;

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

        if (currentConfig == null) {
            currentConfig = new SessionConfig();
            currentConfig.setDuration(SessionConfig.DEFAULT_DURATION);
            currentConfig.setBgMusic(SessionConfig.DEFAULT_BG_MUSIC);
            binding.homeSaveBtn.setVisibility(View.VISIBLE);

        } else {
            boolean isSaveBtnVisible = currentConfig.getName().isEmpty();
            setSaveBtnVisibility(isSaveBtnVisible);
        }


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


        binding.homeValueDuration.setText(String.valueOf(currentConfig.getDuration()) + " min" );
        binding.homeValueMusic.setText(currentConfig.getBgMusic().getName());

        binding.homeSelectorDuration.setOnClickListener(v -> selectDurationDialog.show());
        binding.homeSelectorMusic.setOnClickListener(v -> selectMusicDialog.show());
        binding.homeStartBtn.setOnClickListener(v -> enterNameDialog.show());
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
            currentConfig.setDuration(duration);
            binding.homeValueDuration.setText(duration.toString() + " min");
            setSaveBtnVisibility(true);
            model.setLatestConfig(currentConfig);

        } else if (dialog instanceof SelectMusicDialog) {
            AudioResource bgMusic = ((SelectMusicDialog) dialog).getValue();
            currentConfig.setBgMusic(bgMusic);
            binding.homeValueMusic.setText(bgMusic.getName());
            setSaveBtnVisibility(true);
            model.setLatestConfig(currentConfig);

        } else if (dialog instanceof EnterNameDialog) {
            String name = ((EnterNameDialog) dialog).getValue();
            if (name != null && !name.isEmpty()) {
                currentConfig.setName(name);
                model.addConfig(currentConfig);
                setSaveBtnVisibility(false);
            }
        }
    }

    private void setSaveBtnVisibility(boolean isVisible) {
        int visibilityId = isVisible? View.VISIBLE : View.GONE;
        binding.homeStartBtn.setVisibility(visibilityId);
    }

    public SessionConfig getCurrentConfig() {
        return currentConfig;
    }

    private void initializeModel() {
        model = new ViewModelProvider(requireActivity()).get(ConfigsViewModel.class);

        // Create the observer for the list of places, which will update the UI.
        final Observer<SessionConfig> latestConfigObserver = new Observer<SessionConfig>() {
            @Override
            public void onChanged(@Nullable final SessionConfig config) {
                if (config != null) {
                    currentConfig = config;

                    binding.homeValueDuration.setText(String.valueOf(currentConfig.getDuration()) + " min" );
                    binding.homeValueMusic.setText(currentConfig.getBgMusic().getName());
                }
            }
        };
        model.getLatestConfig().observe(getViewLifecycleOwner(), latestConfigObserver);
    }

    private void log(String msg) {
        Log.i("Parmi", msg);
    }
}