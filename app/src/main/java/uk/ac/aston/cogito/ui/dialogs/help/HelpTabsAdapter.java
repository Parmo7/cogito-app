package uk.ac.aston.cogito.ui.dialogs.help;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

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
                tabType = HelpTabFragment.TabType.MINDFULNESS;
                break;

            case 2:
                tabType = HelpTabFragment.TabType.APP;
                break;

            case 3:
                tabType = HelpTabFragment.TabType.WARNINGS;
                break;

            case 0:
            default:
                tabType = HelpTabFragment.TabType.TIPS;
                break;
        }

        return new HelpTabFragment(tabType);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}