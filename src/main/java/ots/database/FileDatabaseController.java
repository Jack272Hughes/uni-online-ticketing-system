package ots.database;

import ots.models.*;
import ots.models.seat.ColumnMap;
import ots.models.seat.RowMap;
import ots.models.seat.Seat;
import ots.models.seat.SeatState;
import ots.utils.FileParser;

import java.util.*;

public class FileDatabaseController implements DatabaseController {
    private final static String USERS = "users";
    private final static String EVENTS = "events";
    private final static String TICKETS = "tickets";
    private final static String SEATS = "seats";

    private final FileParser fileParser;

    public FileDatabaseController(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public User getUser(String email) {
        List<User> users = fileParser.parseFileAsList(USERS, User.class);
        Optional<User> user = users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();

        return user.orElse(null);
    }

    public List<Event> getEvents() {
        return fileParser.parseFileAsList(EVENTS, Event.class);
    }

    @Override
    public void createTicket(Ticket ticket) {
        List<Ticket> allTickets = getTickets();
        allTickets.add(ticket);
        fileParser.writeToFile(TICKETS, allTickets);
    }

    @Override
    public List<Ticket> getTickets() {
        return fileParser.parseFileAsList(TICKETS, Ticket.class);
    }

    @Override
    public Map<Integer, RowMap> getSeats() {
        return fileParser.parseFileAsMap(SEATS, Integer.class, RowMap.class);
    }

    @Override
    public Seat getSeat(Event event, String row, int column) {
        SeatState seatState = getSeats().getOrDefault(event.getId(), RowMap.empty()).getColumnMap(row).getSeatState(column);
        return new Seat(row, column, seatState);
    }

    @Override
    public void updateSeatState(Event event, Seat seat, SeatState state) {
        Map<Integer, RowMap> allSeats = getSeats();
        RowMap seats = allSeats.getOrDefault(event.getId(), RowMap.empty());
        ColumnMap columnMap = seats.getColumnMap(seat.getRow());
        columnMap.put(seat.getColumn(), state);
        seats.put(seat.getRow(), columnMap);
        allSeats.put(event.getId(), seats);
        fileParser.writeToFile(SEATS, allSeats);
    }
}
