package ots.controllers;

import ots.database.DatabaseController;
import ots.models.Event;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class EventController {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern isNumber = Pattern.compile("^\\d+$");

    private final DatabaseController databaseController;

    public EventController(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }

    public Event startEventSelection() {
        List<Event> events = databaseController.getEvents();

        System.out.println();
        events.forEach(event -> System.out.println(String.format("%s: %s", event.getId(), event.getName())));

        Optional<Event> chosenEvent = Optional.empty();
        boolean shouldExit = false;
        while (!shouldExit && chosenEvent.isEmpty()) {
            System.out.println("Please select a valid event id or type 'exit':");
            String choice = scanner.nextLine().strip();

            if ("exit".equalsIgnoreCase(choice)) {
                shouldExit = true;
            } else {
                chosenEvent = getEventById(events, choice);
            }
        }

        if (shouldExit) {
            return null;
        }

        return chosenEvent.get();
    }

    private Optional<Event> getEventById(List<Event> events, String input) {
        if (!isNumber.matcher(input).find()) return Optional.empty();

        int id = Integer.parseInt(input);
        return events.stream().filter(event -> event.getId() == id).findFirst();
    }
}
