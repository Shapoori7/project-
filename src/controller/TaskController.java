package controller;

import model.Task;
import model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;

public class TaskController extends MainMenuController{
    public void changeTitle(Matcher matcher) {
        String id = matcher.group(1);
        String newTitle = matcher.group(2);

        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
        task.setTitle(newTitle);
        Controller.DATA_BASE_CONTROLLER.updateTask(task);
        this.menu.showResponse("Title updated successfully!");
    }

    public void changeDescription(Matcher matcher) {
        String id = matcher.group(1);
        String newDescription = matcher.group(2);

        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
        task.setDescription(newDescription);
        Controller.DATA_BASE_CONTROLLER.updateTask(task);
        this.menu.showResponse("Description updated successfully!");
    }

    public void changePriority(Matcher matcher) {
        String id = matcher.group(1);
        String newPriority = matcher.group(2);

        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
        task.setPriority(newPriority);
        Controller.DATA_BASE_CONTROLLER.updateTask(task);
        this.menu.showResponse("Priority updated successfully!");
    }

    public void changeDeadline(Matcher matcher) {
        String id = matcher.group(1);
        String deadline = matcher.group(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'|'hh:mm");
        LocalDate newDeadline = LocalDate.parse(deadline, formatter);

        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);

        if (task.getCreated().isAfter(newDeadline)) {
            this.menu.showError("New deadline is invalid!");
        }
        else {
            task.setDeadline(newDeadline);
            Controller.DATA_BASE_CONTROLLER.updateTask(task);
            this.menu.showResponse("Deadline updated successfully!");
        }
    }

    public void removeUser(Matcher matcher) {
        String id = matcher.group(1);
        String username = matcher.group(2);

        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
        User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

        if (!task.getAssignedUsers().contains(user)) {
            this.menu.showError("There is not any user with this username " + username + " in list!");
        }
        else {
            task.removeUser(user);
            user.removeTask(task);
            Controller.DATA_BASE_CONTROLLER.updateTask(task);
            Controller.DATA_BASE_CONTROLLER.saveUser(user);
            this.menu.showResponse("User " + username + " removed successfully!");
        }
    }

    public void addUser(Matcher matcher) {
        String id = matcher.group(1);
        String username = matcher.group(2);

        Task task = Controller.DATA_BASE_CONTROLLER.findTaskById(id);
        User user = Controller.DATA_BASE_CONTROLLER.findUserByUsername(username);

        if (user == null) {
            this.menu.showError("There is not any user with this username " + username + "!");
        }
        else {
            task.addUser(user);
            user.addTask(task);
            Controller.DATA_BASE_CONTROLLER.updateTask(task);
            Controller.DATA_BASE_CONTROLLER.saveUser(user);
            this.menu.showResponse("User " + username + " added successfully!");
        }
    }
}
