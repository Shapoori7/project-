package model;

import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.ArrayList;

public class User implements Serializable {
    private String fullName;
    private String username;
    private String password;
    private final String email;
    private UserType type;
    private int score;
    private Date birthday;
    private final ArrayList<String> logs;
    private ArrayList<Task> assignedTasks;
    private ArrayList<Team> teams;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.logs = new ArrayList<>();
        this.assignedTasks = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserType getType() {
        return type;
    }

    public ArrayList<String> getLogs() {
        return logs;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public int getScore() {
        return score;
    }

    public void newLog() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a");
        Date date = new Date();
        this.logs.add(dateFormat.format(date));
    }

    public void addTask(Task task) {
        this.assignedTasks.add(task);
    }

    public void removeTask(Task task) {
        this.assignedTasks.remove(task);
    }

    public ArrayList<Task> getAssignedTasks() {
        return assignedTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", score=" + score +
                '}';
    }
}
