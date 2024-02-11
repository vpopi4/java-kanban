package util;

import java.util.ArrayList;

import model.Task;

public class History<T extends Task> {
    private final ArrayList<T> list;
    private static final int MAX_SIZE = 10;

    public History() {
        list = new ArrayList<>(MAX_SIZE);
    }

    public void add(T item) {
        if (list.size() >= MAX_SIZE) {
            list.remove(0);
        }

        list.add(item);
    }

    public ArrayList<T> getHistory() {
        return list;
    }
}
