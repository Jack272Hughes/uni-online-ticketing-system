package ots.models.seat;

import java.util.HashMap;

public class RowMap extends HashMap<String, ColumnMap> {
    private static final ColumnMap EMPTY_COLUMN = new ColumnMap();

    public static RowMap empty() {
        return new RowMap();
    }

    public ColumnMap getColumnMap(String row) {
        return getOrDefault(row, EMPTY_COLUMN);
    }
}
