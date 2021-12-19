package model;

import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.Date;
import java.util.ArrayList;
import model.User;
import model.TaskPriority;

public class Task {
    private UUID id;
    private String title;
    private String description;
    private TaskPriority priority;
    private final Date created;
    private Date deadline;
    private ArrayList<User> assignedUsers;
    private ArrayList<String> comments;

    public Task() {
        this.id = UUID.randomUUID();
        this.assignedUsers = new ArrayList<>();
        this.created = new Date();
    }

    public String getId() {
        return id.toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(String newPriority) {
        switch (newPriority) {
            case "LOWEST" -> this.priority = TaskPriority.LOWEST;
            case "LOW" -> this.priority = TaskPriority.LOW;
            case "HIGH" -> this.priority = TaskPriority.HIGH;
            case "HIGHEST" -> this.priority = TaskPriority.HIGHEST;
        }
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
        StringBuilder users = new StringBuilder();
        for (User user: assignedUsers) {
            users.append(user.getUsername());
        }
        return users.toString();
    }

}
