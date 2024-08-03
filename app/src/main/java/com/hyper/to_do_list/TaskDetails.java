package com.hyper.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

public class TaskDetails extends AppCompatActivity {

    public static final String EXTRA_ID = "com.hyper.to_do_list.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.hyper.to_do_list.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.hyper.to_do_list.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.hyper.to_do_list.EXTRA_PRIORITY";
    public static final String EXTRA_COMPLETED = "com.hyper.to_do_list.EXTRA_COMPLETED";

    private TaskView taskView;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvTitle = findViewById(R.id.tvTitleDet);
        TextView tvDescription = findViewById(R.id.tvDescDet);
        TextView tvPriority = findViewById(R.id.tvPrioritValDet);
        CheckBox cvCompleted = findViewById(R.id.cbIsCompletedDet);
        Button btnEditTask = findViewById(R.id.btnEditTask);
        Button btnDeletTask = findViewById(R.id.btnDeleteTask);
        Button btnBackToMain = findViewById(R.id.btnBack); // New Button for going back

        Intent intent = getIntent();
        if(intent != null){
            taskId = intent.getIntExtra(EXTRA_ID, -1);
            String title = intent.getStringExtra(EXTRA_TITLE);
            String description = intent.getStringExtra(EXTRA_DESCRIPTION);
            int priority = intent.getIntExtra(EXTRA_PRIORITY, 0);
            boolean isCompleted = intent.getBooleanExtra(EXTRA_COMPLETED, false);

            tvTitle.setText(title);
            tvDescription.setText(description);
            tvPriority.setText(priority == 1 ? "High Priority" : "Low Priority");
            cvCompleted.setChecked(isCompleted);
        }

        taskView = new ViewModelProvider(this).get(TaskView.class);
        cvCompleted.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            TaskEntity task = new TaskEntity(tvTitle.getText().toString(), tvDescription.getText().toString(), isChecked, getIntent().getIntExtra(EXTRA_PRIORITY, 0));
            task.setId(taskId);
            taskView.update(task);
            Toast.makeText(TaskDetails.this, "Task updated", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_ID, taskId);
            resultIntent.putExtra(EXTRA_TITLE, tvTitle.getText().toString());
            resultIntent.putExtra(EXTRA_DESCRIPTION, tvDescription.getText().toString());
            resultIntent.putExtra(EXTRA_PRIORITY, getIntent().getIntExtra(EXTRA_PRIORITY, 0));
            resultIntent.putExtra(EXTRA_COMPLETED, isChecked);
            setResult(RESULT_OK, resultIntent);
        }));

        btnEditTask.setOnClickListener(v -> {
            Intent editIntent = new Intent(TaskDetails.this, AddTask.class);
            editIntent.putExtra(AddTask.EXTRA_ID, taskId);
            editIntent.putExtra(AddTask.EXTRA_TITLE, intent.getStringExtra(EXTRA_TITLE));
            editIntent.putExtra(AddTask.EXTRA_DESCRIPTION, intent.getStringExtra(EXTRA_DESCRIPTION));
            editIntent.putExtra(AddTask.EXTRA_PRIORITY, intent.getIntExtra(EXTRA_PRIORITY, 0));
            editIntent.putExtra(AddTask.EXTRA_COMPLETED, intent.getBooleanExtra(EXTRA_COMPLETED, false));
            startActivityForResult(editIntent, MainActivity.EDIT_TASK_REQ);
        });

        btnDeletTask.setOnClickListener(v -> {
            TaskEntity task = new TaskEntity(tvTitle.getText().toString(), tvDescription.getText().toString(), cvCompleted.isChecked(), getIntent().getIntExtra(EXTRA_PRIORITY, 0));
            task.setId(taskId);
            taskView.delete(task);
            Toast.makeText(TaskDetails.this, "Task Deleted", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });

        btnBackToMain.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_ID, taskId);
            resultIntent.putExtra(EXTRA_TITLE, tvTitle.getText().toString());
            resultIntent.putExtra(EXTRA_DESCRIPTION, tvDescription.getText().toString());
            resultIntent.putExtra(EXTRA_PRIORITY, getIntent().getIntExtra(EXTRA_PRIORITY, 0));
            resultIntent.putExtra(EXTRA_COMPLETED, cvCompleted.isChecked());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.EDIT_TASK_REQ && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddTask.EXTRA_ID, -1);
            if(id == -1){
                Toast.makeText(this, "task can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddTask.EXTRA_TITLE);
            String description = data.getStringExtra(AddTask.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddTask.EXTRA_PRIORITY, 0);
            Boolean isCompleted = data.getBooleanExtra(AddTask.EXTRA_COMPLETED, false);

            TaskEntity task = new TaskEntity(title, description, isCompleted, priority);
            task.setId(id);
            taskView.update(task);
            Toast.makeText(this, "task updated", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_ID, id);
            resultIntent.putExtra(EXTRA_TITLE, title);
            resultIntent.putExtra(EXTRA_DESCRIPTION, description);
            resultIntent.putExtra(EXTRA_PRIORITY, priority);
            resultIntent.putExtra(EXTRA_COMPLETED, isCompleted);
            setResult(RESULT_OK, resultIntent);
        }
    }
}
