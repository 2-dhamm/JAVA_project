package soccer.util;

import java.util.Comparator;

public class Team implements Comparable<Team>, IDisplayDataItem {
    private String name;
    private int points;

    public Team(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public int compareTo(Team other) {
        return Integer.compare(other.points, this.points); // descending order
    }

    @Override
    public String getDisplayString() {
        return "Team: " + name + " | Points: " + points;
    }

    // For sorting by name
    public static Comparator<Team> byName() {
        return (t1, t2) -> t1.name.compareTo(t2.name);
    }
}