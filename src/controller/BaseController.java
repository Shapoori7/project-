package controller;

import view.Menu;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseController {
    protected DataBaseController dbCtrl;
    protected Menu menu;
    protected Scanner scanner;
    private final HashMap<String, BaseController> controllers;

    public BaseController() {
        this.controllers = new HashMap<>();
        this.dbCtrl = new DataBaseController();
        this.scanner = new Scanner(System.in).useDelimiter("\n");
    }

    public void init() {
        this.controllers.put("WelcomeMenu", new AuthController());
        this.controllers.put("MainMenu", new MainMenuController());
    }

    public void showMenu(String menuName) {
        this.controllers.get(menuName).getMenu().show();
        this.controllers.get(menuName).commandHandler();
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
}
