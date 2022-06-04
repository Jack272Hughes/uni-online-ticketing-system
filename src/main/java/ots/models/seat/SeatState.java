package ots.models.seat;

import java.util.HashMap;
import java.util.Map;

public enum SeatState {
    AVAILABLE,
    HOLD,
    RESERVED;

    private final static Map<Integer, SeatState> seatStateMap = new HashMap<>() {{
        put(0, AVAILABLE);
        put(1, HOLD);
        put(2, RESERVED);
    }};

    public static SeatState from(int state) {
        return seatStateMap.get(state);
    }
}
