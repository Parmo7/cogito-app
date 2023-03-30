package uk.ac.aston.cogito.ui.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentOnBoarding1Binding;

public class OnBoardingFragment1 extends Fragment {

    private FragmentOnBoarding1Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOnBoarding1Binding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nextBtn.setOnClickListener( v-> {
            NavHostFragment.findNavController(OnBoardingFragment1.this)
                    .navigate(R.id.action_onBoardingFragment1_to_onBoardingFragment2);
        });
    }
}