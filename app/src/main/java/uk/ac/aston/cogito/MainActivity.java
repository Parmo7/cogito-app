package uk.ac.aston.cogito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import uk.ac.aston.cogito.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = binding.toolbar;
        setSupportActionBar(myToolbar);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        preferences = getSharedPreferences(getString(R.string.prefs_key), Context.MODE_PRIVATE);

        // Check if we need to display our OnboardingtFragment
        boolean isFirstTimeUser = preferences.getBoolean(getString(R.string.prefs_is_first_time), true);
        if (isFirstTimeUser) {
            // The user hasn't seen the OnboardingSupportFragment yet, so show it
            startActivity(new Intent(this, OnBoardingActivity.class));
        }
    }

    public SharedPreferences getSharedPreferences() {
        return preferences;
    }

    public boolean getFormalRemindersScheduled() {
        return preferences.getBoolean(getString(R.string.prefs_formal_scheduled), false);
    }

    public boolean getInformalRemindersScheduled() {
        return preferences.getBoolean(getString(R.string.prefs_informal_scheduled), false);
    }

    public void setFormalRemindersScheduled(boolean areScheduled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.prefs_formal_scheduled), areScheduled);
        editor.apply();
    }

    public void setInformalRemindersScheduled(boolean areScheduled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.prefs_informal_scheduled), areScheduled);
        editor.apply();
    }
}