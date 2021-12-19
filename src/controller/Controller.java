package controller;

import java.util.HashMap;
import java.util.Scanner;

public interface Controller {
    Scanner INPUT = new Scanner(System.in).useDelimiter("\n");
    DataBaseController DATA_BASE_CONTROLLER = new DataBaseController();
    HashMap<String, BaseController> CONTROLLERS = new HashMap<>();
    // controllers with no special view
    ProfileController PROFILE = new ProfileController();
    TaskController TASK = new TaskController();

    static void init() {
        CONTROLLERS.put("WelcomeMenu", new AuthController());
        CONTROLLERS.put("MainMenu", new MainMenuController());
    }

}
