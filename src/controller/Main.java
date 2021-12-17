package controller;

public class Main {
    public static void main(String[] args) {
        BaseController baseController = new BaseController();
        Controller.init();
        baseController.showMenu("WelcomeMenu");

    }
}
