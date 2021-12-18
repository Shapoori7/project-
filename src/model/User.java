package model;

import java.util.*;
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
    private ArrayList<String> logs;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.logs = new ArrayList<>();
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

    public void setType(UserType type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void newLog() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a");
        Date date = new Date();
        this.logs.add(dateFormat.format(date));
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
