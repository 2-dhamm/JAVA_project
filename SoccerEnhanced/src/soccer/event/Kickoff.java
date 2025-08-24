package soccer.event;

public class Kickoff extends GameEvent {
    private String teamWithPossession;

    public Kickoff(long time, String teamName) {
        super(time);
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty.");
        }
        this.teamWithPossession = teamName;
        this.description = "Kickoff by " + teamName;
    }

    public String getTeamWithPossession() {
        return teamWithPossession;
    }

    @Override
    public String getEventType() {
        return "Kickoff";
    }

    @Override
    public String getDisplayString() {
        return "[" + getTime() + "' - " + getEventType() + "] " + getDescription() +
                " (Possession: " + teamWithPossession + ")";
    }
}