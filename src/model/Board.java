package model;

import java.util.ArrayList;

public class Board {
    private String name;
    private ArrayList<Category> categories;

    public Board(String name) {
        this.name = name;
        this.categories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean categoryExists(String name) {
        for (Category category: this.categories) {
            if (category.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void addCategory(String name) {
        Category category = new Category(name, this.categories.size());
        this.categories.add(category);
    }

    public boolean isEmpty() {
        return this.categories.size() == 0;
    }

    public Task loadTask(String taskId) {
        Task key = null;
        for (Category category: this.categories) {
            key = category.loadTask(taskId);
            if (key != null) {
                return key;
            }
        }

        return null;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

}
