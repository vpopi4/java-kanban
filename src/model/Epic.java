package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasks;

    public Epic(Integer id) {
        super(id);
        subtaskIds = new ArrayList<>();
    }

    public Epic(int id, TaskCreationData dto) {
        super(id, dto.getName(), dto.getDescription());
        subtasks = new ArrayList<>();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    private ArrayList<Integer> getSubtaskIDs() {
        var list = new ArrayList<Integer>();

        for (Subtask subtask : subtasks) {
            list.add(subtask.getId());
        }

        return list;
    }
    
    public static Epic fromString(String string) {
        String[] record = string.split(",");

        Integer id = Integer.parseInt(record[0]);
        TaskType type = TaskType.valueOf(record[1]);
        String name = record[2];
        TaskStatus status = TaskStatus.valueOf(record[3]);
        String description = record[4];

        if (type == TaskType.EPIC) {
            Epic epic = new Epic(id);
            epic.setName(name);
            epic.setStatus(status);
            epic.setDescription(description);
            return epic;
        }

        throw new IllegalArgumentException("record can not be resolved as epic");
    }
}
