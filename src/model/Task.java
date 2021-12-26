package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.ArrayList;

public class Task implements Serializable {
    private final UUID id;
    private String title;
    private String description;
    private TaskPriority priority;
    private final LocalDate created;
    private LocalDate deadline;
    private final ArrayList<User> assignedUsers;
    private ArrayList<String> comments;
    private int completed;
    private final String nameOfTeam;

    public Task(String nameOfTeam, String title, LocalDate created, LocalDate deadline) {
        this.nameOfTeam = nameOfTeam;
        this.title = title;
        this.id = UUID.randomUUID();
        this.assignedUsers = new ArrayList<>();
        this.created = Objects.requireNonNullElseGet(created, LocalDate::now);
        this.completed = 0;
        this.deadline = deadline;
    }

    public String getId() {
        return id.toString();
    }

    public String getTitle() {
        return title;
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

    public int getCompleted() {
        return completed;
    }

    public TaskPriority getPriority() {
        return priority;
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
                "title : " + title +
                ",id : " + id +
                ", description='" + description +
                ", Priority=" + priority +
                ", creation date : " + dateFormat.format(created) + '\'' +
                ", deadline : " + dateFormat.format(deadline) +
                ", assign to : " + usersToString() +
                '}';
    }

    public String usersToString() {
        assignedUsers.sort(Comparator.comparing(User::getUsername));
        StringBuilder users = new StringBuilder();
        users.append("[");
        for (User user: assignedUsers) {
            users.append(user.getUsername()).append(", ");
        }
        users.append("]");
        return users.toString();
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getNameOfTeam() {
        return nameOfTeam;
    }
}
