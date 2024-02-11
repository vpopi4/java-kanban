package util;

import java.util.ArrayList;

import interfaces.HistoryManager;
import model.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> list;
    private static final int MAX_SIZE = 10;

    public InMemoryHistoryManager() {
        list = new ArrayList<>(MAX_SIZE);
    }

    @Override
    public void add(Task item) {
        if (list.size() >= MAX_SIZE) {
            list.remove(0);
        }

        list.add(item);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return list;
    }
}
