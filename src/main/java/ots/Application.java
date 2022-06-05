package ots;

import ots.controllers.EventController;
import ots.controllers.LoginController;
import ots.controllers.SeatSelectionController;
import ots.controllers.TicketController;
import ots.database.DatabaseController;
import ots.database.FileDatabaseController;
import ots.models.Event;
import ots.models.User;
import ots.models.seat.Seat;
import ots.utils.FileParser;
import ots.utils.JsonFileParser;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Application {
    private static final Scanner scanner = new Scanner(System.in);

    private static final FileParser fileParser = new JsonFileParser("/Users/Jack Hughes/Documents/Coding/uni-online-ticketing-system/data");
    private static final DatabaseController databaseController = new FileDatabaseController(fileParser);

    private static final LoginController loginController = new LoginController(databaseController);
    private static final EventController eventController = new EventController(databaseController);
    private static final SeatSelectionController seatSelectionController = new SeatSelectionController(databaseController);
    private static final TicketController ticketController = new TicketController(databaseController);

    public static void main(String[] args) {
        Optional<User> optionalUser = Optional.empty();
        while (optionalUser.isEmpty()) {
            System.out.println("Please enter your email:");
            String email = scanner.nextLine().strip();
            System.out.println("Please enter your password:");
            String password = scanner.nextLine().strip();

            optionalUser = loginController.login(email, password);
        }

        User user = optionalUser.get();
        System.out.println(String.format("Welcome %s, you are a %s", user.getName(), user.getType()));

        String choice = "";
        while (!"exit".equalsIgnoreCase(choice)) {
            System.out.println("\n'purchase' - View events and purchase tickets");
            System.out.println("'tickets' - View previously purchased tickets");
            System.out.println("'exit' - Exit current session");
            System.out.println("Please enter the option you want to do:");
            choice = scanner.nextLine().strip();

            if ("purchase".equalsIgnoreCase(choice)) {
                startTicketPurchase(user);
            } else if ("tickets".equalsIgnoreCase(choice)) {
                ticketController.displayTickets(user);
            }
        }

        System.out.println("Thank you for using our online ticketing system");
    }

    private static void startTicketPurchase(User user) {
        Optional<Event> event = eventController.startEventSelection();
        if (event.isEmpty()) {
            return;
        }

        List<Seat> selectedSeats = seatSelectionController.startSeatSelection(event.get());
        ticketController.purchaseTickets(event.get(), user, selectedSeats);
    }
}
