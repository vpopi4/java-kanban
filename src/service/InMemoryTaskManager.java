package service;

import java.util.ArrayList;

import interfaces.TaskManager;
import interfaces.service.EpicService;
import interfaces.service.SubtaskService;
import interfaces.service.TaskService;
import model.Task;
import service.epicService.InMemoryEpicService;
import service.subtaskService.InMemorySubtaskService;
import service.taskService.InMemoryTaskService;
import util.TaskManagerConfig;

public class InMemoryTaskManager implements TaskManager {
    private final TaskService taskService;
    private final EpicService epicService;
    private final SubtaskService subtaskService;
    private final TaskManagerConfig config;

    public InMemoryTaskManager(TaskManagerConfig config) {
        this.config = config;
        taskService = new InMemoryTaskService(config);
        epicService = new InMemoryEpicService(config);
        subtaskService = new InMemorySubtaskService(config);
    }

    @Override
    public TaskService getTaskService() {
        return taskService;
    }

    @Override
    public EpicService getEpicService() {
        return epicService;
    }

    @Override
    public SubtaskService getSubtaskService() {
        return subtaskService;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return config.getHistoryManager().getHistory();
    }
}
