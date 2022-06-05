package ots.utils;

import ots.models.seat.ColumnMap;
import ots.models.seat.RowMap;
import ots.models.seat.Seat;
import ots.models.seat.SeatState;

import java.util.HashSet;
import java.util.List;

public class SeatDisplay {
    private static final String COLOUR_RESET = "\033[0m";
    private static final String GREEN = "\033[0;32m";
    private static final String CYAN = "\033[0;36m";
    private static final String WHITE = "\033[0;37m";
    private static final String RED = "\033[0;31m";

    private static final int TOTAL_ROWS = 8;
    private static final int TOTAL_COLUMNS = 8;

    private final RowMap rows;
    private final HashSet<String> selectedSeats = new HashSet<>();

    public SeatDisplay(RowMap rows, List<Seat> selectedSeats) {
        this.rows = rows;
        selectedSeats.forEach(seat -> this.selectedSeats.add(seat.getLocation()));
    }

    public void display() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n    | 1  | 2  | 3  | 4  | 5  | 6  | 7  | 8  |\n")
                     .append("---------------------------------------------");
        for (int rowNum = 1; rowNum <= TOTAL_ROWS; rowNum++) {
            String rowLetter = Character.toString(rowNum + 64);
            stringBuilder.append("\n| ").append(rowLetter).append(" |");

            ColumnMap columnMap = rows.getColumnMap(rowLetter);
            for (int columnNum = 1; columnNum <= TOTAL_COLUMNS; columnNum++) {
                SeatState seatState = columnMap.getSeatState(columnNum);
                String colouredSeat = getColouredSeat(seatState, rowLetter + columnNum);
                stringBuilder.append(" ").append(colouredSeat).append(" |");
            }
        }

        System.out.println(stringBuilder.toString());
    }

    private String getColouredSeat(SeatState seatState, String location) {
        String colouredSeat;

        switch (seatState) {
            case AVAILABLE:
                colouredSeat = GREEN;
                break;
            case HOLD:
                if (selectedSeats.contains(location)) {
                    colouredSeat = CYAN;
                } else {
                    colouredSeat = WHITE;
                }
                break;
            case RESERVED:
            default:
                colouredSeat = RED;
        }

        colouredSeat += "██" + COLOUR_RESET;
        return colouredSeat;
    }
}
