package interfaces;

import java.util.List;

import model.Task;

public interface HistoryManager {

    void add(Task item);
    List<Task> getHistory();

}
