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
}
