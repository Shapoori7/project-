package controller;

import model.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeaderController extends MainMenuController{
    private final HashMap<String, String> patterns;

    public LeaderController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("showTeams", "show --teams");

    }


    public void showTeams() {
        ArrayList<Team> leaderTeams = new ArrayList<>();
        for (Team team: Controller.DATA_BASE_CONTROLLER.loadTeamsList()) {
            if (team.getLeader().equals(client)) {
                leaderTeams.add(team);
            }
        }

        if (leaderTeams.size() == 0) {
            this.menu.showError("There is no team for you!");
            return;
        }

        loaderTeams.sort(Comparator.comparing(Team::getName));
        loaderTeams.sort(Comparator.comparing(Team::getCreated));

        String response = "";

        for (Team team: leaderTeams) {
            response += team.getName() += "\n";
        }

        this.menu.showResponse(response);
    }

    

    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "showTeams" -> {
                showTeams();
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
