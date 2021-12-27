package controller;

import model.Board;
import model.Category;
import model.Task;
import model.User;
import model.UserType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        this.patterns.put("createTask", "");
        this.patterns.put("showMembers", "show --members");
        this.patterns.put("addMember", "");
        this.patterns.put("deleteMember", "");
        this.patterns.put("suspendMember", "");
        this.patterns.put("promoteMember", "");

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

    private boolean validTitle(String title) {
        for (Task task: this.team.getTasks()) {
            if (task.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }

    private boolean validDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'|'hh:mm");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            return true;
        }
        return false;
    }

    public void createTask(Matcher matcher) {
        String taskTitle = matcher.group(1);
        String startDate = matcher.group(2);
        String finishDate = matcher.group(3);

        if (!validTitle(taskTitle)) {
            this.menu.showError("");
            return;
        }
        LocalDate created = LocalDate.parse(startDate);
        LocalDate deadline = LocalDate.parse(finishDate);

        if (validDate(startDate) || created.isAfter(deadline)) {
            this.menu.showError("Invalid start date!");
            return;
        }
        else if (validDate(finishDate) || deadline.isBefore(created)) {
            this.menu.showError("Invalid deadline!");
            return;
        }

        Task task = new Task(this.team.getName(), taskTitle, created, deadline);
        this.team.addTask(task);

        Controller.DATA_BASE_CONTROLLER.updateTask(task);
        Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);
    }

    public void showMembers() {
        StringBuilder membersNames = new StringBuilder();
        for (User usr: this.team.getMembers()) {
            membersNames.append(usr.getFullName()).append("\n");
        }

        this.menu.showResponse(membersNames.toString());
    }

    public void addMember(Matcher matcher) {
        String username = matcher.group(1);
        User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

        if (user == null) {
            this.menu.showError("No user exists with this username!");
            return;
        }

    }

    public void deleteMember(Matcher matcher) {
        String username = matcher.group(1);
        if (checkUserExists(username)) {
            this.team.deleteMember(username);
        }
    }

    public void suspendMember(Matcher matcher) {
        String username = matcher.group(1);
        if (checkUserExists(username)) {

        }
    }

    public void promoteMember(Matcher matcher) {
        String username = matcher.group(1);
        if (checkUserExists(username)) {
            String rate = matcher.group(2);
            User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);
            user.setType(UserType.valueOf(rate));
            this.team.updateUser(user);

            Controller.DATA_BASE_CONTROLLER.saveUser(user);
            Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);
        }
    }

    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "showAllTasks" -> showAllTasks();
            case "createTask" -> createTask(matcher);
            case "showMembers" -> showMembers();
            case "addMember" -> addMember(matcher);
            case "deleteMember" -> deleteMember(matcher);
            case "suspendMember" -> suspendMember(matcher);
            case "promoteMember" -> promoteMember(matcher);

        }

    }

    public Set<String> getPatternsNames() {
        return this.patterns.keySet();
    }

    public HashMap<String, String> getPatterns() {
        return patterns;
    }

    private boolean checkUserExists(String username) {
        User user = this.team.loadUser(username);

        if (user == null) {
            this.menu.showError("No user exists with this username!");
            return false;
        }
        return true;
    }

}
