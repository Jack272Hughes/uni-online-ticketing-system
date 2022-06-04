package ots.models.seat;

public class Seat {
    private final String row;
    private final int column;
    private final SeatState state;

    public Seat(String row, int column, SeatState state) {
        this.row = row;
        this.column = column;
        this.state = state;
    }

    public String getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public SeatState getState() {
        return state;
    }

    public String getLocation() {
        return row + column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return column == seat.column &&
                row.equals(seat.row);
    }
}
