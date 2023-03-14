package uk.ac.aston.cogito.ui.session;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentCheckInBinding;
import uk.ac.aston.cogito.model.HistoryViewModel;
import uk.ac.aston.cogito.model.MoodManager;
import uk.ac.aston.cogito.model.entities.Mood;


public class CheckInFragment extends Fragment {

    // High-level variables
    private BottomNavigationView navBar;
    private FragmentCheckInBinding binding;
    private HistoryViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCheckInBinding.inflate(inflater, container, false);
        navBar = getActivity().findViewById(R.id.nav_view);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navBar.setVisibility(View.GONE);
        model = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);

        binding.checkinSubmitBtn.setOnClickListener(v -> {
            int id = binding.chipGroup.getCheckedChipId();

            // If no chip has been selected
            if (id == View.NO_ID) {
                Toast.makeText(getContext(), "No selection made.", Toast.LENGTH_SHORT).show();

            // Else, if a chip is currently selected
            } else {
                Chip selectedChip = binding.chipGroup.findViewById(id);
                if (selectedChip != null) {
                    String chipName = selectedChip.getText().toString();
                    Mood associatedMood = MoodManager.getMoodByName(chipName);
                    model.recordTodayMood(associatedMood);

                    Toast.makeText(getContext(), "Daily check-in completed.", Toast.LENGTH_SHORT).show();
                }

                NavHostFragment.findNavController(CheckInFragment.this)
                        .navigate(R.id.action_checkInFragment_to_navigation_home);
            }
        });

        binding.checkinSkipBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(CheckInFragment.this)
                    .navigate(R.id.action_checkInFragment_to_navigation_home);
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        navBar.setVisibility(View.VISIBLE);

        binding = null;
    }
}