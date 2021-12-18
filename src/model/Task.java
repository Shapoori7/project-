package model;

import java.util.UUID;
import java.util.Date;
import java.util.ArrayList;

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
    }

}