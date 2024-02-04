package dtos;

import enums.TaskStatus;

public class TaskDTO {
    public String name;
    public String description;
    public TaskStatus status;

    public TaskDTO(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }
}
