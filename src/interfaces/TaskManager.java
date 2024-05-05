package interfaces;

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

}