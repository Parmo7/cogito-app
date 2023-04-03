package uk.ac.aston.cogito.ui.dialogs.help;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import uk.ac.aston.cogito.R;

public class HelpDialog extends BottomSheetDialogFragment {

    private HelpTabsAdapter helpTabsAdapter;
    private ViewPager2 viewPager;
    private static final TabProp[] tabProps = new TabProp[] {
        new TabProp(R.string.help_tab_hints,R.drawable.ic_outline_lightbulb_24),
        new TabProp(R.string.help_tab_general,R.drawable.ic_outline_info_24),
        new TabProp(R.string.help_tab_warnings,R.drawable.ic_round_warning_amber_24)
    };

    public HelpDialog() {
        super(R.layout.dialog_help);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helpTabsAdapter = new HelpTabsAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(helpTabsAdapter);
        viewPager.setSaveEnabled(false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(tabProps[position].nameId);
                    tab.setIcon(tabProps[position].iconId);
                }
        ).attach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog inheritedDialog =  super.onCreateDialog(savedInstanceState);

        inheritedDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        return inheritedDialog;
    }




    private static class TabProp {

        int nameId;
        int iconId;

        TabProp(int nameId, int iconId) {
            this.nameId = nameId;
            this.iconId = iconId;
        }
    }
}
