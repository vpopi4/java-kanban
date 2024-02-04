package model;

import dtos.TaskDTO;
import enums.TaskStatus;

import java.util.Objects;

public class Task {
    protected final int id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    public Task(int id, TaskDTO dto) {
        this.id = id;
        this.name = dto.name;
        this.description = dto.description;
        this.status = dto.status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" + "\n\tid=" + id + ",\n\tname='" + name + '\'' + ",\n\tdescription='" + description + '\'' + ",\n\tstatus=" + status + "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }
}
