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
        this.patterns.put("moveNext", "");
        this.patterns.put("doneOrFailed", "");
        this.patterns.put("reopen", "");


    }

    public void newBoard(Matcher matcher) {
        if (stageOneChecker()) {
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
        String boardName = matcher.group(1);
        if (this.team.loadBoard(boardName) == null) {
            this.menu.showError("There is no board with this name");
        }
        else {
            this.team.removeBoard(boardName);
        }

    }

    public void newCategory(Matcher matcher) {
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
        Board stagedBoard = this.team.getStageOneBoard();
        if (stagedBoard.isEmpty()) {
            this.menu.showError("Please make a category first");
        }
        else {
            this.team.addBoard(stagedBoard);
            this.team.setStageOneBoard(null);
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

        Category currentCategory = board.loadTaskCategory(task);
        board.changeTaskCategory(task, currentCategory, category);
    }

    public void moveNext(Matcher matcher) {
        String taskId = matcher.group(1);
        String boardName = matcher.group(2);
        Board board = this.team.loadBoard(boardName);
        Task task = board.loadTask(taskId);
        UserType type = client.getType();

        if (task == null) {
            this.menu.showError("Invalid task!");
            return;
        }

        if (!type.equals(UserType.LEADER)) {
            if (!task.getAssignedUsers().contains(client)) {
                this.menu.showError("This task is not assigned to you");
                return;
            }
        }

        Category current = board.loadTaskCategory(task);
        ArrayList<Category> categories = board.getCategories();
        int currentColumn = categories.indexOf(current);
        if (currentColumn == categories.size() - 1) {
            return;
        }

        Category next = categories.get(currentColumn + 1);
        board.changeTaskCategory(task, current, next);
    }

    public void doneOrFailed(Matcher matcher) {
        String nameOfCategory = matcher.group(1);
        String boardName = matcher.group(2);
        Board board = this.team.loadBoard(boardName);
        Category category = board.loadCategory(nameOfCategory);

        StringBuilder response = new StringBuilder();

        for (Task task: category.getTasks()) {
            response.append(task.toString());
            response.append("\n");
        }

        this.menu.showResponse(response.toString());
    }

    public void reopenTask(Matcher matcher) {
        String fullCommand = matcher.group();
        String taskId = matcher.group(1);
        String deadline, boardName, teammateName, categoryName;

        if (fullCommand.contains("--assign") && !fullCommand.contains("--category")) {
            teammateName = matcher.group(2);
            deadline = matcher.group(3);
            categoryName = "";
            boardName = matcher.group(4);
        }
        else if (fullCommand.contains("--category") && !fullCommand.contains("--assign")) {
            teammateName = "";
            deadline = matcher.group(2);
            categoryName = matcher.group(3);
            boardName = matcher.group(4);
        }
        else if (fullCommand.contains("--assign") && fullCommand.contains("--category")) {
            teammateName = matcher.group(2);
            deadline = matcher.group(3);
            categoryName = matcher.group(4);
            boardName = matcher.group(5);
        }
        else {
            teammateName = "";
            deadline = matcher.group(2);
            categoryName = "";
            boardName = matcher.group(3);
        }

        Board board = this.team.loadBoard(boardName);
        Task task = board.loadTask(taskId);
        if (task == null) {
            this.menu.showError("Invalid task");
            return;
        }

        Category failed = board.loadTaskCategory(task);
        if (!failed.getName().equals("failed")) {
            this.menu.showError("This task is not in failed section");
            return;
        }

        ArrayList<Category> categories = board.getCategories();
        if (categoryName.equals("")) {
            categoryName = categories.get(0).getName();
        }
        Category category = board.loadCategory(categoryName);
        if (category == null) {
            this.menu.showError("Invalid category");
            return;
        }

        if (!teammateName.equals("")) {
            User teammate = this.team.loadUser(teammateName);
            if (teammate == null) {
                this.menu.showError("Invalid team member");
                return;
            }
            task.addUser(teammate);
            teammate.addTask(task);
            Controller.DATA_BASE_CONTROLLER.saveUser(teammate);
        }

        task.setDeadline(LocalDate.parse(deadline));
        category.addTask(task);
        board.changeTaskCategory(task, failed, category);

        Controller.DATA_BASE_CONTROLLER.updateTeam(this.team);
        Controller.DATA_BASE_CONTROLLER.updateTask(task);
    }


    public void commandHandler(String key, Matcher matcher) {
        switch (key) {
            case "newBoard" -> {
                if (leaderRequired())
                    newBoard(matcher);
                super.commandHandler();
            }
            case "removeBoard" -> {
                if (leaderRequired())
                    removeBoard(matcher);
                super.commandHandler();
            }
            case "newCategory" -> {
                if (leaderRequired())
                    newCategory(matcher);
                super.commandHandler();
            }
            case "changeColumn" -> {
                if (leaderRequired())
                    changeColumn(matcher);
                super.commandHandler();

            }
            case "unstageBoard" -> {
                if (leaderRequired())
                    unstageBoard();
                super.commandHandler();
            }
            case "addTask" -> {
                if (leaderRequired())
                    addTask(matcher);
                super.commandHandler();
            }
            case "assignTask" -> {
                if (leaderRequired())
                    assignTask(matcher);
                super.commandHandler();
            }
            case "moveTask" -> {
                if (leaderRequired())
                    moveTask(matcher);
                super.commandHandler();
            }
            case "moveNext" -> {
                moveNext(matcher);
                super.commandHandler();
            }
            case "doneOrFailed" -> {
                if (leaderRequired())
                    doneOrFailed(matcher);
                super.commandHandler();
            }
            case "reopen" -> {
                if (leaderRequired())
                    reopenTask(matcher);
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
