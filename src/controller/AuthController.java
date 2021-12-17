package controller;

import model.User;
import view.WelcomeMenu;

import java.util.HashMap;
import java.util.regex.Matcher;

public class AuthController extends BaseController {
    private final HashMap<String, String> patterns;

    public AuthController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("signUp", "user\\s+creat\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        this.patterns.put("login", "Login\\s+(\\S+)\\s+(\\S+)$");

        this.menu = new WelcomeMenu();

    }

    public void signUp(Matcher matcher) {
        String username = matcher.group(1);
        String password1 = matcher.group(2);
        String password2 = matcher.group(3);
        String email = matcher.group(4);

        if (!password1.equals(password2)) {
            this.menu.showError("Your passwords are not the same!");
            commandHandler();
        }

        else if (dbCtrl.findUserByUsername(username) != null) {
            this.menu.showError("user with username " + username + " already exists!");
            commandHandler();
        }

        else if (dbCtrl.findUserByEmail(email) != null) {
            this.menu.showError("User with this email already exists!");
            commandHandler();
        }

        else if (!validEmail(email)) {
            this.menu.showError("Email address is invalid!");
            commandHandler();
        }

        else {
            User user = new User(username, password1, email);
            dbCtrl.saveUser(user);
            this.menu.showResponse("user created successfully!");
            showMenu("MainMenu");
        }

    }

    public void login(Matcher matcher) {
        String username = matcher.group(1);
        String password = matcher.group(2);

        User user = dbCtrl.findUserByUsername(username);

        if (user == null) {
            this.menu.showError("There is not any user with username: " + username + "!");
            commandHandler();
        }
        else if (!user.getPassword().equals(password)) {
            this.menu.showError("Username and password didn't match!");
            commandHandler();
        }
        else {
            this.menu.showResponse("user logged in successfully!");
            showMenu("MainMenu");
        }

    }

    @Override
    public void commandHandler() {
        String command = scanner.nextLine();
        HashMap<String, Matcher> result = checkCommand(this.patterns, command);
        if (result == null) {
            menu.showError("error: invalid input from you!");
            commandHandler();
        }
        else {
            String key = result.entrySet().iterator().next().getKey();
            Matcher matcher = result.get(key);
            switch (key) {
                case "signUp" -> signUp(matcher);
                case "login" -> login(matcher);
            }

        }

    }

    private boolean validEmail(String email) {
        return true;
    }

}
