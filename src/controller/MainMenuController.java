package controller;

import model.Task;
import model.UserType;
import view.MainMenu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenuController extends BaseController {
    private final HashMap<String, String> patterns;

    public MainMenuController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("enterMenu", "enter\\s+menu (\\S+)");
        this.patterns.put("changePassword", "");
        this.patterns.put("changeUsername", "");
        this.patterns.put("showTeams", "");
        this.patterns.put("showTeam", "");
        this.patterns.put("myProfile", "Profile --show --myProfile");
        this.patterns.put("showLogs", "");
        this.patterns.put("showNotifications", "");
        this.patterns.put("changeTitle", "");
        this.patterns.put("changeDescription", "");
        this.patterns.put("changePriority", "");
        this.patterns.put("changeDeadline", "");

        this.menu = new MainMenu();
    }

    public void changePassword(Matcher matcher) {
        String oldPass = matcher.group(1);
        String newPass = matcher.group(2);

        if (!client.getPassword().equals(oldPass)) {
            this.menu.showError("wrong old password!");
            this.menu.showResponse("type your old password again:");
            oldPass = Controller.INPUT.nextLine();
            if (!client.getPassword().equals(oldPass)) {
                client = null;
                showMenu("WelcomeMenu");
            }
        }

        else if (oldPass.equals(newPass)) {
            this.menu.showError("Please type a New Password !");
            commandHandler();
        }

        else if (!validPassword(newPass)) {
            this.menu.showError(
                    "Please Choose A strong Password " +
                    "(Containing at least 8 characters including 1 digit and 1 Capital Letter)");
            commandHandler();
        }
        else {
            client.setPassword(newPass);
            Controller.DATA_BASE_CONTROLLER.saveUser(client);
            this.menu.showResponse("password changed successfully");
            client = null;
            showMenu("WelcomeMenu");
        }

    }

    public void changeUsername(Matcher matcher) {
        String newUsername = matcher.group(1);

        if (newUsername.length() < 4) {
            this.menu.showError("Your new username must include at least 4 characters!");
        }
        else if (Controller.DATA_BASE_CONTROLLER.findUserByUsername(newUsername) != null) {
            this.menu.showError("username already taken!");
        }
        else if (!validUsername(newUsername)) {
            this.menu.showError("New username contains Special Characters! Please remove them and try again");
        }
        else if (newUsername.equals(client.getUsername())) {
            this.menu.showError("you already have this username!");
        }
        else {
            client.setUsername(newUsername);
            Controller.DATA_BASE_CONTROLLER.saveUser(client);
            this.menu.showResponse("username changed successfully");
        }
        commandHandler();

    }

    public void myProfile() {
        this.menu.showResponse(client.toString());
        commandHandler();
    }

    public void showLogs() {
        StringBuilder userLogs = new StringBuilder();
        for (String log: client.getLogs()) {
            userLogs.append(log);
            userLogs.append("\n");
        }
        this.menu.showResponse(userLogs.toString());
        commandHandler();
    }

    public void changeTitle(Matcher matcher) {
        String id = matcher.group(1);
        String newTitle = matcher.group(2);

        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
        task.setTitle(newTitle);
        Controller.DATA_BASE_CONTROLLER.updateTask(task);
        this.menu.showResponse("Title updated successfully!");
        commandHandler();

    }

    public void changeDescription(Matcher matcher) {
        String id = matcher.group(1);
        String newDescription = matcher.group(2);

        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
        task.setDescription(newDescription);
        Controller.DATA_BASE_CONTROLLER.updateTask(task);
        this.menu.showResponse("Description updated successfully!");
        commandHandler();

    }

    public void changePriority(Matcher matcher) {
        String id = matcher.group(1);
        String newPriority = matcher.group(2);

        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
        task.setPriority(newPriority);
        Controller.DATA_BASE_CONTROLLER.updateTask(task);
        this.menu.showResponse("Priority updated successfully!");
        commandHandler();
    }

    public void changeDeadline(Matcher matcher) {
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

        commandHandler();
    }

    @Override
    public void commandHandler() {
        String command = Controller.INPUT.nextLine();
        HashMap<String, Matcher> result = checkCommand(this.patterns, command);
        if (result == null) {
            menu.showError("error: invalid input from you!");
            commandHandler();
        }
        else {
            String key = result.entrySet().iterator().next().getKey();
            Matcher matcher = result.get(key);
            switch (key) {
                case "enterMenu" -> showMenu(matcher.group());
                case "changePassword" -> changePassword(matcher);
                case "changeUsername" -> changeUsername(matcher);
                case "myProfile" -> myProfile();
                case "showLogs" -> showLogs();
                case "changeTitle" -> {
                    if (leaderRequired()) {changeTitle(matcher);}
                }
                case "changeDescription" -> {
                    if (leaderRequired()) {changeDescription(matcher);}
                }
                case "changePriority" -> {
                    if (leaderRequired()) {changePriority(matcher);}
                }
                case "changeDeadline" -> {
                    if (leaderRequired()) {changeDeadline(matcher);}
                }

            }

        }

    }

    private boolean validPassword(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
        return passwordPattern.matcher(password).matches() && password.length() >= 8;
    }

    private boolean validUsername(String username) {
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9-]*$");
        return usernamePattern.matcher(username).matches();
    }

    private boolean leaderRequired() {
        boolean hasAccess = client.getType().equals(UserType.LEADER);
        if (!hasAccess) {
            this.menu.showError("You Don't Have Access To Do This Action!");
        }
        return hasAccess;
    }

}
