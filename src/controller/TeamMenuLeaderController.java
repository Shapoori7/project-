package controller;

import model.Board;
import model.Category;
import model.Message;
import model.Task;
import model.Team;
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
        this.patterns.put("assignMember", "");
        this.patterns.put("showScoreBoard", "show --scoreboard");
        this.patterns.put("sendMessageToMember", "");
        this.patterns.put("sendMessageToTeam", "");

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
            UserType newRate = UserType.valueOf(rate);
            User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

            user.setType(newRate);
            this.team.updateUser(user);

            Controller.DATA_BASE_CONTROLLER.saveUser(user);
            Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);

            if (newRate.equals(UserType.LEADER)) {
                this.client.removeTeam(this.team);;
                this.team.deleteMember(client.getUsername());
                Controller.DATA_BASE_CONTROLLER.saveUser(client);
                showMenu("MainMenu");
            }
            else {
                super.commandHandler();
            }

        }
    }

    public void assignMember(Matcher matcher) {
        String username = matcher.group(2);
        if (checkUserExists(username)) {
            String taskId = matcher.group(1);
            Task task = this.team.loadTask(taskId);
            if (task == null) {
                this.menu.showError("No Task exists with this id!");
                return;
            }
            User user = this.team.loadUser(username);
            task.addUser(user);
            user.addTask(task);

            Controller.DATA_BASE_CONTROLLER.updateTask(task);
            Controller.DATA_BASE_CONTROLLER.updateTeam(team);
            Controller.DATA_BASE_CONTROLLER.saveUser(user);

            this.menu.showResponse("Member Assigned Successfully!");
        }
    }

    public void showScoreBoard() {
        if (this.team.getMembers().size() == 0) {
            this.menu.showError("There is no member in this team!");
            return;
        }
        super.showScoreboard();
    }

    public void sendMessageToMember(Matcher matcher) {
        String username = matcher.group(2);
        if (checkUserExists(username)) {
            String notifContent = matcher.group(1);
            Message message = new Message(client.getUsername(), notifContent);
            User reciever = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);
            reciever.addNotification(message);
            Controller.DATA_BASE_CONTROLLER.saveUser(reciever);
        }

    }

    public void sendMessageToTeam(Matcher matcher) {
        String teamName = matcher.group(2);
        Team team = Controller.DATA_BASE_CONTROLLER.findTeamByName(teamName);

        if (team == null) {
            this.menu.showError("No team exists with this name!");
            return;
        }
        
        String notifContent = matcher.group(1);
        Message message = new Message(client.getUsername(), notifContent);
        for (User member: team.getMembers()) {
            member.addNotification(message);
            Controller.DATA_BASE_CONTROLLER.saveUser(member);

        }
        
    }

    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "showAllTasks" -> {
                showAllTasks();
                super.commandHandler();
            }
            case "createTask" -> {
                createTask(matcher);
                super.commandHandler();
            }
            case "showMembers" -> {
                showMembers();
                super.commandHandler();
            }
            case "addMember" -> {
                addMember(matcher);
                super.commandHandler();
            }
            case "deleteMember" -> {
                deleteMember(matcher);
                super.commandHandler();
            }
            case "suspendMember" -> {
                suspendMember(matcher);
                super.commandHandler();
            }
            case "promoteMember" -> promoteMember(matcher);
            case "assignMember" -> {
                assignMember(matcher);
                super.commandHandler();
            }
            case "showScoreBoard" -> {
                showScoreBoard();
                super.commandHandler();
            }
            case "sendMessageToMember" -> {
                sendMessageToMember(matcher);
                super.commandHandler();
            }
            case "sendMessageToTeam" -> {
                sendMessageToTeam(matcher);
                super.commandHandler();
            }

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
