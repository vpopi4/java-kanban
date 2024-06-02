package util;

import interfaces.HistoryManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {
    HistoryManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    private Task createTask(int id) {
        Task task = new Task(id);
        task.setName("Task#" + id);
        task.setDescription("lorem ipsum dollar");
        return task;
    }

    @Test
    public void shouldSaveTask() {
        // Given
        Task expected = createTask(0);

        // When
        manager.add(expected);

        // Then
        Task actual = manager.getHistory().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyListWhenNoItemHaveBeenAddedYet() {
        // Given
        List<Task> list = manager.getHistory();

        // When
        // Nothing

        // Then
        assertTrue(list.isEmpty());
    }

    @Test
    public void shouldDoesNotSaveWhenPassedNull() {
        // Given
        Task task = null;

        // When
        manager.add(task);

        // Then
        List<Task> actual = manager.getHistory();
        assertTrue(actual.isEmpty());
    }
}
