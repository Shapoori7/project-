package view;

public class WelcomeMenu extends Menu {
    private final String message;
    private String version;

    public WelcomeMenu() {
        this.message = "welcome to our program";
        this.version = "0.0";
    }

    @Override
    public void show() {
        System.out.println(this.message);
        System.out.println(this.version);
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
