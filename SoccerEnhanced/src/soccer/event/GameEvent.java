package soccer.event;

import soccer.util.IDisplayDataItem;

public abstract class GameEvent implements IDisplayDataItem {
    protected long eventTime;
    protected String description;

    public GameEvent(long time) {
        if (time < 0) throw new IllegalArgumentException("Event time cannot be negative.");
        this.eventTime = time;
        this.description = "Game event";
    }

    public long getTime() {
        return eventTime;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getEventType();

    @Override
    public String getDisplayString() {
        return "[" + eventTime + "' - " + getEventType() + "] " + description;
    }
}