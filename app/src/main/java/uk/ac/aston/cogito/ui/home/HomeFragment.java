package uk.ac.aston.cogito.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentHomeBinding;
import uk.ac.aston.cogito.ui.home.dialogs.BottomDialogListener;
import uk.ac.aston.cogito.ui.home.dialogs.FormBottomDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectDurationDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectMusicDialog;

public class HomeFragment extends Fragment implements BottomDialogListener {

    public static final int _DEFAULT_DURATION = 10;

    private FragmentHomeBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeAllDialogSelectors();
    }

    private void initializeAllDialogSelectors() {
        SelectDurationDialog selectDurationDialog = new SelectDurationDialog(this, getContext());
        SelectMusicDialog selectMusicDialog = new SelectMusicDialog(this, getContext());


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
            binding.homeValueDuration.setText(value.toString() + " min");
        } else if (dialog instanceof SelectMusicDialog) {
            String value = ((SelectMusicDialog) dialog).getValue();
            binding.homeValueMusic.setText(value);
        }
    }
}