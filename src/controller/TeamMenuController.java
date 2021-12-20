package controller;

import model.Message;
import model.Team;
import view.TeamMenu;

import java.util.HashMap;
import java.util.regex.Matcher;

public class TeamMenuController extends BaseController{
    private final HashMap<String, String> patterns;
    private Team team;

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



            }

        }

    }

}
