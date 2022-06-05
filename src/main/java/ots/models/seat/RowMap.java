package ots.models.seat;

import java.util.HashMap;

public class RowMap extends HashMap<String, ColumnMap> {
    public static RowMap empty() {
        return new RowMap();
    }

    public ColumnMap getColumnMap(String row) {
        return getOrDefault(row, new ColumnMap());
    }
}
