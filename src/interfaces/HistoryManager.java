package interfaces;

import java.util.ArrayList;

import model.Task;

public interface HistoryManager {

    void add(Task item);
    ArrayList<Task> getHistory();

}
