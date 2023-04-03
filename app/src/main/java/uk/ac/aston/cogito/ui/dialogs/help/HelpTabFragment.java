package uk.ac.aston.cogito.ui.dialogs.help;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.cogito.R;

public class HelpTabFragment extends Fragment {

    public enum TabType {HINTS, GENERAL, WARNINGS}
    private final TabType tabType;


    public HelpTabFragment(TabType tabType) {
        this.tabType = tabType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutResId;
        switch (tabType) {
            case GENERAL:
                layoutResId = R.layout.fragment_help_tab_general;
                break;

            case WARNINGS:
                layoutResId = R.layout.fragment_help_tab_warnings;
                break;

            case HINTS:
            default:
                layoutResId = R.layout.fragment_help_tab_hints;
        }

        // Inflate the layout for this fragment
        return inflater.inflate(layoutResId, container, false);
    }
}