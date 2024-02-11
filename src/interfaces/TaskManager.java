package interfaces;

import java.util.ArrayList;

import interfaces.service.EpicService;
import interfaces.service.SubtaskService;
import interfaces.service.TaskService;
import model.Task;

public interface TaskManager {

    TaskService getTaskService();

    EpicService getEpicService();

    SubtaskService getSubtaskService();

    ArrayList<Task> getHistory();

}