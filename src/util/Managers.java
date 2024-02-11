package util;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import interfaces.repository.TaskRepository;
import repository.InMemoryEpicRepository;
import repository.InMemorySubtaskRepository;
import repository.InMemoryTaskRepository;
import service.InMemoryTaskManager;

public class Managers {
    public TaskManager getDefault() {
        return getInMemoryTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public TaskManager getInMemoryTaskManager() {
        TaskRepository taskRepository = new InMemoryTaskRepository();
        EpicRepository epicRepository = new InMemoryEpicRepository();
        SubtaskRepository subtaskRepository = new InMemorySubtaskRepository();
        IdGenerator idGenerator = new IdGenerator();
        HistoryManager historyService = new InMemoryHistoryManager();
        
        TaskManagerConfig config = new TaskManagerConfig(
            taskRepository,
            epicRepository,
            subtaskRepository,
            idGenerator,
            historyService
        );

        return new InMemoryTaskManager(config);
    }
}
