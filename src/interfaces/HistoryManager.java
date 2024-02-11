package interfaces;

import java.util.ArrayList;

import model.Task;

public interface HistoryManager<T extends Task> {

    void add(T item);

    ArrayList<T> getHistory();

}