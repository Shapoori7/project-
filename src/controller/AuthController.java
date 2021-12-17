package controller;

import model.User;
import view.Menu;
import view.WelcomeMenu;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

public class AuthController extends BaseController {
    private final Menu menu;
    private final Scanner scanner;
    private final HashMap<String, String> patterns;

    public AuthController() {
        super();
        this.scanner = new Scanner(System.in).useDelimiter("\n");

        this.patterns = new HashMap<>();
        this.patterns.put("signUp", "user\\s+creat\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        this.patterns.put("login", "Login\\s+(\\S+)\\s+(\\S+)$");

        this.menu = new WelcomeMenu();
        this.menu.show();
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

        else if (dbCtrl.findUserByUsername(email) != null) {
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
            // change menu
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
            // change menu
        }

    }

    public void commandHandler() {
        String command = scanner.nextLine();
        HashMap<String, Matcher> result = checkCommand(this.patterns, command);
        if (result == null) {
            menu.showError("error: ");
            commandHandler();
        }
        else {
            String key = result.entrySet().iterator().next().getKey();
            switch (key) {
                case "signUp" -> signUp(result.get(key));
                case "login" -> login(result.get(key));
            }

        }

    }

    private boolean validEmail(String email) {
        return true;
    }

}
