package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Team implements Serializable {
    String name;
    User leader;
    ArrayList<User> members;
    LocalDate created;
    ArrayList<Task> tasks;
    ArrayList<Message> messages;
    ArrayList<Board> boards;
    Board stageOneBoard;

    public Team(User leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.created = LocalDate.now();
        this.tasks = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.boards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public LocalDate getCreated() {
        return created;
    }

    public Board getStageOneBoard() {
        return stageOneBoard;
    }

    public void addBoard(Board board) {
        this.stageOneBoard = board;
    }

    public void removeBoard(String boardName) {
        this.boards.removeIf(board -> board.getName().equals(boardName));
        if (stageOneBoard.getName().equals(boardName)) {
            this.stageOneBoard = null;
        }
    }

    public Board loadBoard(String name) {
        if (this.stageOneBoard.getName().equals(name)) {
            return this.stageOneBoard;
        }

        for (Board board: this.boards) {
            if (Board.getName().equals(name)) {
                return board;
            }
        }

        return null;
    }

    public void updateBoard(Board board) {
        if (this.stageOneBoard.getName().equals(board.getName())) {
            this.stageOneBoard = board;
            return;
        }

        this.boards.removeIf(board -> board.getName().equals(board.getName()));
        this.boards.add(board);
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
        if (this.tasks.size() == 0) {
            return "no task yet";
        }
        this.tasks.sort(Comparator.comparing(Task::getTitle));
        this.tasks.sort(Comparator.comparing(Task::getCompleted));

        StringBuilder roadmap = new StringBuilder();

        for (Task task: this.tasks) {
            roadmap.append(task.getTitle()).append(" : ");
            roadmap.append(task.getCompleted()).append(" % done").append("\n");
        }

        return roadmap.toString();
    }

    public String generateChatroom() {
        if (this.messages.size() == 0) {
            return "no message yet";
        }
        this.messages.sort(Comparator.comparing(Message::getDate));

        StringBuilder chatroom = new StringBuilder();
        for (Message message: this.messages) {
            chatroom.append(message.toString()).append("\n");
        }

        return chatroom.toString();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public String getTasksToString() {
        if (this.tasks.size() == 0) {
            return "no task yet";
        }
        Comparator priorityComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TaskPriority priority1 = ((Task)o1).getPriority();
                TaskPriority priority2 = ((Task)o2).getPriority();

                if (priority1.equals(priority2)) {
                    return 1;
                }
                else if (priority1.equals(TaskPriority.HIGHEST)) {
                    return 1;
                }
                else if (priority1.equals(TaskPriority.HIGH) && !priority2.equals(TaskPriority.HIGHEST)) {
                    return 1;
                }
                else if (priority1.equals(TaskPriority.LOW) && priority2.equals(TaskPriority.LOWEST)) {
                    return 1;
                }

                return 0;
            }
        };

        this.tasks.sort(Comparator.comparing(Task::getCreated));
        this.tasks.sort(priorityComparator);

        StringBuilder tasksString = new StringBuilder();
        for (Task task: this.tasks) {
            tasksString.append(task.toString()).append("\n");
        }

        return tasksString.toString();
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
