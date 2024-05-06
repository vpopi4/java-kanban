package service.taskService;

import interfaces.HistoryManager;
import interfaces.service.TaskService;
import model.Task;
import model.TaskCreationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryTaskRepository;
import util.IdGenerator;
import util.InMemoryHistoryManager;
import util.TaskManagerConfig;
import util.TaskStatus;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskServiceTest {
    TaskService taskService;
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();

        TaskManagerConfig config = new TaskManagerConfig(
                new InMemoryTaskRepository(),
                null,
                null,
                new IdGenerator(),
                historyManager
        );

        taskService = new InMemoryTaskService(config);
    }

    @Test
    public void testCreation_twoTasksCase() {
        // creating first task
        // Arrange
        TaskCreationData data = new TaskCreationData(
                "name",
                "lorem ipsum dollar"
        );

        // Act
        Task task0 = taskService.create(data);

        // Assertion
        assertEquals(0, task0.getId());
        assertEquals(TaskStatus.NEW, task0.getStatus());

        // creating second task
        // Arrange
        data.setName("name1");
        data.setStatus(TaskStatus.IN_PROGRESS);

        // Act
        Task task1 = taskService.create(data);

        // Assertion
        assertEquals(1, task1.getId());
        assertEquals(TaskStatus.IN_PROGRESS, task1.getStatus());
        assertNotEquals(task0, task1);

        // polling created tasks
        // Act
        Task task0FromRepo = taskService.get(0);
        Task task1FromRepo = taskService.get(1);

        // Arrange
        assertEquals(task0, task0FromRepo);
        assertEquals(task1, task1FromRepo);

        // Act
        ArrayList<Task> tasks = taskService.getAll();

        // Arrange
        assertEquals(tasks.get(0), task0FromRepo);
        assertEquals(tasks.get(1), task1FromRepo);
    }

    @Test
    public void testCreating_nullValues() {
        // Arrange
        TaskCreationData data = new TaskCreationData(null, null);

        // Act
        Task task = taskService.create(data);
        Task taskFromRepo = taskService.get(0);

        // Assert
        assertInstanceOf(Task.class, task);
        assertNull(task.getName());
        assertNull(task.getDescription());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(task, taskFromRepo);

        // Arrange
        data = null;

        // Act
        task = taskService.create(data);
        taskFromRepo = taskService.get(1);

        // Assert
        assertInstanceOf(Task.class, task);
        assertNull(task.getName());
        assertNull(task.getDescription());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(task, taskFromRepo);
    }


    @Test
    public void testUpdating() {
        // Arrange
        TaskCreationData data = new TaskCreationData("name", "lorem ipsum dollar");
        Task task = taskService.create(data);

        task.setName("different name");
        task.setStatus(TaskStatus.IN_PROGRESS);

        // Act
        Task actualTask = taskService.update(task);

        // Assert
        assertEquals("different name", actualTask.getName());
        assertEquals("lorem ipsum dollar", actualTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, actualTask.getStatus());

        // Arrange
        task.setStatus(TaskStatus.DONE);

        // Act
        actualTask = taskService.update(task);

        // Assert
        assertEquals(task, actualTask);
    }

    @Test
    public void testRemoving() {
        // test void remove(Integer id) method
        // Arrange
        Task task0 = taskService.create(new TaskCreationData("task 0", "learn java"));
        Task task1 = taskService.create(new TaskCreationData("task 1", "learn unit testing"));
        Task task2 = taskService.create(new TaskCreationData("task 2", "learn git"));

        task0.setStatus(TaskStatus.IN_PROGRESS);
        task1.setStatus(TaskStatus.DONE);

        // Act
        taskService.remove(task1.getId());

        ArrayList<Task> tasks = taskService.getAll();

        boolean exceptionCatched = false;
        try {
            taskService.get(task1.getId());
        } catch (NoSuchElementException e) {
            exceptionCatched = true;
        }

        // Assert
        assertTrue(exceptionCatched);
        assertTrue(historyManager.getHistory().isEmpty());
        assertEquals(2, tasks.size());
        assertEquals(task0, tasks.get(0));
        assertEquals(task2, tasks.get(1));

        // test void removeAll() method
        // Act
        taskService.removeAll();
        tasks = taskService.getAll();

        exceptionCatched = false;
        try {
            taskService.get(task0.getId());
        } catch (NoSuchElementException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched); // Assertion

        exceptionCatched = false;
        try {
            taskService.get(task2.getId());
        } catch (NoSuchElementException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched); // Assertion

        // Assert
        assertEquals(0, tasks.size());

        // test creating tasks after removing all
        // Arrange
        Task task3 = taskService.create(new TaskCreationData("task 3", "learn spring"));

        // Assert
        assertEquals(3, task3.getId());
    }

    @Test
    public void testUpdation_withoutUpdateMethod() {
        // Arrange
        Task task = taskService.create(new TaskCreationData("name", "desk"));

        // Act
        task.setStatus(TaskStatus.DONE);
        Task taskFromRepo = taskService.get(0);

        // Assert
        assertSame(task, taskFromRepo);

        // Arrange
        taskService.create(new TaskCreationData("name1", "some text"));

        // Act
        Task task1 = taskService.get(1);

        task1.setStatus(TaskStatus.DONE);

        Task task1_ = taskService.get(1);

        // Assert
        assertSame(task1_, task1);
        assertEquals(TaskStatus.DONE, task1_.getStatus());

        // update and create methods return link that are used inside the repository by the HashMap,
        // so actually we can update task without calling update method
        // probably we need fix it
        // TODO: fix in memory storing
    }
}
