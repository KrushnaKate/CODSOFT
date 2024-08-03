package com.hyper.to_do_list;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {TaskEntity.class},version = 1)
public abstract class TaskDb extends RoomDatabase {
    private static TaskDb instance;
    public abstract TaskListDAO taskListDAO();

    public static synchronized TaskDb getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDb.class,
                    "task_db").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
