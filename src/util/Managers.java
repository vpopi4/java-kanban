package util;

import interfaces.TaskManager;
import service.InMemoryTaskManager;

public class Managers {
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
