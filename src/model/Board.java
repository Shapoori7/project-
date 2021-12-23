package model;

import java.util.ArrayList;

public class Board {
    private String name;
    private ArrayList<Category> categories;

    public Board(String name) {
        this.name = name;
        this.categories = new ArrayList<>();
        Category done = new Category("done", 0);
        this.categories.add(done);
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
        Task key = null;
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

}
