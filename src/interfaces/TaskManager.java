package interfaces;

import java.util.ArrayList;

import model.Task;

public interface TaskManager {

    TaskService getTaskService();

    EpicService getEpicService();

    SubtaskService getSubtaskService();

    ArrayList<Task> getHistory();

}