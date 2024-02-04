package service;

import dtos.TaskDTO;
import model.Task;
import repository.TaskRepository;

import java.util.ArrayList;

public class TaskManager {
    private int seq;
    private final TaskRepository taskRepository;

    public TaskManager() {
        this.seq = 0;
        this.taskRepository = new TaskRepository();
    }

     int generateId() {
        return seq++;
    }

    public void createTask(TaskDTO dto) {
        Task task = new Task(generateId(), dto);
        taskRepository.create(task);
    }

    public Task getTaskById(int id) {
        return taskRepository.getById(id);
    }

    public ArrayList<Task> getAllTask() {
        return taskRepository.getAll();
    }

    public void updateTask(Task task) {
        taskRepository.update(task);
    }

    public void removeAllTasks() {
        taskRepository.removeALl();
    }

    public void removeTaskById(int id) {
        taskRepository.removeById(id);
    }
}
