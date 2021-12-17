package view;

public abstract class Menu {
    public abstract void show();
    public void showResponse(String response) {
        System.out.println(response);
    };

    public void showError(String error) {
        System.err.println(error);
    }
}
