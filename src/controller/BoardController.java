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
