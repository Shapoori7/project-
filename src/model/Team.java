package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Team {
    String name;
    User leader;
    ArrayList<User> members;
    LocalDate created;
    ArrayList<Task> tasks;

    public Team(User leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.created = LocalDate.now();
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public LocalDate getCreated() {
        return created;
    }

    public String generateScoreboard() {
        ArrayList<User> all = new ArrayList<>(this.members);
        all.add(this.leader);
        all.sort(Comparator.comparing(User::getFullName));
        all.sort(Comparator.comparing(User::getScore));

        StringBuilder scoreboard = new StringBuilder();

        for (int i = 0; i < all.size(); i++) {
            User user = all.get(i);
            scoreboard.append(i + 1).append("\t");
            scoreboard.append(user.getFullName()).append(" : ");
            scoreboard.append(user.getScore()).append("\n");
        }

        return scoreboard.toString();
    }

    public String generateRoadmap() {
        this.tasks.sort(Comparator.comparing(Task::getTitle));
        this.tasks.sort(Comparator.comparing(Task::getCompleted));

        StringBuilder roadmap = new StringBuilder();

        for (Task task: this.tasks) {
            roadmap.append(task.getTitle()).append(" : ");
            roadmap.append(task.getCompleted()).append(" % done").append("\n");
        }

        return roadmap.toString();
    }

    public String toString(User user) {
        StringBuilder teamInfo = new StringBuilder();
        teamInfo.append(this.name).append("\n");
        teamInfo.append("leader: ").append(this.leader.getFullName()).append("\n");
        teamInfo.append("members:").append("\n");

        if (!user.equals(this.leader)) {
            teamInfo.append(user.getFullName()).append("\n");
        }

        members.sort(Comparator.comparing(User::getFullName));

        for (User member: members) {
            if (!member.equals(user)) {
                teamInfo.append(member.getFullName()).append("\n");
            }
        }

        return teamInfo.toString();
    }

}
