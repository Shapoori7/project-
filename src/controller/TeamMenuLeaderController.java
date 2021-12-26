package controller;

import model.Board;
import model.Category;
import model.Task;
import model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;

public class TeamMenuLeaderController extends TeamMenuController{
    private final HashMap<String, String> patterns;

    public TeamMenuLeaderController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("showAllTasks", "show --all --tasks");

    }

    private String getStatus(Task task) {
        for (Board board: this.team.getBoards()) {
            Category category = board.loadTaskCategory(task);
            if (category != null) {
                return category.getName();
            }
        }
        return "(status not found!)";
    }

    private String generateTaskString(Task task) {
        String status = getStatus(task);
        return "created: " + task.getCreated() + ", " +
                "users: " + task.usersToString() + ", " +
                "status: " + status + ", " +
                "deadline: " + task.getDeadline();
    }

    public void showAllTasks() {
        StringBuilder response = new StringBuilder();
        ArrayList<Task> tasks = this.team.getTasks();
        tasks.sort(Comparator.comparing(Task::getCreated));

        for (Task task: tasks) {
            response.append(generateTaskString(task)).append("\n");
        }

        this.menu.showResponse(response.toString());
    }


    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "showAllTasks" -> showAllTasks();

        }

    }

    public Set<String> getPatternsNames() {
        return this.patterns.keySet();
    }

    public HashMap<String, String> getPatterns() {
        return patterns;
    }

}
