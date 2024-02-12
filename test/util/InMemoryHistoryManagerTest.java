package util;

import interfaces.HistoryManager;
import model.Task;
import model.TaskCreationData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    private Task createTask(int id) {
        return new Task(id, new TaskCreationData(
                "Task#" + id,
                "lorem ipsum dollar"
        ));
    }

    @Test
    public void shouldSaveTask() {
        Task expected = createTask(0);

        manager.add(expected);

        Task actual = manager.getHistory().get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldRemoveFirstItemIfLastItemAddedIsEleventh() {
        Task expected = createTask(1);

        for (int i = 0; i < 11; i++) {
            manager.add(createTask(i));
        }

        Task actual = manager.getHistory().get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnListWithSizeEqualTo10WhenAdding11Items() {
        int expected = 10;

        for (int i = 0; i < 11; i++) {
            manager.add(createTask(i));
        }

        int actual = manager.getHistory().size();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnListWithSizeEqualTo10WhenAdding100Items() {
        int expected = 10;

        for (int i = 0; i < 100; i++) {
            manager.add(createTask(i));
        }

        int actual = manager.getHistory().size();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyListWhenNoItemHaveBeenAddedYet() {
        List<Task> list = manager.getHistory();

        assertTrue(list.isEmpty());
    }

    @Test
    public void shouldDoesNotSaveWhenPassedNull() {
        manager.add(null);
        List<Task> actual = manager.getHistory();

        assertTrue(actual.isEmpty());
    }
}