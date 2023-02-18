package uk.ac.aston.cogito.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import uk.ac.aston.cogito.databinding.FragmentHomeBinding;
import uk.ac.aston.cogito.model.entities.AudioResource;
import uk.ac.aston.cogito.model.entities.SessionConfig;
import uk.ac.aston.cogito.ui.home.dialogs.BottomDialogListener;
import uk.ac.aston.cogito.ui.home.dialogs.FormBottomDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectDurationDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectMusicDialog;

public class HomeFragment extends Fragment implements BottomDialogListener {

    private FragmentHomeBinding binding;
    private SessionConfig currentConfig;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        currentConfig = new SessionConfig();
        currentConfig.setDuration(SessionConfig.DEFAULT_DURATION);
        currentConfig.setBgMusic(SessionConfig.DEFAULT_BG_MUSIC);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeAllDialogSelectors();
        binding.homePrimaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        binding.homeValueDuration.setText(String.valueOf(currentConfig.getDuration()) + " min" );
        binding.homeValueMusic.setText(currentConfig.getBgMusic().getName());

        binding.homeSelectorDuration.setOnClickListener(v -> selectDurationDialog.show());
        binding.homeSelectorMusic.setOnClickListener(v -> selectMusicDialog.show());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onDoneBtnPressed(FormBottomDialog dialog) {

        if (dialog instanceof SelectDurationDialog) {
            Integer value = ((SelectDurationDialog) dialog).getValue();
            currentConfig.setDuration(value);
            binding.homeValueDuration.setText(value.toString() + " min");

        } else if (dialog instanceof SelectMusicDialog) {
            AudioResource value = ((SelectMusicDialog) dialog).getValue();
            getCurrentConfig().setBgMusic(value);
            binding.homeValueMusic.setText(value.getName());

        }
    }

    public SessionConfig getCurrentConfig() {
        return currentConfig;
    }

    public void setCurrentConfig(SessionConfig currentConfig) {
        this.currentConfig = currentConfig;
    }
}