package uk.ac.aston.cogito.ui.onboarding;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentOnBoarding2Binding;

public class OnBoardingFragment2 extends Fragment {

    private FragmentOnBoarding2Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOnBoarding2Binding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.prevBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(OnBoardingFragment2.this)
                    .navigate(R.id.action_onBoardingFragment2_to_onBoardingFragment1);
        });

        binding.nextBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(OnBoardingFragment2.this)
                    .navigate(R.id.action_onBoardingFragment2_to_onBoardingFragment3);
        });
    }
}