package controller;

public class Main {
    public static void main(String[] args) {
        BaseController baseController = new BaseController();
        Controller.init();
        baseController.showMenu("WelcomeMenu");

    }
}

/* Todos:
    - handle user, task, team in database when one of them is changed
    - we need a method that checks which tasks are failed and move them to failed category
    - ask to know what does suspending a member do? is it removing all assigned tasks to him?

 */