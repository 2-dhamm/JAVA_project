// soccer/gui/SoccerGUI.java
package soccer.gui;

import soccer.event.*;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class SoccerGUI extends JFrame {
    private DefaultListModel<String> eventModel;
    private JList<String> eventList;
    private List<GameEvent> events;
    private Stack<GameEvent> undoStack;

    // Fields
    private JTextField timeField, playerField, assistField;
    private JTextField playerFouledField, playerFoulingField;
    private JComponent teamField;

    private JComboBox<String> eventTypeCombo;
    private JComboBox<String> filterCombo; // تم التصحيح: تعريف فقط
    private JLabel statsLabel, timerLabel;

    // Teams
    private String team1, team2;

    // Timer
    private javax.swing.Timer gameTimer;
    private int currentMinute = 0;

    // Dynamic Panel
    private JPanel dynamicFieldsPanel;

    public SoccerGUI() {
        System.out.println("✅ 1. SoccerGUI: بدء التشغيل");

        events = new ArrayList<>();
        undoStack = new Stack<>();
        eventModel = new DefaultListModel<>();

        // إدخال أسماء الفرق
        team1 = JOptionPane.showInputDialog(this, "Enter Team 1 Name:", "Team Setup");
        if (team1 == null || team1.trim().isEmpty()) team1 = "Team A";
        System.out.println("✅ 2. Team 1: " + team1);

        team2 = JOptionPane.showInputDialog(this, "Enter Team 2 Name:", "Team Setup");
        if (team2 == null || team2.trim().isEmpty()) team2 = "Team B";
        System.out.println("✅ 3. Team 2: " + team2);

        // لا تستخدم أي شيء يعتمد على UI قبل setupUI()
        setupUI();
        loadEvents(); // حمل الأحداث بعد إنشاء القائمة
        applyFilter(); // حدّث العرض
        updateStats();

        System.out.println("✅ 4. SoccerGUI: جاهز للعرض");
    }

    private void setupUI() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("⚽ Soccer Match Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLayout(new BorderLayout());

        // ======= Top Panel: Controls =======
        JPanel topPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Match Controls"));

        // Row 1: Event Type + Time + Timer
        JPanel row1 = new JPanel(new FlowLayout());
        row1.add(new JLabel("Event:"));
        eventTypeCombo = new JComboBox<>(new String[]{
                "Goal", "YellowCard", "RedCard", "Foul", "Offside", "Kickoff"
        });
        eventTypeCombo.addActionListener(e -> updateDynamicFields());
        row1.add(eventTypeCombo);

        row1.add(new JLabel("Time:"));
        timeField = new JTextField("0", 5);
        row1.add(timeField);

        timerLabel = new JLabel("⏱️ 0'");
        timerLabel.setFont(timerLabel.getFont().deriveFont(Font.BOLD));
        row1.add(timerLabel);

        JButton startTimerBtn = new JButton("⏯️ Start Timer");
        startTimerBtn.addActionListener(e -> startTimer());
        row1.add(startTimerBtn);

        topPanel.add(row1);

        // Row 2: Dynamic Fields
        dynamicFieldsPanel = new JPanel();
        dynamicFieldsPanel.setLayout(new GridLayout(0, 2, 10, 5));
        topPanel.add(dynamicFieldsPanel);

        // ✅ استدعاء بعد التهيئة
        updateDynamicFields();

        // Row 3: Buttons
        JPanel buttonRow = new JPanel(new FlowLayout());

        JButton addButton = new JButton("➕ Add Event");
        addButton.addActionListener(this::addEvent);
        buttonRow.add(addButton);

        JButton undoButton = new JButton("↩️ Undo");
        undoButton.addActionListener(e -> undoLastEvent());
        buttonRow.add(undoButton);

        JButton deleteButton = new JButton("🗑️ Delete Selected");
        deleteButton.addActionListener(e -> deleteSelected());
        buttonRow.add(deleteButton);

        JButton saveButton = new JButton("💾 Save Events");
        saveButton.addActionListener(e -> saveEvents());
        buttonRow.add(saveButton);

        JButton loadButton = new JButton("📂 Load Events");
        loadButton.addActionListener(e -> loadEvents());
        buttonRow.add(loadButton);

        topPanel.add(buttonRow);
        add(topPanel, BorderLayout.NORTH);

        // ======= Center: Events List =======
        eventList = new JList<>(eventModel);
        eventList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(eventList), BorderLayout.CENTER);

        // ======= Bottom: Stats & Filter =======
        JPanel bottomPanel = new JPanel(new BorderLayout());

        statsLabel = new JLabel("📊 Goals: 0 | Yellow: 0 | Red: 0 | Fouls: 0 | Total: 0");
        bottomPanel.add(statsLabel, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Filter:"));

        // ✅ تم التصحيح: إنشاء filterCombo هنا
        filterCombo = new JComboBox<>(new String[]{"All", "Goals", "Cards", "Fouls", "Team: " + team1, "Team: " + team2});
        filterCombo.addActionListener(e -> applyFilter());
        filterPanel.add(filterCombo);

        bottomPanel.add(filterPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void updateDynamicFields() {
        if (dynamicFieldsPanel == null) return;
        dynamicFieldsPanel.removeAll();

        String type = (String) eventTypeCombo.getSelectedItem();

        switch (type) {
            case "Goal":
                addField("Player:", playerField = new JTextField());
                addField("Assist:", assistField = new JTextField());
                addField("Team:", teamField = comboWithTeams());
                break;
            case "YellowCard":
            case "RedCard":
            case "Offside":
                addField("Player:", playerField = new JTextField());
                addField("Team:", teamField = comboWithTeams());
                break;
            case "Foul":
                addField("Fouled:", playerFouledField = new JTextField());
                addField("Fouling:", playerFoulingField = new JTextField());
                addField("Team:", teamField = comboWithTeams());
                break;
            case "Kickoff":
                addField("Team:", teamField = comboWithTeams());
                break;
        }

        dynamicFieldsPanel.revalidate();
        dynamicFieldsPanel.repaint();
    }

    private void addField(String label, JComponent field) {
        dynamicFieldsPanel.add(new JLabel(label));
        dynamicFieldsPanel.add(field);
    }

    private JComboBox<String> comboWithTeams() {
        JComboBox<String> cb = new JComboBox<>(new String[]{team1, team2});
        return cb;
    }

    private void addEvent(ActionEvent e) {
        try {
            int time = Integer.parseInt(!timeField.getText().trim().isEmpty() ? timeField.getText().trim() : "0");
            String type = (String) eventTypeCombo.getSelectedItem();

            GameEvent event = null;
            String team = getSelectedTeam();

            switch (type) {
                case "Goal":
                    String player = getTextField(playerField);
                    String assist = getTextField(assistField);
                    if (player.isEmpty()) throw new IllegalArgumentException("Player name required.");
                    event = new Goal(time, player);
                    if (!assist.isEmpty() || team != null)
                        ((Goal) event).setDescription("Goal by " + player + " (assist: " + assist + ") - " + team);
                    break;

                case "YellowCard":
                case "RedCard":
                    player = getTextField(playerField);
                    if (player.isEmpty() || team == null) throw new IllegalArgumentException("Player and team required.");
                    if ("YellowCard".equals(type))
                        event = new YellowCard(time, player, team);
                    else
                        event = new RedCard(time, player, team);
                    break;

                case "Offside":
                    player = getTextField(playerField);
                    if (player.isEmpty() || team == null) throw new IllegalArgumentException("Player and team required.");
                    event = new Offside(time, player, team);
                    break;

                case "Foul":
                    String fouled = getTextField(playerFouledField);
                    String fouling = getTextField(playerFoulingField);
                    if (fouled.isEmpty() || fouling.isEmpty() || team == null)
                        throw new IllegalArgumentException("All fields required.");
                    event = new Foul(time, fouled, fouling, team);
                    break;

                case "Kickoff":
                    if (team == null) throw new IllegalArgumentException("Team required.");
                    event = new Kickoff(time, team);
                    break;
            }

            if (event != null) {
                events.add(event);
                undoStack.push(event);
                applyFilter();
                updateStats();
                saveEvents();
            }

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private String getSelectedTeam() {
        if (teamField instanceof JComboBox) {
            return (String) ((JComboBox<?>) teamField).getSelectedItem();
        } else if (teamField instanceof JTextField) {
            return ((JTextField) teamField).getText().trim();
        }
        return null;
    }

    private String getTextField(JTextField field) {
        return field != null ? field.getText().trim() : "";
    }

    private void deleteSelected() {
        int idx = eventList.getSelectedIndex();
        if (idx == -1) {
            showError("Select an event to delete.");
            return;
        }
        eventModel.remove(idx);
        events.removeIf(e -> e.getDisplayString().equals(eventModel.getElementAt(idx)));
        updateStats();
        saveEvents();
    }

    private void undoLastEvent() {
        if (undoStack.isEmpty()) return;
        GameEvent last = undoStack.pop();
        events.remove(last);
        updateStats();
        applyFilter();
        saveEvents();
    }

    private void saveEvents() {
        try (PrintWriter out = new PrintWriter("match_events.txt")) {
            for (GameEvent e : events) {
                out.println(e.getDisplayString());
            }
            System.out.println("✅ Events saved to match_events.txt");
        } catch (IOException e) {
            System.err.println("❌ Save failed: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "فشل الحفظ", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEvents() {
        eventModel.clear();
        events.clear();

        File file = new File("match_events.txt");
        if (!file.exists()) {
            System.out.println("⚠️ No saved events found.");
            return;
        }

        try (Scanner scan = new Scanner(file, "UTF-8")) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                if (!line.isEmpty()) {
                    eventModel.addElement(line);
                }
            }
            System.out.println("✅ Events loaded from match_events.txt");
        } catch (IOException e) {
            System.err.println("❌ Load failed: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "فشل التحميل", "Error", JOptionPane.ERROR_MESSAGE);
        }

        applyFilter(); // ✅ الآن filterCombo تم إنشاؤه
    }

    private void applyFilter() {
        if (filterCombo == null) return; // ✅ تأكد من التهيئة

        String filter = (String) filterCombo.getSelectedItem();
        eventModel.clear();

        for (GameEvent e : events) {
            String desc = e.getDisplayString();

            if ("All".equals(filter) ||
                    ("Goals".equals(filter) && e instanceof Goal) ||
                    ("Cards".equals(filter) && (e instanceof YellowCard || e instanceof RedCard)) ||
                    ("Fouls".equals(filter) && e instanceof Foul) ||
                    (filter.startsWith("Team:") && desc.contains(filter.replace("Team: ", "")))
            ) {
                eventModel.addElement(desc);
            }
        }
    }

    private void updateStats() {
        int goals = 0, yellow = 0, red = 0, fouls = 0;
        for (GameEvent e : events) {
            if (e instanceof Goal) goals++;
            else if (e instanceof YellowCard) yellow++;
            else if (e instanceof RedCard) red++;
            else if (e instanceof Foul) fouls++;
        }
        statsLabel.setText(String.format("📊 Goals: %d | Yellow: %d | Red: %d | Fouls: %d | Total: %d",
                goals, yellow, red, fouls, events.size()));
    }

    private void startTimer() {
        if (gameTimer != null && gameTimer.isRunning()) return;
        gameTimer = new javax.swing.Timer(1000, e -> {
            currentMinute++;
            timerLabel.setText("⏱️ " + currentMinute + "'");
            timeField.setText(String.valueOf(currentMinute));
        });
        gameTimer.start();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "❌ Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new SoccerGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "حدث خطأ: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}