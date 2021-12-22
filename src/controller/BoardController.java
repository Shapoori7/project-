package controller;

import model.Board;
import model.UserType;

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
            if (!this.team.boardExists(boardName)) {
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
            if (this.team.getStageOneBoard != null && !this.team.getStageOneBoard.getName().equals(boardName)) {
                this.menu.showError("Please finish creating the board first");
            }
            else {
                Board board = this.team.loadBoard(boardName);
                if (board.categoryExists(categoryName)) {
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
        Task task = Controller.DATA_BASE_CONTROLLER.loadTask(taskId);
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

        board.getCategories().get(0).add(task);
        this.team.updateBoard(board);
        Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);

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
