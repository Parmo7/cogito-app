package uk.ac.aston.cogito.ui.session;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentDoneBinding;

public class DoneFragment extends Fragment {

    private FragmentDoneBinding binding;
    private BottomNavigationView navBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDoneBinding.inflate(inflater, container, false);
        navBar = getActivity().findViewById(R.id.nav_view);
        navBar.setVisibility(View.GONE);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.doneOkBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(DoneFragment.this)
                    .navigate(R.id.action_doneFragment_to_navigation_home);
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        navBar.setVisibility(View.VISIBLE);
        binding = null;
    }
}