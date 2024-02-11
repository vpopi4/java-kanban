package util;

import interfaces.HistoryManager;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import interfaces.repository.TaskRepository;
import model.Task;

public class TaskManagerConfig {
    private TaskRepository taskRepository;
    private EpicRepository epicRepository;
    private SubtaskRepository subtaskRepository;
    private IdGenerator idGenerator;
    private HistoryManager<Task> historyManager;

    public TaskManagerConfig(
            TaskRepository taskRepository,
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator,
            HistoryManager<Task> historyManager
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

    public HistoryManager<Task> getHistoryManager() {
        return historyManager;
    }
}
