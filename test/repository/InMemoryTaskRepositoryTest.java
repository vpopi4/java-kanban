package repository;

import model.Task;
import model.TaskCreationData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repository;

    @BeforeEach
    public void beforeEach() {
        repository = new InMemoryTaskRepository();
    }

    private Task createTask(int id) {
        return new Task(id, new TaskCreationData(
                "Task#" + id,
                "lorem ipsum dollar"
        ));
    }

    @Test
    public void shouldCreateTask() {
        Task task = createTask(0);
        Task createdTask = repository.create(task);
        assertEquals(task, createdTask);
    }

    @Test
    public void shouldHandleTasksWithSameId() {
        Task task1 = createTask(0);
        Task task2 = new Task(0, new TaskCreationData(
                "Task#0.1",
                "lorem ipsum dollar"
        ));
        repository.create(task1);
        repository.create(task2);

        ArrayList<Task> allTasks = repository.getAll();

        assertEquals(1, allTasks.size());
        assertTrue(allTasks.contains(task2));
    }

    @Test
    public void shouldGetTaskById() {
        Task task = createTask(0);
        repository.create(task);
        Task retrievedTask = repository.get(0);
        assertEquals(task, retrievedTask);
    }

    @Test
    public void shouldGetAllTasks() {
        Task task1 = createTask(0);
        Task task2 = createTask(1);
        repository.create(task1);
        repository.create(task2);
        ArrayList<Task> allTasks = repository.getAll();
        assertEquals(2, allTasks.size());
        assertTrue(allTasks.contains(task1));
        assertTrue(allTasks.contains(task2));
    }

    @Test
    public void shouldUpdateTask() {
        Task task = createTask(0);
        repository.create(task);
        Task updatedTask = new Task(0, new TaskCreationData("Updated Task", "Updated Description"));
        repository.update(updatedTask);
        Task retrievedTask = repository.get(0);
        assertEquals(updatedTask, retrievedTask);
    }

    @Test
    public void shouldRemoveTask() {
        Task task = createTask(0);
        repository.create(task);
        repository.remove(0);
        Task retrievedTask = repository.get(0);
        assertNull(retrievedTask);
    }

    @Test
    public void shouldRemoveAllTasks() {
        Task task1 = createTask(0);
        Task task2 = createTask(1);
        repository.create(task1);
        repository.create(task2);
        repository.removeAll();
        ArrayList<Task> allTasks = repository.getAll();
        assertEquals(0, allTasks.size());
    }
}
