package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Team {
    String name;
    User leader;
    ArrayList<User> members;
    LocalDate created;

    public Team(User leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.created = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public LocalDate getCreated() {
        return created;
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
