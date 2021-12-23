package controller;

import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class BoardController extends TeamMenuController{
    private final HashMap<String, String> patterns;

    public BoardController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("newBoard", "");
        this.patterns.put("removeBoard", "");
        this.patterns.put("newCategory", "");
        this.patterns.put("changeColumn", "");
        this.patterns.put("unstageBoard", "");
        this.patterns.put("addTask", "");
        this.patterns.put("assignTask", "");
        this.patterns.put("moveTask", "");


    }

    public void newBoard(Matcher matcher) {
        if (leaderRequired() && stageOneChecker()) {
            String boardName = matcher.group(1);
            if (this.team.loadBoard(boardName) != null) {
                this.menu.showError("There is already a board with this name");
            }
            else {
                Board board = new Board(boardName);
                this.team.addBoard(board);
                Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);
            }

        }

    }

    public void removeBoard(Matcher matcher) {
        if (leaderRequired()) {
            String boardName = matcher.group(1);
            if (this.team.loadBoard(boardName) == null) {
                this.menu.showError("There is no board with this name");
            }
            else {
                this.team.removeBoard(boardName);
            }

        }

    }

    public void newCategory(Matcher matcher) {
        if (leaderRequired()) {
            String categoryName = matcher.group(1);
            String boardName = matcher.group(2);
            if (this.team.getStageOneBoard() != null && !this.team.getStageOneBoard().getName().equals(boardName)) {
                this.menu.showError("Please finish creating the board first");
            }
            else {
                Board board = this.team.loadBoard(boardName);
                if (board.loadCategory(categoryName) != null) {
                    this.menu.showError("The name is already taken for a category!");
                }
                else {
                    board.addCategory(categoryName);
                    this.team.updateBoard(board);
                    Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);
                }
            }

        }
    

    }

    public void changeColumn(Matcher matcher) {
        String categoryName = matcher.group(1);
        int column = Integer.parseInt(matcher.group(2));
        String boardName = matcher.group(3);

        Board board = this.team.loadBoard(boardName);
        ArrayList<Category> categories = board.getCategories();
        if (categories.size() <= column || column < 0) {
            this.menu.showError("wrong column!");
            return;
        }
        Category category = board.loadCategory(categoryName);
        categories.remove(category);
        categories.add(column, category);

        for (int index = 0 ; index < categories.size() ; index++) {
            categories.get(index).setColumn(index);
        }

        board.setCategories(categories);
        this.team.updateBoard(board);
        Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);

    }

    public void unstageBoard() {
        if (leaderRequired()) {
            Board stagedBoard = this.team.getStageOneBoard();
            if (stagedBoard.isEmpty()) {
                this.menu.showError("Please make a category first");
            }
            else {
                this.team.addBoard(stagedBoard);
                this.team.setStageOneBoard(null);
            }
        }
        

    }

    public void addTask(Matcher matcher) {
        String taskId = matcher.group(1);
        String boardName = matcher.group(1);
        Board board = this.team.loadBoard(boardName);

        if (board.loadTask(taskId) != null) {
            this.menu.showError("This task has already been added to this board");
            return;
        }
        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(taskId);
        if (task == null) {
            this.menu.showError("Invalid task id!");
            return;
        }

        if (task.getDeadline().isBefore(LocalDate.now())) {
            this.menu.showError("The deadline of this task has already passed");
            return;
        }

        if (task.getAssignedUsers().size() == 0) {
            this.menu.showError("Please assign this task to someone first");
            return;
        }

        board.getCategories().get(0).addTask(task);
        this.team.updateBoard(board);
        Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);

    }

    public void assignTask(Matcher matcher) {
        String teammateName = matcher.group(1);
        String taskId = matcher.group(2);
        String boardName = matcher.group(3);

        Board board = this.team.loadBoard(boardName);
        User teammate = this.team.loadUser(teammateName);
        Task task = board.loadTask(taskId);

        if (task == null) {
            this.menu.showError("Invalid task id");
            return;
        }
        if (teammate == null) {
            this.menu.showError("Invalid teammate");
            return;
        }
        if (board.loadCategory("done").loadTask(taskId) != null) {
            this.menu.showError("This task has already finished");
            return;
        }

        teammate.addTask(task);
        task.addUser(teammate);
        board.updateTask(task);
        this.team.updateUser(teammate);
        this.team.updateBoard(board);

        Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);
        Controller.DATA_BASE_CONTROLLER.updateTask(task);
        Controller.DATA_BASE_CONTROLLER.saveUser(teammate);
    }

    public void moveTask(Matcher matcher) {
        String categoryName = matcher.group(1);
        String taskId = matcher.group(2);
        String boardName = matcher.group(3);

        Board board = this.team.loadBoard(boardName);
        Task task = board.loadTask(taskId);
        Category category = board.loadCategory(categoryName);

        if (task == null) {
            this.menu.showError("There is no task with given information");
            return;
        }
        if (category == null) {
            this.menu.showError("Invalid category");
            return;
        }

        board.changeTaskCategory(task, category);

    }


    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "newBoard" -> {
                newBoard(matcher);
                super.commandHandler();
            }
            case "removeBoard" -> {
                removeBoard(matcher);
                super.commandHandler();
            }
            case "newCategory" -> {
                newCategory(matcher);
                super.commandHandler();
            }
            case "changeColumn" -> {
                changeColumn(matcher);
                super.commandHandler();

            }
            case "unstageBoard" -> {
                unstageBoard();
                super.commandHandler();
            }
            case "addTask" -> {
                addTask(matcher);
                super.commandHandler();
            }
            case "assignTask" -> {
                assignTask(matcher);
                super.commandHandler();
            }
            case "moveTask" -> {
                moveTask(matcher);
                super.commandHandler();
            }
            

        }

    }


    private boolean leaderRequired() {
        boolean hasAccess = client.getType().equals(UserType.LEADER);
        if (!hasAccess) {
            this.menu.showError("You do not have the permission to do this action!");
        }
        return hasAccess;
    }

    private boolean stageOneChecker() {
        if (this.team.getStageOneBoard() != null) {
            this.menu.showError("Please finish creating the board first");
            return false;
        }
        return true;
    }

}
