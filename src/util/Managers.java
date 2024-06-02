package util;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import service.InMemoryTaskManager;

public class Managers {
    public TaskManager getDefault() {
        return getInMemoryTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }
}
