package model;

import controller.Controller;

import java.util.ArrayList;

public class Board {
    private final String name;
    private ArrayList<Category> categories;

    public Board(String name) {
        this.name = name;
        this.categories = new ArrayList<>();
        this.addCategory("done");
        this.addCategory("failed");
    }

    public String getName() {
        return name;
    }

    public Category loadCategory(String name) {
        for (Category category: this.categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public void addCategory(String name) {
        Category category = new Category(name, this.categories.size());
        this.categories.add(category);
    }

    public boolean isEmpty() {
        return this.categories.size() == 0;
    }

    public Task loadTask(String taskId) {
        Task key;
        for (Category category: this.categories) {
            key = category.loadTask(taskId);
            if (key != null) {
                return key;
            }
        }

        return null;
    }

    public void updateTask(Task task) {
        String id = task.getId();
        for (Category category: categories) {
            if (category.loadTask(id) != null) {
                category.updateTask(task);
                return;
            }
        }
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Category loadTaskCategory(Task task) {
        String taskId = task.getId();
        for (Category category: this.categories) {
            if (category.loadTask(taskId) != null) {
                return category;
            }
        }
        return null;
    }

    public void changeTaskCategory(Task task, Category categoryToRemove, Category categoryToMove) {
        int column1 = this.categories.indexOf(categoryToRemove);
        this.categories.get(column1).removeTask(task);
        int column2 = this.categories.indexOf(categoryToMove);
        this.categories.get(column2).addTask(task);
    }

    private int calculatePercentage(String nameOfCategory) {
        int totalTasks = 0;
        for (Category category: categories) {
            totalTasks += category.getTasks().size();
        }
        int categoryTasks = loadCategory(nameOfCategory).getTasks().size();

        return categoryTasks / totalTasks * 100;
    }

    private String getTasksToString() {
        StringBuilder str = new StringBuilder();
        ArrayList<Task> all = new ArrayList<>();
        for (Category category: this.categories) {
            all.addAll(category.getTasks());
        }

        all.sort(Controller.PRIORITY_COMPARATOR);
        for (Task task: all) {
            str.append(task.toString()).append("\n");
        }

        return str.toString();
    }

    public String toString(String leaderName) {
        return "Board name : " + name + "\n" +
                "Board completion : " + calculatePercentage("done") + " %" + "\n" +
                "Board failed : " + calculatePercentage("failed") + " %" + "\n" +
                "Board leader : " + leaderName + "\n" +
                "Board tasks : " + "\n" + getTasksToString();
    }

}
