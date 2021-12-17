package view;

public class MainMenu extends Menu{
    private String otherMenus;
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
        System.out.println("welcome to main menu");
        System.out.println("based on your user type u have access to different parts");
        System.out.println();
        System.out.println(otherMenus);
        System.out.println("to enter a menu, type this command: enter menu <Menu Name>");

    }

}
