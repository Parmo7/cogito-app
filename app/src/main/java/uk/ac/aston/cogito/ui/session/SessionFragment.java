package uk.ac.aston.cogito.ui.session;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentSessionBinding;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SessionFragment extends Fragment {

    private static final long SEEK_INTERVAL = 10 * 1000;     // 10 seconds

    private BottomNavigationView navBar;
    private FragmentSessionBinding binding;

    private enum SessionState {PLAYING, PAUSED};

    private MediaPlayer player;
    private SessionConfig config;

    private CountDownTimer timer;
    private long millisLeft;

    private ObjectAnimator circleAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentSessionBinding.inflate(inflater, container, false);
        navBar = getActivity().findViewById(R.id.nav_view);
        config = SessionFragmentArgs.fromBundle(getArguments()).getSessionConfig();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navBar.setVisibility(View.GONE);

        player = MediaPlayer.create(getContext(), config.getBgMusic().getResId());
        player.setLooping(true);
        player.setScreenOnWhilePlaying(true);
        player.start();

        binding.totalTime.setText("/ " + String.format("%02d", config.getDuration()) + ":00");
        timer = getNewTimer(getTotalDurationMillis());
        timer.start();

        animateOuterCircle();
        initializeButtons();
    }

    private CountDownTimer getNewTimer(long timerDurationInMillis) {
        return new CountDownTimer(timerDurationInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;

                long millisElapsed = getTotalDurationMillis() - millisUntilFinished;
                @SuppressLint("DefaultLocale") String elapsedTime =
                        String.format(
                                "%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millisElapsed),
                                TimeUnit.MILLISECONDS.toSeconds(millisElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisElapsed))
                        );

                binding.elapsedTime.setText(elapsedTime);
            }

            @Override
            public void onFinish() {
                player.stop();
                circleAnimation.pause();
            }
        };
    }

    private void initializeButtons() {
        binding.sessionPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
                circleAnimation.pause();
                timer.cancel();
                setSessionState(SessionState.PAUSED);
            }
        });

        binding.sessionRewindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long seekPos = getElapsedTimeMillis() - SEEK_INTERVAL;
                if (seekPos < 0) {
                    seekPos = 0;
                }

                player.seekTo((int) seekPos);

                timer.cancel();
                timer = getNewTimer(getTotalDurationMillis() - seekPos);
                timer.start();
            }
        });

        binding.sessionForwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long seekPos = getElapsedTimeMillis() + SEEK_INTERVAL;
                if (seekPos >= getTotalDurationMillis()) {
                    seekPos = getTotalDurationMillis() - 1000;
                }

                player.seekTo((int) seekPos);

                timer.cancel();
                timer = getNewTimer(getTotalDurationMillis() - seekPos);
                timer.start();
            }
        });

        binding.sessionContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();

                timer = getNewTimer(millisLeft);
                timer.start();

                circleAnimation.resume();
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

        player.stop();
        player.release();
        circleAnimation.cancel();
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


    private void animateOuterCircle() {
        ImageView outerCircle = binding.sessionAnimatedCircle;

        circleAnimation = ObjectAnimator.ofPropertyValuesHolder(
                outerCircle,
                PropertyValuesHolder.ofFloat("scaleX", 1.25f),
                PropertyValuesHolder.ofFloat("scaleY", 1.25f));
        circleAnimation.setDuration(5000);

        circleAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        circleAnimation.setRepeatMode(ObjectAnimator.REVERSE);

        circleAnimation.start();
    }

    private long getTotalDurationMillis() {
        return config.getDuration() * 60 * 1000 + 1000;
    }

    private long getElapsedTimeMillis() {
        return getTotalDurationMillis() - millisLeft;
    }
}