package uk.ac.aston.cogito.ui.session;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentCheckInBinding;


public class CheckInFragment extends Fragment {

    // High-level variables
    private BottomNavigationView navBar;
    private FragmentCheckInBinding binding;

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

    }

    @Override
    public void onStop() {
        super.onStop();
        navBar.setVisibility(View.GONE);

        binding = null;
    }
}