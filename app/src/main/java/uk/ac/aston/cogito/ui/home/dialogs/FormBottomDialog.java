package uk.ac.aston.cogito.ui.home.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public abstract class FormBottomDialog extends BottomSheetDialog {

    protected final BottomDialogListener listener;
    protected SessionConfig sessionConfig;
    protected Button doneBtn;

    public FormBottomDialog(BottomDialogListener listener, @NonNull Context context, SessionConfig sessionConfig, int layoutResId) {
        super(context, R.style.AppBottomSheetDialogTheme);
        setContentView(layoutResId);

        this.doneBtn = findViewById(R.id.done_btn);
        this.listener = listener;
        this.sessionConfig = sessionConfig;
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

    public SessionConfig getSessionConfig() {
        return sessionConfig;
    }
}
