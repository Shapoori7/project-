package controller;

import model.Task;
import model.TaskPriority;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public interface Controller {
    Scanner INPUT = new Scanner(System.in).useDelimiter("\n");
    DataBaseController DATA_BASE_CONTROLLER = new DataBaseController();
    HashMap<String, BaseController> CONTROLLERS = new HashMap<>();
    // controllers with no special view
    ProfileController PROFILE = new ProfileController();
    TaskController TASK = new TaskController();
    BoardController BOARD = new BoardController();
    CalendarController CALENDAR = new CalendarController();

    Comparator PRIORITY_COMPARATOR = new Comparator() {
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

    static void init() {
        CONTROLLERS.put("WelcomeMenu", new AuthController());
        CONTROLLERS.put("MainMenu", new MainMenuController());
        CONTROLLERS.put("TeamMenu", new TeamMenuController());
    }

}
