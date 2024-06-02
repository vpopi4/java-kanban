package interfaces.model;

import util.TaskStatus;
import util.TaskType;

public interface Taskable {
    Integer getId();

    TaskType getType();

    String getName();

    TaskStatus getStatus();

    String getDescription();
}
