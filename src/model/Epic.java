package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final List<Integer> subtaskIds;

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

    public void addSubtaskId(Integer id) {
        subtaskIds.add(id);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds.clear();
        this.subtaskIds.addAll(subtaskIds);
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
