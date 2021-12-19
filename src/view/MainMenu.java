package view;

public class MainMenu extends Menu{
    private final String otherMenus;
    public MainMenu() {
        super();
        this.otherMenus = """
                Profile Menu
                Team Menu
                Tasks Page
                Calendar Menu
                Notification Bar
                """;
    }

    @Override
    public void show() {
        showResponse("welcome to main menu");
        showResponse("based on your user type u have access to different parts");
        System.out.println();
        showResponse(otherMenus);
        showResponse("to enter a menu, type this command: enter menu <Menu Name>");

    }

}
