package util;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import model.Task;
import service.InMemoryTaskManager;

public class Managers {
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
