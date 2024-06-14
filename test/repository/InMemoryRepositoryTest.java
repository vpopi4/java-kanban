package repository;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TaskStatus;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryRepositoryTest {
    private InMemoryRepository repository;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryRepository();

        task = new Task(1);
        task.setName("Task 1");
        task.setDescription("Description 1");
        task.setStatus(TaskStatus.NEW);

        epic = new Epic(2);
        task.setName("Epic 1");
        task.setDescription("Description 2");
        task.setStatus(TaskStatus.NEW);

        subtask = new Subtask(3, 2);
        subtask.setName("Subtask 1");
        subtask.setDescription("Description 3");
        subtask.setStatus(TaskStatus.NEW);
    }

    @Test
    public void testCreationTask() {
        repository.create(task);
        assertEquals(task, repository.getTaskById(1).orElse(null));
    }

    @Test
    public void testCreationEpic() {
        repository.create(epic);
        assertEquals(epic, repository.getEpicById(2).orElse(null));
    }

    @Test
    public void testCreationSubtask() {
        repository.create(subtask);
        assertEquals(subtask, repository.getSubtaskById(3).orElse(null));
    }

    @Test
    public void testGetAnyTaskById() {
        repository.create(task);
        assertEquals(task, repository.getAnyTaskById(1).orElse(null));
    }

    @Test
    public void testGetAllTasks() {
        repository.create(task);
        List<Task> tasks = repository.getAllTasks();
        assertTrue(tasks.contains(task));
    }

    @Test
    public void testGetAllEpics() {
        repository.create(epic);
        List<Epic> epics = repository.getAllEpics();
        assertTrue(epics.contains(epic));
    }

    @Test
    public void testGetAllSubtasks() {
        repository.create(subtask);
        List<Subtask> subtasks = repository.getAllSubtasks();
        assertTrue(subtasks.contains(subtask));
    }

    @Test
    public void testUpdateTask() {
        repository.create(task);

        Task updatedTask = new Task(1);
        updatedTask.setName("Updated Task");
        updatedTask.setDescription("Updated Description");

        repository.update(updatedTask);

        assertEquals(updatedTask, repository.getTaskById(1).orElse(null));
    }

    @Test
    public void testUpdateEpic() {
        repository.create(epic);
        Epic updatedEpic = new Epic(2);
        updatedEpic.setName("Updated Epic");
        updatedEpic.setDescription("Updated Description");

        repository.update(updatedEpic);

        assertEquals(updatedEpic, repository.getEpicById(2).orElse(null));
    }

    @Test
    public void testUpdateSubtask() {
        repository.create(subtask);
        Subtask updatedSubtask = new Subtask(3, 2);
        updatedSubtask.setName("Updated Subtask");
        updatedSubtask.setDescription("Updated Description");

        repository.update(updatedSubtask);

        assertEquals(updatedSubtask, repository.getSubtaskById(3).orElse(null));
    }

    @Test
    public void testRemoveTask() {
        repository.create(task);
        repository.remove(1);
        assertFalse(repository.getTaskById(1).isPresent());
    }

    @Test
    public void testRemoveEpic() {
        repository.create(epic);
        repository.remove(2);
        assertFalse(repository.getEpicById(2).isPresent());
    }

    @Test
    public void testRemoveSubtask() {
        repository.create(subtask);
        repository.remove(3);
        assertFalse(repository.getSubtaskById(3).isPresent());
    }

    @Test
    public void testRemoveAllTasks() {
        repository.create(task);
        repository.removeAllTasks();
        assertTrue(repository.getAllTasks().isEmpty());
    }

    @Test
    public void testRemoveAllEpicsAndSubtasks() {
        repository.create(epic);
        repository.create(subtask);
        repository.removeAllEpicsAndSubtasks();
        assertTrue(repository.getAllEpics().isEmpty());
        assertTrue(repository.getAllSubtasks().isEmpty());
    }

    @Test
    public void testRemoveAllSubtasks() {
        repository.create(subtask);
        repository.removeAllSubtasks();
        assertTrue(repository.getAllSubtasks().isEmpty());
    }

    // Пограничные случаи
    @Test
    public void testGetNonExistentTask() {
        assertFalse(repository.getTaskById(99).isPresent());
    }

    @Test
    public void testUpdateNonExistentTask() {
        Task nonExistentTask = new Task(99);
        assertThrows(NoSuchElementException.class, () -> repository.update(nonExistentTask));
    }

    @Test
    public void testRemoveNonExistentTask() {
        assertDoesNotThrow(() -> repository.remove(99));
    }
}
