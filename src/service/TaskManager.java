package service;

import dtos.TaskDTO;
import enums.TaskStatus;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int seq;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        this.seq = 0;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    private int generateId() {
        return seq++;
    }

    public Task createTask(TaskDTO dto) {
        Task task = new Task(generateId(), dto);
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(TaskDTO dto) {
        Epic epic = new Epic(generateId(), dto);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(TaskDTO dto, int epicId) {
        Epic epic = epics.get(epicId);
        Subtask subtask = new Subtask(generateId(), dto, epic);

        subtasks.put(subtask.getId(), subtask);
        epic.addSubtask(subtask);

        return subtask;
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        return epic.getSubtasks();
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void removeEpic(int id) {
        Epic epic = epics.get(id);

        for (Subtask subtask : epic.getSubtasks()) {
            subtasks.remove(subtask.getId());
        }

        epics.remove(id);
    }

    public void removeAllSubtasks() {
        for (Epic epic : getAllEpics()) {
            epic.getSubtasks().clear();
        }
        subtasks.clear();
    }

    public void removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        subtask.getEpic().getSubtasks().remove(subtask);
        subtasks.remove(id);
    }

    public Task updateTask(Task task) {
        Task savedTask = tasks.get(task.getId());

        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
        savedTask.setStatus(task.getStatus());

        return savedTask;
    }

    public Epic updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());

        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        savedEpic.setStatus(calculateStatusOfEpic(savedEpic));

        return savedEpic;
    }

    public Subtask updateSubtask(Subtask subtask) {
        Subtask savesSubtask = subtasks.get(subtask.getId());

        savesSubtask.setName(subtask.getName());
        savesSubtask.setDescription(subtask.getDescription());
        savesSubtask.setStatus(subtask.getStatus());

        Epic epic = savesSubtask.getEpic();

        epic.setStatus(calculateStatusOfEpic(epic));

        return savesSubtask;
    }

    private TaskStatus calculateStatusOfEpic(Epic epic) {
        int size = epic.getSubtasks().size();

        if (size == 0) {
            return TaskStatus.NEW;
        }

        int countOfNew = 0;
        int countOfDone = 0;

        for (Subtask subtask : epic.getSubtasks()) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                countOfNew++;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                countOfDone++;
            }
        }

        if (countOfNew == size) {
            return TaskStatus.NEW;
        } else if (countOfDone == size) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
}
