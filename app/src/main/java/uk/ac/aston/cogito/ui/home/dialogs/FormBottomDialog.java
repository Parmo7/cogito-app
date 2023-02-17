package uk.ac.aston.cogito.ui.home.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import uk.ac.aston.cogito.R;

public abstract class FormBottomDialog extends BottomSheetDialog {

    protected final BottomDialogListener listener;

    public FormBottomDialog(BottomDialogListener listener, @NonNull Context context, int layoutResId) {
        super(context, R.style.AppBottomSheetDialogTheme);
        setContentView(layoutResId);

        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeForm();
        initializeDoneBtn();
    }

    protected void initializeDoneBtn() {
        Button doneBtn = findViewById(R.id.done_btn);

        if (doneBtn != null) {
            doneBtn.setOnClickListener(view -> {
                listener.onDoneBtnPressed(this);
                dismiss();
            });
        }
    }

    protected abstract void initializeForm();
}
