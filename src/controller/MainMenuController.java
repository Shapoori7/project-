package controller;

import view.MainMenu;

import java.util.HashMap;
import java.util.regex.Matcher;

public class MainMenuController extends BaseController {
    private final HashMap<String, String> patterns;

    public MainMenuController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("enterMenu", "enter\\s+menu (\\S+)");
        this.patterns.putAll(Controller.PROFILE.getPatterns());
        this.patterns.putAll(Controller.TASK.getPatterns());
        this.patterns.putAll(Controller.CALENDAR.getPatterns());
        this.patterns.putAll(Controller.LEADER_MAIN.getPatterns());
        this.patterns.put("enterTeam", "");

        this.menu = new MainMenu();
    }

    private void enterTeam(Matcher matcher) {
        Controller.PROFILE.showTeam(matcher);
        showMenu("TeamMenu", matcher.group(1));
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
                case "enterTeam" -> enterTeam(matcher);
                default -> {
                    if (Controller.PROFILE.getPatternsNames().contains(key)) {
                        Controller.PROFILE.commandHandler(key, matcher);
                    }
                    else if (Controller.TASK.getPatternsNames().contains(key)) {
                        Controller.TASK.commandHandler(key, matcher);
                        this.commandHandler();
                    }
                    else if (Controller.CALENDAR.getPatternsNames().contains(key)) {
                        Controller.CALENDAR.commandHandler(key);
                        this.commandHandler();
                    }
                    else if (Controller.LEADER_MAIN.getPatternsNames().contains(key)) {
                        if (leaderRequired()) {
                            Controller.LEADER_MAIN.commandHandler(key, matcher);
                        }
                        this.commandHandler();

                    }
                }
            }

        }

    }


}
