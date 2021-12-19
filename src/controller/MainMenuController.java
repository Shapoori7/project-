package controller;

import model.UserType;
import view.MainMenu;

import java.util.HashMap;
import java.util.regex.Matcher;

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
        this.patterns.put("removeUser", "");
        this.patterns.put("addUser", "");

        this.menu = new MainMenu();
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
                case "changePassword" -> Controller.PROFILE.changePassword(matcher);
                case "changeUsername" -> {
                    Controller.PROFILE.changeUsername(matcher);
                    commandHandler();
                }
                case "myProfile" -> {
                    Controller.PROFILE.myProfile();
                    commandHandler();
                }
                case "showLogs" -> {
                    Controller.PROFILE.showLogs();
                    commandHandler();
                }
                case "changeTitle" -> {
                    if (leaderRequired()) {Controller.TASK.changeTitle(matcher);}
                    commandHandler();
                }
                case "changeDescription" -> {
                    if (leaderRequired()) {Controller.TASK.changeDescription(matcher);}
                    commandHandler();
                }
                case "changePriority" -> {
                    if (leaderRequired()) {Controller.TASK.changePriority(matcher);}
                    commandHandler();
                }
                case "changeDeadline" -> {
                    if (leaderRequired()) {Controller.TASK.changeDeadline(matcher);}
                    commandHandler();
                }
                case "removeUser" -> {
                    if (leaderRequired()) {Controller.TASK.removeUser(matcher);}
                    commandHandler();
                }
                case "addUser" -> {
                    if (leaderRequired()) {Controller.TASK.addUser(matcher);}
                    commandHandler();
                }
                default -> {
                    this.menu.showError("invalid command");
                    commandHandler();
                }

            }

        }

    }


    private boolean leaderRequired() {
        boolean hasAccess = client.getType().equals(UserType.LEADER);
        if (!hasAccess) {
            this.menu.showError("You Don't Have Access To Do This Action!");
        }
        return hasAccess;
    }

}
