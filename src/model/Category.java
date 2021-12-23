package model;

import java.util.ArrayList;

public class Category {
    private String name;
    private int columnNumber;
    private ArrayList<Task> tasks;

    public Category(String name, int number) {
        this.name = name;
        this.columnNumber = number;
        this.tasks = new ArrayList<>();
    }

    public Task loadTask(String taskId) {
        for (Task task: this.tasks) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }

        return null;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }


    public String getName() {
        return name;
    }
}