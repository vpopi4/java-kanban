package model;

import interfaces.model.Taskable;
import util.TaskStatus;
import util.TaskType;

public class Task implements Taskable {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    public Task(Integer id) {
        this.id = id;
        this.status = TaskStatus.NEW;
    }

    public Task(int id, TaskCreationData dto) {
        this.id = id;
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.status = TaskStatus.NEW;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        // id,type,name,status,description,epic
        return id + "," +
                TaskType.TASK + "," +
                name + "," +
                status + "," +
                description + ",\n";
    }

    public static Task fromString(String string) {
        String[] record = string.split(",");

        Integer id = Integer.parseInt(record[0]);
        TaskType type = TaskType.valueOf(record[1]);
        String name = record[2];
        TaskStatus status = TaskStatus.valueOf(record[3]);
        String description = record[4];

        if (type == TaskType.TASK) {
            Task task = new Task(id);
            task.setName(name);
            task.setStatus(status);
            task.setDescription(description);
            return task;
        }

        throw new IllegalArgumentException("record can not be resolved as task");
    }
}
