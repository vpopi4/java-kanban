package interfaces.model;

import util.TaskStatus;
import util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Taskable {
    Integer getId();

    TaskType getType();

    String getName();

    TaskStatus getStatus();

    String getDescription();

    Duration getDuration();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();
}
