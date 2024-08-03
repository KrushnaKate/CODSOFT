package com.hyper.to_do_list;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_list")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private boolean isCompleted;
    private  int priority;//1 for high 0 for low

    public TaskEntity(String title, String description, boolean isCompleted, int priority) {

        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.priority = priority;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
