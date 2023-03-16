package uk.ac.aston.cogito.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.aston.cogito.databinding.FragmentSettingsBinding;
import uk.ac.aston.cogito.model.SettingsViewModel;
import uk.ac.aston.cogito.ui.dialogs.BottomDialogListener;
import uk.ac.aston.cogito.ui.dialogs.FormBottomDialog;

public class SettingsFragment extends Fragment implements BottomDialogListener {

    private FragmentSettingsBinding binding;
    private SettingsViewModel model;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(this).get(SettingsViewModel.class);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }


    @Override
    public void onDoneBtnPressed(FormBottomDialog dialog) {

    }
}