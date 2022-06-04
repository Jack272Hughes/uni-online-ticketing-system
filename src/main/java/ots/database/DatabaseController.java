package ots.database;

import ots.models.*;
import ots.models.seat.RowMap;
import ots.models.seat.Seat;
import ots.models.seat.SeatState;

import java.util.List;
import java.util.Map;

public interface DatabaseController {
    User getUser(String email);
    List<Event> getEvents();
    void createTicket(Ticket ticket);
    List<Ticket> getTickets();
    Map<Integer, RowMap> getSeats();
    Seat getSeat(Event event, String row, int column);
    void updateSeatState(Event event, Seat seat, SeatState state);
}
