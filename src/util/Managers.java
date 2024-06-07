package util;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import service.FileBackedTaskManager;
import service.InMemoryTaskManager;

public class Managers {
    public TaskManager getDefault() {
        return getFileBackedTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public TaskManager getFileBackedTaskManager() {
        return new FileBackedTaskManager();
    }
}
