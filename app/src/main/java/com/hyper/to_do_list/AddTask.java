package com.hyper.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTask extends AppCompatActivity {

    public static final String EXTRA_ID = "com.hyper.to_do_list.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.hyper.to_do_list.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.hyper.to_do_list.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.hyper.to_do_list.EXTRA_PRIORITY";
    public static final String EXTRA_COMPLETED = "com.hyper.to_do_list.EXTRA_COMPLETED";
    private EditText edtTitle, edtDescription;
    private RadioGroup rgPriority;
    private RadioButton rbHigh, rbLow;
    private Button btnSaveTask;
    private Boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        rgPriority = findViewById(R.id.rgPriority);
        rbHigh = findViewById(R.id.rbHigh);
        rbLow = findViewById(R.id.rbLow);
        btnSaveTask = findViewById(R.id.btnSaveTask);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_ID)) {
            isEditMode = true;
            edtTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            edtDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            int priority = intent.getIntExtra(EXTRA_PRIORITY, 0);
            if (priority == 1) {
                rbHigh.setChecked(true);
            } else {
                rbLow.setChecked(true);
            }
        }

        btnSaveTask.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        int selectedPriority = rgPriority.getCheckedRadioButtonId();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || selectedPriority == -1) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int priority = (selectedPriority == R.id.rbHigh) ? 1 : 0;

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        if (isEditMode) {
            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            if (id != -1) {
                data.putExtra(EXTRA_ID, id);
                boolean isCompleted = getIntent().getBooleanExtra(EXTRA_COMPLETED, false);
                data.putExtra(EXTRA_COMPLETED, isCompleted);
            } else {
                Toast.makeText(this, "Task can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        setResult(RESULT_OK, data);
        finish();
    }
}
