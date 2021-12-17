package controller;

import model.User;
import view.Menu;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseController {
    protected Menu menu;
    protected User client;

    public BaseController() {}

    public void showMenu(String menuName) {
        Controller.CONTROLLERS.get(menuName).getMenu().show();
        Controller.CONTROLLERS.get(menuName).commandHandler();
    }

    public HashMap<String, Matcher> checkCommand(HashMap<String, String> patterns, String command) {
        for (String key: patterns.keySet()) {
            String regex = patterns.get(key);
            if (Pattern.matches(regex, command)) {
                HashMap<String, Matcher> result = new HashMap<>();
                result.put(key, getMatcher(regex, command));
                return result;

            }
        }
        return null;
    }

    private Matcher getMatcher(String regex, String command) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            return matcher;
        }
        return null;

    }

    public void commandHandler(){};

    public Menu getMenu() {
        return this.menu;
    }

    public void setClient(User client) {
        this.client = client;
    }

}
