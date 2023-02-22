package uk.ac.aston.cogito.ui.home.dialogs;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class EnterNameDialog extends FormBottomDialog {

    private EditText nameEditText;
    private Button saveBtn;

    public EnterNameDialog(BottomDialogListener listener, @NonNull Context context, SessionConfig sessionConfig) {
        super(listener, context, sessionConfig, R.layout.dialog_enter_name);
    }

    @Override
    protected void initializeForm() {
        nameEditText = findViewById(R.id.name_edit_text);
        saveBtn = findViewById(R.id.save_btn);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    saveBtn.setEnabled(true);
                } else {
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public String getValue() {
        if (nameEditText != null) {
            return nameEditText.getText().toString();
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sessionConfig.getName() != null && !sessionConfig.getName().isEmpty()) {
            nameEditText.setText(sessionConfig.getName());
        }
    }
}
