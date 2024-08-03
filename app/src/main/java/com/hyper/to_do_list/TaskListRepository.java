package com.hyper.to_do_list;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskListRepository {

    private TaskListDAO taskListDAO;
    private LiveData<List<TaskEntity>> allTasks;

    public TaskListRepository(Application application){
        TaskDb db = TaskDb.getInstance(application);
        taskListDAO = db.taskListDAO();
        allTasks = taskListDAO.getAllTasks();
    }

    public void insert(TaskEntity task){
        new InsertTaskAsyncTask(taskListDAO).execute(task);
    }

    public void update(TaskEntity task){
        new UpdateTaskAsyncTask(taskListDAO).execute(task);
    }

    public void delete(TaskEntity task){
        new DeleteTaskAsyncTask(taskListDAO).execute(task);
    }

    public LiveData<List<TaskEntity>> getAllTasks(){
        return allTasks;
    }

    private static class InsertTaskAsyncTask extends AsyncTask<TaskEntity,Void,Void> {
        private TaskListDAO taskListDAO;

        private InsertTaskAsyncTask(TaskListDAO taskListDAO){
            this.taskListDAO = taskListDAO;
        }

        @Override
        protected Void doInBackground(TaskEntity... tasks){
            taskListDAO.insertTask(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<TaskEntity,Void,Void>{
        private TaskListDAO taskListDAO;

        private UpdateTaskAsyncTask(TaskListDAO taskListDAO){
            this.taskListDAO = taskListDAO;
        }
        @Override
        protected Void doInBackground(TaskEntity... tasks){
            taskListDAO.updateTask(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<TaskEntity,Void,Void>{
        private TaskListDAO taskListDAO;

        private DeleteTaskAsyncTask(TaskListDAO taskListDAO){

            this.taskListDAO = taskListDAO;

        }

        @Override
        protected Void doInBackground(TaskEntity... tasks){
            taskListDAO.deleteTask(tasks[0]);
            return null;
        }
    }
}




