package util;

import interfaces.model.Taskable;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class TaskableValidatorTest {
    private final Comparator<Taskable> taskableComparator = Comparator.comparing(Taskable::getStartTime);

    @Test
    public void testIsIntersect() {
        LocalDateTime start1 = LocalDateTime.of(2024, 6, 10, 8, 0);
        Duration duration1 = Duration.ofHours(2);

        LocalDateTime start2 = LocalDateTime.of(2024, 6, 10, 9, 0);
        Duration duration2 = Duration.ofHours(2);

        Task task1 = new Task(1);
        task1.setStartTime(start1);
        task1.setDuration(duration1);

        Task task2 = new Task(2);
        task2.setStartTime(start2);
        task2.setDuration(duration2);

        assertTrue(TaskableValidator.isIntersect(task1, task2));
    }

    @Test
    public void testIsNotIntersect() {
        LocalDateTime start1 = LocalDateTime.of(2024, 6, 10, 8, 0);
        Duration duration1 = Duration.ofHours(2);

        LocalDateTime start2 = LocalDateTime.of(2024, 6, 10, 12, 0);
        Duration duration2 = Duration.ofHours(2);

        Task task1 = new Task(1);
        task1.setStartTime(start1);
        task1.setDuration(duration1);

        Task task2 = new Task(2);
        task2.setStartTime(start2);
        task2.setDuration(duration2);

        assertFalse(TaskableValidator.isIntersect(task1, task2));
    }

    @Test
    public void testCheckIntersectionClosestSearchWithFloorIntersection() {
        LocalDateTime startTimeT1 = LocalDateTime.of(2024, 6, 10, 8, 0);
        Duration durationT1 = Duration.ofHours(2);
        Task t1 = new Task(1);
        t1.setStartTime(startTimeT1);
        t1.setDuration(durationT1);

        LocalDateTime startTimeFloor = LocalDateTime.of(2024, 6, 10, 7, 1);
        Duration durationFloor = Duration.ofHours(1);
        Task floor = new Task(2);
        floor.setStartTime(startTimeFloor);
        floor.setDuration(durationFloor);

        TreeSet<Taskable> ts = new TreeSet<>(taskableComparator);
        ts.add(floor);

        Exception exception = assertThrows(TaskableValidator.IntersectionException.class, () -> {
            TaskableValidator.checkIntersectionClosestSearch(t1, ts);
        });

        assertTrue(exception.getMessage().contains("intersection has occurred"));
    }

    @Test
    public void testCheckIntersectionClosestSearchWithCeilingIntersection() {
        LocalDateTime startTimeT1 = LocalDateTime.of(2024, 6, 10, 8, 0);
        Duration durationT1 = Duration.ofHours(2);
        Task t1 = new Task(1);
        t1.setStartTime(startTimeT1);
        t1.setDuration(durationT1);

        LocalDateTime startTimeCeiling = LocalDateTime.of(2024, 6, 10, 9, 0);
        Duration durationCeiling = Duration.ofHours(3);
        Task ceiling = new Task(2);
        ceiling.setStartTime(startTimeCeiling);
        ceiling.setDuration(durationCeiling);

        TreeSet<Taskable> ts = new TreeSet<>(taskableComparator); // Pass custom comparator to TreeSet constructor
        ts.add(ceiling);

        Exception exception = assertThrows(TaskableValidator.IntersectionException.class, () -> {
            TaskableValidator.checkIntersectionClosestSearch(t1, ts);
        });

        assertTrue(exception.getMessage().contains("intersection has occurred"));
    }

    @Test
    public void testCheckIntersectionClosestSearchWithoutIntersection() {
        LocalDateTime startTimeT1 = LocalDateTime.of(2024, 6, 10, 8, 0);
        Duration durationT1 = Duration.ofHours(2);
        Task t1 = new Task(1);
        t1.setStartTime(startTimeT1);
        t1.setDuration(durationT1);

        LocalDateTime startTimeFloor = LocalDateTime.of(2024, 6, 10, 6, 0);
        Duration durationFloor = Duration.ofHours(1);
        Task floor = new Task(2);
        floor.setStartTime(startTimeFloor);
        floor.setDuration(durationFloor);

        LocalDateTime startTimeCeiling = LocalDateTime.of(2024, 6, 10, 10, 0);
        Duration durationCeiling = Duration.ofHours(3);
        Task ceiling = new Task(3);
        ceiling.setStartTime(startTimeCeiling);
        ceiling.setDuration(durationCeiling);

        TreeSet<Taskable> ts = new TreeSet<>(taskableComparator);
        ts.add(floor);
        ts.add(ceiling);

        assertDoesNotThrow(() -> TaskableValidator.checkIntersectionClosestSearch(t1, ts));
    }
}