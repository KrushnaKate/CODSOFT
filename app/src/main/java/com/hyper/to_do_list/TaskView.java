package com.hyper.to_do_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskView extends AndroidViewModel {

    private TaskListRepository taskListRepository;
    private LiveData<List<TaskEntity>> allTasks;

    public TaskView(@NonNull Application application) {
        super(application);
        taskListRepository = new TaskListRepository(application);
        allTasks = taskListRepository.getAllTasks();
    }

    public void insert(TaskEntity task) {
        taskListRepository.insert(task);
    }

    public void update(TaskEntity task) {
        taskListRepository.update(task);
    }

    public void delete(TaskEntity task) {
        taskListRepository.delete(task);
    }

    public LiveData<List<TaskEntity>> getAllTasks() {
        return allTasks;
    }
}
