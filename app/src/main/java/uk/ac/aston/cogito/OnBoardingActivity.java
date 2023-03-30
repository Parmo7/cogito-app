package uk.ac.aston.cogito;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

import uk.ac.aston.cogito.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity {

    private ActivityOnBoardingBinding binding;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = binding.toolbar;
        setSupportActionBar(myToolbar);

        preferences = getSharedPreferences(getString(R.string.prefs_key), Context.MODE_PRIVATE);

        LinearLayout skipBtn = findViewById(R.id.skip_btn);
        skipBtn.setOnClickListener(v -> {
            markOnBoardingDone();
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    private void markOnBoardingDone() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.prefs_is_first_time), false);
        editor.apply();
    }
}