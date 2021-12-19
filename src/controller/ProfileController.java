package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileController extends MainMenuController{

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

}
