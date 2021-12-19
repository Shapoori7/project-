package view;

public class TeamMenu extends Menu{
    private final String teamMenuParts;

    public TeamMenu() {
        super();
        this.teamMenuParts = """
                ScoreBoard
                Road Map
                Chat Room
                Tasks
                """;
    }

    @Override
    public void show() {
        showResponse("welcome to the team menu");
        showResponse("here are available parts u can visit:");
        showResponse(teamMenuParts);
    }

}
