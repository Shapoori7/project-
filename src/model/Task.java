package model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.UUID;
import java.util.ArrayList;

public class Task {
    private final UUID id;
    private String title;
    private String description;
    private TaskPriority priority;
    private final LocalDate created;
    private LocalDate deadline;
    private final ArrayList<User> assignedUsers;
    private ArrayList<String> comments;

    public Task() {
        this.id = UUID.randomUUID();
        this.assignedUsers = new ArrayList<>();
        this.created = LocalDate.now();
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

    public LocalDate getCreated() {
        return created;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setPriority(String newPriority) {
        switch (newPriority) {
            case "LOWEST" -> this.priority = TaskPriority.LOWEST;
            case "LOW" -> this.priority = TaskPriority.LOW;
            case "HIGH" -> this.priority = TaskPriority.HIGH;
            case "HIGHEST" -> this.priority = TaskPriority.HIGHEST;
        }
    }

    public ArrayList<User> getAssignedUsers() {
        return assignedUsers;
    }

    public void removeUser(User user) {
        this.assignedUsers.remove(user);
    }

    public void addUser(User user) {
        this.assignedUsers.add(user);
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
