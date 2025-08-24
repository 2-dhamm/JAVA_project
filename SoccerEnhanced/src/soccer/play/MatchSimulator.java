package soccer.play;

import soccer.event.*;
import soccer.util.Calculator;
import soccer.util.ShoppingCart;
import soccer.util.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchSimulator {
    public static void main(String[] args) {
        System.out.println("⚽ Soccer Match Simulator\n");

        // === Part 1: Test Game Events ===
        List<GameEvent> events = new ArrayList<>();

        try {
            Kickoff kickoff = new Kickoff(0, "Barcelona");
            Goal goal = new Goal(23, "Messi");

            events.add(kickoff);
            events.add(goal);

            System.out.println("📋 Match Events:");
            for (GameEvent e : events) {
                System.out.println(e.getDisplayString());
            }

            // Test invalid kickoff
            // new Kickoff(0, ""); // Uncomment to test exception

        } catch (IllegalArgumentException e) {
            System.err.println("Event Error: " + e.getMessage());
        }

        System.out.println();

        // === Part 2: Test Team Sorting (Comparable) ===
        List<Team> teams = new ArrayList<>();
        teams.add(new Team("Real Madrid", 88));
        teams.add(new Team("Atletico", 76));
        teams.add(new Team("Barcelona", 85));

        System.out.println("🏆 Teams before sorting:");
        for (Team t : teams) {
            System.out.println(t.getDisplayString());
        }

        Collections.sort(teams);

        System.out.println("\n🏆 Teams sorted by points (desc):");
        for (Team t : teams) {
            System.out.println(t.getDisplayString());
        }

        // Sort by name
        teams.sort(Team.byName());
        System.out.println("\n🏆 Teams sorted by name:");
        for (Team t : teams) {
            System.out.println(t.getDisplayString());
        }

        // === Part 3: Test Exception Handling ===
        System.out.println("\n🧯 Testing Exception Handling...");

        // Calculator
        Calculator calc = new Calculator();
        try {
            System.out.println("10 / 2 = " + calc.divide(10, 2));
            System.out.println("10 / 0 = " + calc.divide(10, 0)); // Throws
        } catch (IllegalArgumentException e) {
            System.err.println("Calculator Error: " + e.getMessage());
        }

        // ShoppingCart
        ShoppingCart cart = new ShoppingCart();
        try {
            cart.addItem("Ball", 2);
            cart.addItem("", 1); // Throws
        } catch (IllegalArgumentException e) {
            System.err.println("ShoppingCart Error: " + e.getMessage());
        }

        try {
            String[] arr = {"A", "B"};
            System.out.println(arr[5]); // ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Array Error: Index out of bounds.");
        }
    }
}