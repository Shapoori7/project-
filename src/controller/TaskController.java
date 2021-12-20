package controller;

import model.Task;
import model.User;
import model.UserType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;

public class TaskController extends MainMenuController{
    private final HashMap<String, String> patterns;

    public TaskController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("changeTitle", "");
        this.patterns.put("changeDescription", "");
        this.patterns.put("changePriority", "");
        this.patterns.put("changeDeadline", "");
        this.patterns.put("removeUser", "");
        this.patterns.put("addUser", "");

    }


    public void changeTitle(Matcher matcher) {
        if (leaderRequired()) {
            String id = matcher.group(1);
            String newTitle = matcher.group(2);

            Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
            task.setTitle(newTitle);
            Controller.DATA_BASE_CONTROLLER.updateTask(task);
            this.menu.showResponse("Title updated successfully!");

        }
    }

    public void changeDescription(Matcher matcher) {
        if (leaderRequired()) {
            String id = matcher.group(1);
            String newDescription = matcher.group(2);

            Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
            task.setDescription(newDescription);
            Controller.DATA_BASE_CONTROLLER.updateTask(task);
            this.menu.showResponse("Description updated successfully!");
        }
    }

    public void changePriority(Matcher matcher) {
        if (leaderRequired()) {
            String id = matcher.group(1);
            String newPriority = matcher.group(2);

            Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
            task.setPriority(newPriority);
            Controller.DATA_BASE_CONTROLLER.updateTask(task);
            this.menu.showResponse("Priority updated successfully!");
        }
    }

    public void changeDeadline(Matcher matcher) {
        if (leaderRequired()) {
            String id = matcher.group(1);
            String deadline = matcher.group(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'|'hh:mm");
            LocalDate newDeadline = LocalDate.parse(deadline, formatter);

            Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);

            if (task.getCreated().isAfter(newDeadline)) {
                this.menu.showError("New deadline is invalid!");
            }
            else {
                task.setDeadline(newDeadline);
                Controller.DATA_BASE_CONTROLLER.updateTask(task);
                this.menu.showResponse("Deadline updated successfully!");
            }

        }
    }

    public void removeUser(Matcher matcher) {
        if (leaderRequired()) {
            String id = matcher.group(1);
            String username = matcher.group(2);

            Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
            User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

            if (!task.getAssignedUsers().contains(user)) {
                this.menu.showError("There is not any user with this username " + username + " in list!");
            }
            else {
                task.removeUser(user);
                user.removeTask(task);
                Controller.DATA_BASE_CONTROLLER.updateTask(task);
                Controller.DATA_BASE_CONTROLLER.saveUser(user);
                this.menu.showResponse("User " + username + " removed successfully!");
            }

        }
    }

    public void addUser(Matcher matcher) {
        if (leaderRequired()) {
            String id = matcher.group(1);
            String username = matcher.group(2);

            Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
            User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

            if (user == null) {
                this.menu.showError("There is not any user with this username " + username + "!");
            }
            else {
                task.addUser(user);
                user.addTask(task);
                Controller.DATA_BASE_CONTROLLER.updateTask(task);
                Controller.DATA_BASE_CONTROLLER.saveUser(user);
                this.menu.showResponse("User " + username + " added successfully!");
            }

        }
    }

    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "changeTitle" -> changeTitle(matcher);
            case "changeDescription" -> changeDescription(matcher);
            case "changePriority" -> changePriority(matcher);
            case "changeDeadline" -> changeDeadline(matcher);
            case "removeUser" -> removeUser(matcher);
            case "addUser" -> addUser(matcher);
        }

    }

    private boolean leaderRequired() {
        boolean hasAccess = client.getType().equals(UserType.LEADER);
        if (!hasAccess) {
            this.menu.showError("You Don't Have Access To Do This Action!");
        }
        return hasAccess;
    }

    public Set<String> getPatternsNames() {
        return this.patterns.keySet();
    }

    public HashMap<String, String> getPatterns() {
        return patterns;
    }
}
