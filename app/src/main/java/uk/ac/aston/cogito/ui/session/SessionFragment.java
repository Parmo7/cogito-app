package uk.ac.aston.cogito.ui.session;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentSessionBinding;
import uk.ac.aston.cogito.model.HistoryViewModel;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SessionFragment extends Fragment {

    // High-level variables
    private BottomNavigationView navBar;
    private FragmentSessionBinding binding;
    private SessionConfig config;

    // Variables relating to audio playback
    private enum PlayerInstruction {INIT, RESUME, PAUSE, REWIND, FORWARD, RELEASE, TICK}
    private MediaPlayer playerMusic;
    private MediaPlayer playerBell;
    private List<BellSchedule> bellSchedules;

    // Variables relating to the management of the timer
    private static final long SEEK_INTERVAL = 10 * 1000;     // 10 seconds
    private CountDownTimer timer;
    private long millisLeft;

    // Other variables
    private ObjectAnimator circleAnimation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        // Update the title and total duration
        if (!config.getName().isEmpty()) {
            binding.title.setText(config.getName());
        }
        binding.totalTime.setText("/ " + String.format("%02d", config.getDuration()) + ":00");

        // Configure the timer and the audio player
        timer = createTimer(getTotalDurationMillis());
        timer.start();
        manageAudioPlayback(PlayerInstruction.INIT);

        // Initialise the rest of the UI
        animateOuterCircle();
        initButtons();
    }


    private void initButtons() {
        binding.sessionPauseBtn.setOnClickListener(v -> {
            timer.cancel();

            manageAudioPlayback(PlayerInstruction.PAUSE);

            updateButtons(false);
            circleAnimation.pause();
        });

        binding.sessionContinueBtn.setOnClickListener(v -> {
            timer = createTimer(millisLeft);
            timer.start();

            manageAudioPlayback(PlayerInstruction.RESUME);

            updateButtons(true);
            circleAnimation.resume();
        });

        binding.sessionRewindBtn.setOnClickListener(v -> {
            long seekPos = getElapsedTimeMillis() - SEEK_INTERVAL;
            if (seekPos < 0) {
                seekPos = 0;
            }

            timer.cancel();
            timer = createTimer(getTotalDurationMillis() - seekPos);
            timer.start();

            manageAudioPlayback(PlayerInstruction.REWIND);
        });

        binding.sessionForwardBtn.setOnClickListener(v -> {
            long seekPos = getElapsedTimeMillis() + SEEK_INTERVAL;
            if (seekPos >= getTotalDurationMillis()) {
                seekPos = getTotalDurationMillis() - 1000;
            }

            timer.cancel();
            timer = createTimer(getTotalDurationMillis() - seekPos);
            timer.start();

            manageAudioPlayback(PlayerInstruction.FORWARD);
        });

        binding.sessionEndBtn.setOnClickListener(v -> {
            manageAudioPlayback(PlayerInstruction.RELEASE);
            NavHostFragment.findNavController(SessionFragment.this)
                    .navigate(R.id.action_session_to_navigation_home);
        });
    }


    private void updateButtons(boolean isPlaying) {
        int pauseRewFwdBtnsVisibility = isPlaying? View.VISIBLE : View.INVISIBLE;
        int countinueEndBtnsVisibility = !isPlaying? View.VISIBLE : View.INVISIBLE;

        binding.sessionPauseBtn.setVisibility(pauseRewFwdBtnsVisibility);
        binding.sessionRewindBtn.setVisibility(pauseRewFwdBtnsVisibility);
        binding.sessionForwardBtn.setVisibility(pauseRewFwdBtnsVisibility);

        binding.sessionContinueBtn.setVisibility(countinueEndBtnsVisibility);
        binding.sessionEndBtn.setVisibility(countinueEndBtnsVisibility);
    }


    private CountDownTimer createTimer(long timerDurationInMillis) {
        return new CountDownTimer(timerDurationInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;

                // Update the text in the circle (i.e. elapsed time)
                @SuppressLint("DefaultLocale") String elapsedTime = String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(getElapsedTimeMillis()),
                        TimeUnit.MILLISECONDS.toSeconds(getElapsedTimeMillis()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getElapsedTimeMillis())));
                binding.elapsedTime.setText(elapsedTime);

                // update audio playback
                manageAudioPlayback(PlayerInstruction.TICK);
            }

            @Override
            public void onFinish() {
                manageAudioPlayback(PlayerInstruction.RELEASE);
                circleAnimation.end();

                HistoryViewModel model = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
                model.recordTodaySession();

                if (model.isDailyCheckInDone()) {
                    NavHostFragment.findNavController(SessionFragment.this)
                            .navigate(R.id.action_session_to_navigation_home);

                } else {
                    NavHostFragment.findNavController(SessionFragment.this)
                            .navigate(R.id.action_session_to_checkInFragment);
                }
            }
        };
    }


    private void animateOuterCircle() {
        ImageView outerCircle = binding.sessionAnimatedCircle;

        circleAnimation = ObjectAnimator.ofPropertyValuesHolder(outerCircle,
                PropertyValuesHolder.ofFloat("scaleX", 1.25f),
                PropertyValuesHolder.ofFloat("scaleY", 1.25f));

        circleAnimation.setDuration(5000);
        circleAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        circleAnimation.setRepeatMode(ObjectAnimator.REVERSE);
        circleAnimation.start();
    }


    private void manageAudioPlayback(SessionFragment.PlayerInstruction instruction) {
        switch (instruction) {
            case INIT:
                if (config.getBgMusic().getResId() != 0) {
                    playerMusic = MediaPlayer.create(getContext(), config.getBgMusic().getResId());
                    playerMusic.setLooping(true);
                    playerMusic.setScreenOnWhilePlaying(true);
                    playerMusic.setVolume(0.1f, 0.1f);
                    playerMusic.start();
                }

                bellSchedules = new ArrayList<>();

                // Start bell
                if (config.getStartBellSound().getResId() != 0) {
                    int audioResId = config.getStartBellSound().getResId();
                    MediaPlayer mp = MediaPlayer.create(getContext(), audioResId);
                    long startTime = 0;
                    long endTime = startTime + mp.getDuration();

                    BellSchedule bs = new BellSchedule(audioResId, startTime, endTime);
                    bellSchedules.add(bs);
                }


                // Intermediate bells
                if (config.getIntermediateBellSound().getResId() != 0
                        && config.getNumIntermediateBells() > 0) {
                    long intermediateBellsInterval = getTotalDurationMillis() / (config.getNumIntermediateBells() + 1);
                    int audioResId = config.getIntermediateBellSound().getResId();
                    MediaPlayer mp = MediaPlayer.create(getContext(), audioResId);

                    long startTime = intermediateBellsInterval - 1000;
                    long endTime;

                    do {
                        endTime = startTime + mp.getDuration();

                        BellSchedule bs = new BellSchedule(audioResId, startTime, endTime);
                        bellSchedules.add(bs);

                        startTime += intermediateBellsInterval;
                    } while (startTime < getTotalDurationMillis() - 30000);
                }

                // End bell
                if (config.getEndBellSound().getResId() != 0) {
                    int audioResId = config.getEndBellSound().getResId();
                    MediaPlayer mp = MediaPlayer.create(getContext(), audioResId);
                    long endTime = getTotalDurationMillis();
                    long startTime = getTotalDurationMillis() - mp.getDuration();

                    BellSchedule bss = new BellSchedule(audioResId, startTime, endTime);
                    bellSchedules.add(bss);
                }

                break;

            case TICK:

                for (BellSchedule bss: bellSchedules) {
                    if ((playerBell == null || !playerBell.isPlaying())
                            && getElapsedTimeMillis() >= bss.getStartTimeMillis()
                            && getElapsedTimeMillis() <= bss.getEndTimeMillis()) {

                        long seekPos = getElapsedTimeMillis() - bss.getStartTimeMillis();
                        playerBell = MediaPlayer.create(getContext(), bss.getAudioResId());
                        playerBell.setVolume(1, 1);
                        playerBell.seekTo((int) seekPos);
                        playerBell.start();

                    }
                }

                break;

            case RESUME:
                if (playerMusic != null) {
                    playerMusic.start();
                }
                break;

            case PAUSE:
                if (playerMusic != null && playerMusic.isPlaying()) {
                    playerMusic.pause();
                }

                if (playerBell != null && playerBell.isPlaying()) {
                    playerBell.pause();
                }

                break;

            case REWIND:
                long seekPosRewind = getElapsedTimeMillis() - SEEK_INTERVAL;
                if (seekPosRewind < 0) {
                    seekPosRewind = 0;
                }

                if (playerMusic != null) {
                    playerMusic.seekTo((int) seekPosRewind);
                }
                if (playerBell != null) {
                    playerBell.stop();
                    playerBell = null;
                }

                break;

            case FORWARD:
                long seekPosForward = getElapsedTimeMillis() + SEEK_INTERVAL;
                if (seekPosForward >= getTotalDurationMillis()) {
                    seekPosForward = getTotalDurationMillis() - 1000;
                }

                if (playerMusic != null) {
                    playerMusic.seekTo((int) seekPosForward);
                }
                if (playerBell != null) {
                    playerBell.stop();
                    playerBell = null;
                }

                break;

            case RELEASE:
                if (playerMusic != null) {
                    playerMusic.release();
                }

                if (playerBell != null) {
                    playerBell.release();
                }

                break;
        }
    }

    private long getTotalDurationMillis() {
        return (long) config.getDuration() * 60 * 1000 + 1000;
    }

    private long getElapsedTimeMillis() {
        return getTotalDurationMillis() - millisLeft;
    }

    private long getTimeLeftMillis() {
        return millisLeft;
    }


    @Override
    public void onStop() {
        super.onStop();

        manageAudioPlayback(PlayerInstruction.RELEASE);
        circleAnimation.cancel();
        navBar.setVisibility(View.VISIBLE);

        binding = null;
    }
}