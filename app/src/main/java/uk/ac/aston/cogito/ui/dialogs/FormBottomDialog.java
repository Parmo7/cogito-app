package uk.ac.aston.cogito.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public abstract class FormBottomDialog extends BottomSheetDialog {

    protected final BottomDialogListener listener;
    protected Button doneBtn;

    public FormBottomDialog(BottomDialogListener listener, @NonNull Context context, int layoutResId) {
        super(context, R.style.AppBottomSheetDialogTheme);
        super.setContentView(layoutResId);

        this.doneBtn = findViewById(R.id.done_btn);
        this.listener = listener;

        // Make sure the dialog displays in full height
        setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeForm();
        initializeDoneBtn();
    }

    protected void initializeDoneBtn() {
        if (doneBtn != null) {
            doneBtn.setOnClickListener(view -> {
                listener.onDoneBtnPressed(this);
                dismiss();
            });
        }
    }

    protected abstract void initializeForm();

    public abstract void show(SessionConfig sessionConfig);
}
