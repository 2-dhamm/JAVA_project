package soccer.event;

public class RedCard extends GameEvent {
    private String playerName;
    private String teamName;

    public RedCard(long time, String playerName, String teamName) {
        super(time);
        this.playerName = playerName;
        this.teamName = teamName;
        this.description = "Red card for " + playerName + " (" + teamName + ")";
    }

    public String getPlayerName() { return playerName; }
    public String getTeamName() { return teamName; }

    @Override
    public String getEventType() { return "RedCard"; }

    @Override
    public String getDisplayString() {
        return "[" + getTime() + "' - " + getEventType() + "] " + description;
    }
}