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
        this.patterns.put("showTeam", "");
        this.patterns.put("createTeam", "");

    }

    private ArrayList<Team> loadLeaderTeams() {
        ArrayList<Team> leaderTeams = new ArrayList<>();
        for (Team team: Controller.DATA_BASE_CONTROLLER.loadTeamsList()) {
            if (team.getLeader().equals(client)) {
                leaderTeams.add(team);
            }
        }

        return leaderTeams;
    }


    public void showTeams() {
        ArrayList<Team> leaderTeams = loadLeaderTeams();

        if (leaderTeams.size() == 0) {
            this.menu.showError("There is no team for you!");
            return;
        }

        leaderTeams.sort(Comparator.comparing(Team::getName));
        leaderTeams.sort(Comparator.comparing(Team::getCreated));

        StringBuilder response = new StringBuilder();

        for (Team team: leaderTeams) {
            response.append(team.getName()).append("\n");
        }

        this.menu.showResponse(response.toString());
    }

    private Team getTeamByName(String teamName) {
        for (Team team: loadLeaderTeams()) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }

        return null;
    }

    public void showTeam(Matcher matcher) {
        String teamName = matcher.group(1);
        Team team = getTeamByName(teamName);

        if (team == null) {
            this.menu.showError("Team not found!");
            return;
        }

        this.menu.showResponse(team.toString());
    }

    private boolean validTeamName(String name) {
        return true;
    }

    public void createTeam(Matcher matcher) {
        String teamName = matcher.group(1);
        if (Controller.DATA_BASE_CONTROLLER.findTeamByName(teamName) != null) {
            this.menu.showError("There is another team with this name!");
            return;
        }

        Team team = new Team(client, teamName);


    }

    

    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "showTeams" -> {
                showTeams();
            }
            case "showTeam" -> {
                showTeam(matcher);
            }
            case "createTeam" -> {
                createTeam(matcher);
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
