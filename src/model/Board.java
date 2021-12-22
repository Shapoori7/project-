package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    private String name;
    private HashMap<String, ArrayList<Task>> categories;

    public Board(String name) {
        this.name = name;
        this.categories = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public boolean categoryExists(String name) {
        return this.categories.keySet().contains(name);
    }

    public void addCategory(String name) {
        this.categories.put(name, new ArrayList<>());
    }



}
