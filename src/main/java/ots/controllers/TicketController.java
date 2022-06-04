package ots.controllers;

import ots.database.DatabaseController;
import ots.models.Event;
import ots.models.Ticket;
import ots.models.User;
import ots.models.seat.Seat;
import ots.models.seat.SeatState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TicketController {
    private static final double price = 12.99;

    private final DatabaseController databaseController;

    public TicketController(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }

    public void purchaseTickets(Event event, User user, List<Seat> selectedSeats) {
        selectedSeats.forEach(seat -> {
            Ticket ticket = new Ticket(event.getId(), user.getEmail(), price, seat.getLocation());
            databaseController.createTicket(ticket);
            databaseController.updateSeatState(event, seat, SeatState.RESERVED);
        });
    }

    public void displayTickets(User user) {
        List<Ticket> tickets = databaseController.getTickets();
        Map<Integer, String> events = new HashMap<>();
        databaseController.getEvents().forEach(event -> events.put(event.getId(), event.getName()));

        AtomicBoolean foundNoTickets = new AtomicBoolean(true);
        tickets.forEach(ticket -> {
            if (ticket.getUserEmail().equals(user.getEmail())) {
                foundNoTickets.set(false);
                System.out.println(String.format("£%s ticket for %s on row %s", ticket.getPrice(), events.get(ticket.getEventId()), ticket.getLocation()));
            }
        });

        if (foundNoTickets.get()) {
            System.out.println("You have not purchased any tickets yet");
        }
    }
}
