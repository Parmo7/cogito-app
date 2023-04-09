package uk.ac.aston.cogito.ui.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.cogito.MainActivity;
import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentOnBoarding3Binding;

public class OnBoardingFragment3 extends Fragment {

    private FragmentOnBoarding3Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOnBoarding3Binding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.prevBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(OnBoardingFragment3.this)
                    .navigate(R.id.action_onBoardingFragment3_to_onBoardingFragment2);
        });

        binding.doneBtn.setOnClickListener(v -> {
            markOnBoardingDone();
            startActivity(new Intent(getContext(), MainActivity.class));
        });
    }

    private void markOnBoardingDone() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences(getString(R.string.prefs_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.prefs_is_first_time), false);
        editor.apply();
    }
}