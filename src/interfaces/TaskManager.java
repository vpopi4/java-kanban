package interfaces;

import java.util.List;

import interfaces.service.EpicService;
import interfaces.service.SubtaskService;
import interfaces.service.TaskService;
import model.Task;

public interface TaskManager {

    TaskService getTaskService();

    EpicService getEpicService();

    SubtaskService getSubtaskService();

    List<Task> getHistory();

}