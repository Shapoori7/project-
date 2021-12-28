package controller;

import model.Message;
import model.Task;
import model.Team;
import view.TeamMenu;

import java.util.HashMap;
import java.util.regex.Matcher;

public class TeamMenuController extends BaseController{
    private final HashMap<String, String> patterns;
    protected Team team;

    public TeamMenuController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("scoreboard", "Scoreboard --show");
        this.patterns.put("roadmap", "Roadmap --show");
        this.patterns.put("chatroom", "Chatroom --show");
        this.patterns.put("sendMessage", "");
        this.patterns.put("showTasks", "show tasks");
        this.patterns.put("showTask", "");

        this.menu = new TeamMenu();

    }

    public void updatePatterns() {
        this.patterns.putAll(Controller.TASK.getPatterns());
        this.patterns.putAll(Controller.BOARD.getPatterns());
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void showScoreboard() {
        this.menu.showResponse(this.team.generateScoreboard());
    }

    public void showRoadmap() {
        this.menu.showResponse(this.team.generateRoadmap());
    }

    public void showChatroom() {
        this.menu.showResponse(this.team.generateChatroom());
    }

    public void sendMessage(Matcher matcher) {
        String text = matcher.group(1);
        Message message = new Message(client.getFullName(), text);

        this.team.addMessage(message);
        showChatroom();
        Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);
    }

    public void showTasks() {
        this.menu.showResponse(this.team.getTasksToString());
    }

    public void showTask(Matcher matcher) {
        String id = matcher.group(1);
        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
        this.menu.showResponse(task.toString());

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
                case "scoreboard" -> {
                    showScoreboard();
                    commandHandler();
                }
                case "roadmap" -> {
                    showRoadmap();
                    commandHandler();
                }
                case "chatroom" -> {
                    showChatroom();
                    commandHandler();
                }
                case "sendMessage" -> {
                    sendMessage(matcher);
                    commandHandler();
                }
                case "showTasks" -> {
                    showTasks();
                    commandHandler();
                }
                case "showTask" -> {
                    showTask(matcher);
                    commandHandler();
                }
                default -> {
                    if (Controller.TASK.getPatternsNames().contains(key)) {
                        Controller.TASK.commandHandler(key, matcher);
                        this.commandHandler();
                    }
                    else if (Controller.BOARD.getPatternsNames().contains(key)) {
                        Controller.BOARD.commandHandler(key, matcher);
                        this.commandHandler();
                    }
                }

            }

        }

    }

}
