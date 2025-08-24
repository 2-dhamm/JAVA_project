package soccer.event;

public class Goal extends GameEvent {
    private String playerName;

    // Constructor
    public Goal(long time, String playerName) {
        super(time);
        this.playerName = playerName;
        this.description = "Goal by " + playerName;
    }

    // Getter
    public String getPlayerName() {
        return playerName;
    }

    // ✅ الطريقة الجديدة: set description
    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    @Override
    public String getEventType() {
        return "Goal";
    }

    // اختياري: لو عايز تعدل getDisplayString
    @Override
    public String getDisplayString() {
        return "[" + getTime() + "' - " + getEventType() + "] " + getDescription();
    }
}