package interfaces;

import interfaces.model.Taskable;
import interfaces.service.EpicService;
import interfaces.service.SubtaskService;
import interfaces.service.TaskService;
import model.Task;

import java.util.List;

public interface TaskManager {

    TaskService getTaskService();

    EpicService getEpicService();

    SubtaskService getSubtaskService();

    List<Task> getHistory();

    List<Taskable> getPrioritizedTasks();
}