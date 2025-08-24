package soccer.event;

public class Offside extends GameEvent {
    private String playerName;
    private String teamName;

    public Offside(long time, String playerName, String teamName) {
        super(time);
        this.playerName = playerName;
        this.teamName = teamName;
        this.description = playerName + " offside (" + teamName + ")";
    }

    @Override
    public String getEventType() { return "Offside"; }

    @Override
    public String getDisplayString() {
        return "[" + getTime() + "' - " + getEventType() + "] " + description;
    }
}