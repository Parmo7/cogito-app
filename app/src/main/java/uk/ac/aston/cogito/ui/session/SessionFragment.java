package uk.ac.aston.cogito.ui.session;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentHomeBinding;
import uk.ac.aston.cogito.databinding.FragmentSessionBinding;

public class SessionFragment extends Fragment {

    private BottomNavigationView navBar;
    private FragmentSessionBinding binding;

    private enum SessionState {PLAYING, PAUSED};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSessionBinding.inflate(inflater, container, false);
        navBar = getActivity().findViewById(R.id.nav_view);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navBar.setVisibility(View.GONE);

        initializeButtons();
    }

    private void initializeButtons() {
        binding.sessionPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSessionState(SessionState.PAUSED);
            }
        });

        binding.sessionContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSessionState(SessionState.PLAYING);
            }
        });

        binding.sessionEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SessionFragment.this)
                        .navigate(R.id.action_session_to_navigation_home);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        navBar.setVisibility(View.VISIBLE);
    }

    private void setSessionState(SessionState requiredState) {

        if (requiredState == SessionState.PAUSED) {
            binding.sessionPauseBtn.setVisibility(View.INVISIBLE);
            binding.sessionRewindBtn.setVisibility(View.INVISIBLE);
            binding.sessionForwardBtn.setVisibility(View.INVISIBLE);

            binding.sessionContinueBtn.setVisibility(View.VISIBLE);
            binding.sessionEndBtn.setVisibility(View.VISIBLE);

        } else {
            binding.sessionPauseBtn.setVisibility(View.VISIBLE);
            binding.sessionRewindBtn.setVisibility(View.VISIBLE);
            binding.sessionForwardBtn.setVisibility(View.VISIBLE);

            binding.sessionContinueBtn.setVisibility(View.INVISIBLE);
            binding.sessionEndBtn.setVisibility(View.INVISIBLE);
        }
    }
}