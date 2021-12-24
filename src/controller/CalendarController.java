package controller;

import model.Task;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.time.temporal.ChronoUnit;

public class CalendarController extends MainMenuController{
    private final HashMap<String, String> patterns;

    public CalendarController() {
        super();

        this.patterns = new HashMap<>();
        this.patterns.put("showDeadlines", "calendar --show deadlines");
    }

    public void showDeadlines() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'|'hh:mm");
        StringBuilder response = new StringBuilder();
        ArrayList<LocalDate> deadlines = new ArrayList<>();
        for (Task task: client.getAssignedTasks()) {
            deadlines.add(task.getDeadline());
        }

        deadlines.sort(LocalDate::compareTo);
        LocalDate now = LocalDate.now();

        for (LocalDate deadline: deadlines) {
            long numberOfDays = numberOfDaysBetween(now, deadline);
            if (numberOfDays > 0) {
                response.append(generateResponse(dateFormat.format(deadline), numberOfDays));
                response.append("\n");
            }
        }

        if (response.isEmpty()) {
            this.menu.showError("no deadlines");
        }
        else {
            this.menu.showResponse(response.toString());
        }

    }

    private String generateResponse(String date, long remainingDays) {
        String startPart;
        if (remainingDays < 4) {
            startPart = "***";
        }
        else if (remainingDays <= 10) {
            startPart = "**";
        }
        else {
            startPart = "*";
        }

        return startPart + date + "__remaining days: " + remainingDays;
    }

    private long numberOfDaysBetween(LocalDate date1, LocalDate date2) {
        return ChronoUnit.DAYS.between(date1, date2);
    }

    public void commandHandler(String key) {
        if (key.equals("showDeadlines")) {
            showDeadlines();
        }

    }

    public Set<String> getPatternsNames() {
        return this.patterns.keySet();
    }

    public HashMap<String, String> getPatterns() {
        return patterns;
    }
}
