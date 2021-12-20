package controller;

import model.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileController extends MainMenuController{
    private final HashMap<String, String> patterns;

    public ProfileController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("changePassword", "");
        this.patterns.put("changeUsername", "");
        this.patterns.put("showTeams", "");
        this.patterns.put("showTeam", "");
        this.patterns.put("myProfile", "Profile --show --myProfile");
        this.patterns.put("showLogs", "");
        this.patterns.put("showNotifications", "");

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
            super.commandHandler();
        }

        else if (!validPassword(newPass)) {
            this.menu.showError(
                    "Please Choose A strong Password " +
                            "(Containing at least 8 characters including 1 digit and 1 Capital Letter)");
            super.commandHandler();
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

    }

    public void showTeams() {
        ArrayList<Team> teams = client.getTeams();
        teams.sort(Comparator.comparing(Team::getCreated));

        StringBuilder userTeams = new StringBuilder();

        for (Team team: teams) {
            userTeams.append(team.getName()).append("\n");
        }

        this.menu.showResponse(userTeams.toString());

    }

    public void showTeam(Matcher matcher) {
        String teamName = matcher.group(1);
        Team team = Controller.DATA_BASE_CONTROLLER.findTeamByName(teamName);

        this.menu.showResponse(team.toString(client));

    }

    public void myProfile() {
        this.menu.showResponse(client.toString());
    }

    public void showLogs() {
        StringBuilder userLogs = new StringBuilder();
        for (String log: client.getLogs()) {
            userLogs.append(log);
            userLogs.append("\n");
        }
        this.menu.showResponse(userLogs.toString());
    }

    private boolean validPassword(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
        return passwordPattern.matcher(password).matches() && password.length() >= 8;
    }

    private boolean validUsername(String username) {
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9-]*$");
        return usernamePattern.matcher(username).matches();
    }

    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "changePassword" -> changePassword(matcher);
            case "changeUsername" -> {
                changeUsername(matcher);
                super.commandHandler();
            }
            case "showTeams" -> {
                showTeams();
                super.commandHandler();
            }
            case "showTeam" -> {
                showTeam(matcher);
                super.commandHandler();
            }
            case "myProfile" -> {
                myProfile();
                super.commandHandler();
            }
            case "showLogs" -> {
                showLogs();
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

}
