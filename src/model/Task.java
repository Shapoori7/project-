package model;

import java.util.*;
import java.util.UUID;
import java.util.Date;
import java.util.ArrayList;
import User;
import TaskPriority;

public class Task {
    private UUID id;
    private String title;
    private String description;
    private TaskPriority priority;
    private Date created;
    private Date deadline;
    private ArrayList<User> assignedUsers;
    private ArrayList<String> comments;

    public Task() {
        this.id = UUID.randomUUID();
        this.assignedUsers = new ArrayList<>();
        this.created = new Date();
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'|'hh:mm");
        return "Task{" +
                "Title='" + title + '\'' +
                ", Description='" + description + '\'' +
                ", Priority=" + priority +
                ", Date and time of creation='" + dateFormat.format(created) + '\'' +
                ", Date and time of deadline=" + dateFormat.format(deadline) +
                ", Assigned users=\n" + usersToString() +
                '}';
    }

    private String usersToString() {
        String usres = "";
        for (User user: assignedUsers) {
            users.join(user.getUsername());
        }
        return users;
    }

}