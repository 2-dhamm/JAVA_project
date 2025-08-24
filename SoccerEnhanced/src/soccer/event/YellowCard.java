package soccer.event;

public class YellowCard extends GameEvent {
    private String playerName;
    private String teamName;

    public YellowCard(long time, String playerName, String teamName) {
        super(time);
        this.playerName = playerName;
        this.teamName = teamName;
        this.description = "Yellow card for " + playerName + " (" + teamName + ")";
    }

    public String getPlayerName() { return playerName; }
    public String getTeamName() { return teamName; }

    @Override
    public String getEventType() { return "YellowCard"; }

    @Override
    public String getDisplayString() {
        return "[" + getTime() + "' - " + getEventType() + "] " + description;
    }
}