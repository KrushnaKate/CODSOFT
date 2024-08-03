package com.hyper.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_TASK_REQ = 1;
    public static final int EDIT_TASK_REQ = 2;
    private TaskView taskview;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.rvTaskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        taskview = new ViewModelProvider(this).get(TaskView.class);
        taskview.getAllTasks().observe(this, new Observer<List<TaskEntity>>() {
            @Override
            public void onChanged(List<TaskEntity> taskEntities) {
                adapter.setTasks(taskEntities);
            }
        });

        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TaskEntity taskEntity) {
                Intent intent = new Intent(MainActivity.this, TaskDetails.class);
                intent.putExtra(TaskDetails.EXTRA_ID, taskEntity.getId());
                intent.putExtra(TaskDetails.EXTRA_TITLE, taskEntity.getTitle());
                intent.putExtra(TaskDetails.EXTRA_DESCRIPTION, taskEntity.getDescription());
                intent.putExtra(TaskDetails.EXTRA_PRIORITY, taskEntity.getPriority());
                intent.putExtra(TaskDetails.EXTRA_COMPLETED, taskEntity.isCompleted());
                startActivityForResult(intent, EDIT_TASK_REQ);
            }
        });

        findViewById(R.id.btnAddTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivityForResult(intent, ADD_TASK_REQ);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == ADD_TASK_REQ && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddTask.EXTRA_TITLE);
            String description = data.getStringExtra(AddTask.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddTask.EXTRA_PRIORITY, 0);
            boolean isCompleted = data.getBooleanExtra(AddTask.EXTRA_COMPLETED, false);

            TaskEntity task = new TaskEntity(title, description, isCompleted, priority);
            taskview.insert(task);

            Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_TASK_REQ && resultCode == RESULT_OK) {
            int id = data.getIntExtra(TaskDetails.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Task can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(TaskDetails.EXTRA_TITLE);
            String description = data.getStringExtra(TaskDetails.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(TaskDetails.EXTRA_PRIORITY, 0);
            boolean isCompleted = data.getBooleanExtra(TaskDetails.EXTRA_COMPLETED, false);

            TaskEntity task = new TaskEntity(title, description, isCompleted, priority);
            task.setId(id);
            taskview.update(task);

            Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task not saved/updated", Toast.LENGTH_SHORT).show();
        }
    }
}
