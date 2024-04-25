package util;

import java.util.LinkedList;
import java.util.List;

import interfaces.HistoryManager;
import model.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> list;
    private static final int MAX_SIZE = 10;

    public InMemoryHistoryManager() {
        list = new LinkedList<>();
    }

    @Override
    public void add(Task item) {
        if (list.size() >= MAX_SIZE) {
            list.remove(0);
        }

        if (item == null) {
            return;
        }

        list.add(item);
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Task> getHistory() {
        return list;
    }
}
