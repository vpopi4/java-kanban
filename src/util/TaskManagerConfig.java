package util;

import interfaces.HistoryManager;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import interfaces.repository.TaskRepository;

public class TaskManagerConfig {
    private final TaskRepository taskRepository;
    private final EpicRepository epicRepository;
    private final SubtaskRepository subtaskRepository;
    private final IdGenerator idGenerator;
    private final HistoryManager historyManager;

    public TaskManagerConfig(
            TaskRepository taskRepository,
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator,
            HistoryManager historyManager
    ) {
        this.taskRepository = taskRepository;
        this.epicRepository = epicRepository;
        this.subtaskRepository = subtaskRepository;
        this.idGenerator = idGenerator;
        this.historyManager = historyManager;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public EpicRepository getEpicRepository() {
        return epicRepository;
    }

    public SubtaskRepository getSubtaskRepository() {
        return subtaskRepository;
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
