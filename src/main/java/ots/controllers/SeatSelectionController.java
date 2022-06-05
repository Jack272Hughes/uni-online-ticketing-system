package ots.controllers;

import ots.database.DatabaseController;
import ots.models.Event;
import ots.models.seat.RowMap;
import ots.models.seat.Seat;
import ots.models.seat.SeatState;
import ots.utils.SeatDisplay;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SeatSelectionController {
    private static final int MAXIMUM_SELECTED_SEATS = 5;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern isPossibleSeat = Pattern.compile("^[A-H][1-8]$");

    private final DatabaseController databaseController;

    public SeatSelectionController(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }

    public List<Seat> startSeatSelection(Event event) {
        List<Seat> selectedSeats = new ArrayList<>();

        String choice = "";
        while (!"purchase".equalsIgnoreCase(choice) && !"exit".equalsIgnoreCase(choice)) {
            RowMap seats = databaseController.getSeats().getOrDefault(event.getId(), RowMap.empty());
            new SeatDisplay(seats, selectedSeats).display();
            System.out.println("Selected seats: " + selectedSeats.stream().map(Seat::getLocation).collect(Collectors.joining(", ")));
            System.out.println("Please select a row and column (e.g. A1) or enter 'purchase' or 'exit':");
            choice = scanner.nextLine().strip();

            if ("purchase".equalsIgnoreCase(choice) || "exit".equalsIgnoreCase(choice)) {
                continue;
            }

            Optional<Seat> optionalChosenSeat = validateChoice(event, choice);

            if (optionalChosenSeat.isEmpty()) {
                System.out.println(String.format("The entered value '%s' is not a valid seat", choice));
                continue;
            }

            Seat chosenSeat = optionalChosenSeat.get();
            if (chosenSeat.getState().equals(SeatState.HOLD) && selectedSeats.stream().anyMatch(seat -> seat.equals(chosenSeat))) {
                databaseController.updateSeatState(event, chosenSeat, SeatState.AVAILABLE);
                selectedSeats.removeIf(seat -> seat.equals(chosenSeat));
                System.out.println(String.format("The seat at '%s' has been unselected", choice));
            } else if (chosenSeat.getState().equals(SeatState.AVAILABLE)) {
                if (selectedSeats.size() >= MAXIMUM_SELECTED_SEATS) {
                    System.out.println(String.format("You have reached the maximum number of %s selected seats", MAXIMUM_SELECTED_SEATS));
                } else {
                    databaseController.updateSeatState(event, chosenSeat, SeatState.HOLD);
                    selectedSeats.add(chosenSeat);
                    System.out.println(String.format("The seat at '%s' has been selected", choice));
                }
            } else {
                System.out.println(String.format("The seat at '%s' is currently not available", choice));
            }
        }

        if ("exit".equalsIgnoreCase(choice)) {
            selectedSeats.forEach(seat -> databaseController.updateSeatState(event, seat, SeatState.AVAILABLE));
            return Collections.emptyList();
        }

        return selectedSeats;
    }

    private Optional<Seat> validateChoice(Event event, String choice) {
        if (!isPossibleSeat.matcher(choice).find()) {
            return Optional.empty();
        }

        String[] rowColumnPair = choice.split("");
        String row = rowColumnPair[0];
        int column = Integer.parseInt(rowColumnPair[1]);

        Seat seat = databaseController.getSeat(event, row, column);
        return Optional.of(seat);
    }
}
