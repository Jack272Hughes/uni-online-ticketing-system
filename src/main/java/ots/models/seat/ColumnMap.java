package ots.models.seat;

import java.util.HashMap;

public class ColumnMap extends HashMap<Integer, Integer> {
    public SeatState getSeatState(int column) {

        if (containsKey(column)) {
            return SeatState.from(get(column));
        } else {
            return SeatState.AVAILABLE;
        }
    }

    public void put(int column, SeatState state) {
        put(column, state.ordinal());
    }
}
