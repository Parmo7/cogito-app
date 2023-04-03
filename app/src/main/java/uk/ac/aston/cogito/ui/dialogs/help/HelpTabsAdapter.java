package uk.ac.aston.cogito.ui.dialogs.help;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import uk.ac.aston.cogito.ui.home.HomeFragment;

public class HelpTabsAdapter extends FragmentStateAdapter {

    public HelpTabsAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        HelpTabFragment.TabType tabType;

        // Based on the provided position in the tab layout,
        // initialize the appropriate child fragment
        switch (position) {
            case 1:
                tabType = HelpTabFragment.TabType.GENERAL;
                break;

            case 2:
                tabType = HelpTabFragment.TabType.WARNINGS;
                break;

            case 0:
            default:
                tabType = HelpTabFragment.TabType.HINTS;
                break;
        }

        return new HelpTabFragment(tabType);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}