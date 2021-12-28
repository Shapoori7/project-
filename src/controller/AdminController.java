package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import model.User;
import model.UserType;
import model.Message;
import model.Team;
import model.TeamStatus;

public class AdminController extends BaseController{
    private final HashMap<String, String> patterns;

    public AdminController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("showProfileOfUser", "");
        this.patterns.put("banUser", "");
        this.patterns.put("cangeRole", "");
        this.patterns.put("notifAll", "");
        this.patterns.put("notifUser", "");
        this.patterns.put("notifTeam", "");
        this.patterns.put("scoreBoardOfTeam", ""); // show --scoreboard [team name]
        this.patterns.put("showPendingTeams", "");
        this.patterns.put("acceptTeams", "");
        this.patterns.put("rejectTeams", "");

    }

    public void showProfileOfUser(Matcher matcher) {
        String username = matcher.group(1);
        User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

        if (user == null) {
            this.menu.showError("There is no user with this username");
            return;
        }

        this.menu.showResponse(user.toString());
    }

    public void banUser(Matcher matcher) {
        String username = matcher.group(1);

        User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

        if (user == null) {
            this.menu.showError("There is no user with this username");
            return;
        }

        Controller.DATA_BASE_CONTROLLER.removeUser(user);

    }

    public void changeRole(Matcher matcher) {
        String username = matcher.group(1);

        User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

        if (user == null) {
            this.menu.showError("There is no user with this username");
            return;
        }

        String newRole = matcher.group(2);
        user.setType(UserType.valueOf(newRole));

        Controller.DATA_BASE_CONTROLLER.saveUser(user);
    }

    public void notifUser(Matcher matcher) {
        String username = matcher.group(2);

        User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

        if (user == null) {
            this.menu.showError("There is no user with this username");
            return;
        }
        
        String notifContent = matcher.group(1);
        Message message = new Message(client.getUsername(), notifContent);
        user.addNotification(message);
        Controller.DATA_BASE_CONTROLLER.saveUser(user);
    }

    public void notifTeam(Matcher matcher) {
        String teamName = matcher.group(2);

        Team team = Controller.DATA_BASE_CONTROLLER.findTeamByName(teamName);

        if (team == null) {
            this.menu.showError("There is no team with this name");
            return;
        }

        String notifContent = matcher.group(1);
        Message message = new Message(client.getUsername(), notifContent);
        for (User member: team.getMembers()) {
            member.addNotification(message);
            Controller.DATA_BASE_CONTROLLER.saveUser(member);

        }

        User leaderOfTeam = team.getLeader();
        leaderOfTeam.addNotification(message);
        Controller.DATA_BASE_CONTROLLER.saveUser(leaderOfTeam);
    }

    public void notifAll(Matcher matcher) {
        String notifContent = matcher.group(1);
        Message message = new Message(client.getUsername(), notifContent);
        ArrayList<User> users = Controller.DATA_BASE_CONTROLLER.loadUsersList();
        for (User member: users) {
            member.addNotification(message);
        }

        Controller.DATA_BASE_CONTROLLER.updateAllUsers(users);
    }

    public void scoreBoardOfTeam(Matcher matcher) {
        String teamName = matcher.group(1);

        Team team = Controller.DATA_BASE_CONTROLLER.findTeamByName(teamName);

        if (team == null) {
            this.menu.showError("There is no team with this name");
            return;
        }

    }

    public void showPendingTeams() {
        StringBuilder pendingTeams = new StringBuilder();
        for (Team team: Controller.DATA_BASE_CONTROLLER.loadTeamsList()) {
            if (team.getStatus().equals(TeamStatus.PENDING)) {
                pendingTeams.append(team.getName()).append("\n");
            }
        }

        if (pendingTeams.isEmpty()) {
            this.menu.showError("There are no Teams in Pending Status!");
            return;
        }

        this.menu.showResponse(pendingTeams.toString());
    }

    private ArrayList<Team> checkAllPending(String[] teamNames) {
        ArrayList<Team> teams = new ArrayList<>();

        for (String name: teamNames) {
            Team team = Controller.DATA_BASE_CONTROLLER.findTeamByName(name);

            if (team == null || team.getStatus().equals(TeamStatus.ACCEPTED)) {
                this.menu.showError("Some teams are not in pending status! Try again");
                return null;
            }

            teams.add(team);
        }

        return teams;
    }

    public void acceptTeams(Matcher matcher) {
        String[] teamNames = matcher.group(1).split(" ");
        ArrayList<Team> pendingTeams = checkAllPending(teamNames);

        if (pendingTeams != null) {
            for (Team team: pendingTeams) {
                team.setStatus(TeamStatus.ACCEPTED);
                Controller.DATA_BASE_CONTROLLER.updateTeam(team);
            }

        }

    }

    public void rejectTeams(Matcher matcher) {
        String[] teamNames = matcher.group(1).split(" ");
        ArrayList<Team> pendingTeams = checkAllPending(teamNames);

        if (pendingTeams != null) {
            for (Team team: pendingTeams) {
                Controller.DATA_BASE_CONTROLLER.removeTeam(team);
            }
        }

    }

    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "showProfileOfUser" -> {
                if (adminRequired()) {
                    showProfileOfUser(matcher);
                }
            }
            case "banUser" -> {
                if (adminRequired()) {
                    banUser(matcher);
                }
            }
            case "cahngeRole" -> {
                if (adminRequired()) {
                    changeRole(matcher);
                }
            }
            case "notifUser" -> {
                if (adminRequired()) {
                    notifUser(matcher);
                }
            }
            case "notifTeam" -> {
                if (adminRequired()) {
                    notifTeam(matcher);
                }
            }
            case "notifAll" -> {
                if (adminRequired()) {
                    notifAll(matcher);
                }
            }
            case "scoreBoardOfTeam" -> {
                if (adminRequired()) {
                    scoreBoardOfTeam(matcher);
                }
            }
            case "showPendingTeams" -> {
                if (adminRequired()) {
                    showPendingTeams();
                }
            }
            case "acceptTeams" -> {
                if (adminRequired()) {
                    acceptTeams(matcher);
                }
            }
            case "rejectTeams" -> {
                if (adminRequired()) {
                    rejectTeams(matcher);
                }
            }
            
        }

    }


    private boolean adminRequired() {
        boolean hasAccess = client.getType().equals(UserType.SYSTEM_ADMIN);
        if (!hasAccess) {
            this.menu.showError("You do not have access to this section");
        }
        return hasAccess;
    }
    
}
