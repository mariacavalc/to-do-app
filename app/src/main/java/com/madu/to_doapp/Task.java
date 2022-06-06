package com.madu.to_doapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private Integer priority;
    private Boolean isDone;

    public Task(String title, String description, Integer priority, Boolean isDone) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isDone = isDone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPriority() {
        return priority;
    }

    public Boolean getDone() {
        return isDone;
    }
}
