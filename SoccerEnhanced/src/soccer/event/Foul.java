package soccer.event;

public class Foul extends GameEvent {
    private String playerFouled;
    private String playerFouling;
    private String teamName;

    public Foul(long time, String playerFouled, String playerFouling, String teamName) {
        super(time);
        this.playerFouled = playerFouled;
        this.playerFouling = playerFouling;
        this.teamName = teamName;
        this.description = playerFouling + " fouled " + playerFouled + " (" + teamName + ")";
    }

    @Override
    public String getEventType() { return "Foul"; }

    @Override
    public String getDisplayString() {
        return "[" + getTime() + "' - " + getEventType() + "] " + description;
    }
}