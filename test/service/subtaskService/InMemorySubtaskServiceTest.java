package service.subtaskService;

import interfaces.service.EpicService;
import interfaces.service.SubtaskService;
import model.Epic;
import model.Subtask;
import model.TaskCreationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryEpicRepository;
import repository.InMemorySubtaskRepository;
import service.epicService.InMemoryEpicService;
import util.IdGenerator;
import util.InMemoryHistoryManager;
import util.TaskManagerConfig;
import util.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemorySubtaskServiceTest {
    EpicService epicService;
    SubtaskService subtaskService;

    @BeforeEach
    public void beforeEach() {
        TaskManagerConfig config = new TaskManagerConfig(
                null,
                new InMemoryEpicRepository(),
                new InMemorySubtaskRepository(),
                new IdGenerator(),
                new InMemoryHistoryManager()
        );

        epicService = new InMemoryEpicService(config);
        subtaskService = new InMemorySubtaskService(config);
    }

    @Test
    public void testCreation() {
        // Arrange
        Epic epic0 = epicService.create(
                new TaskCreationData("epic 0", "learn programming")
        );

        // Act
        Subtask subtask1 = subtaskService.create(
                new TaskCreationData("task0.1", "learn java syntax"),
                epic0.getId()
        );
        Subtask subtask2 = subtaskService.create(
                new TaskCreationData("task0.2", "learn git"),
                epic0.getId()
        );
        Subtask subtask3 = subtaskService.create(
                new TaskCreationData("task0.3", "learn unit testing"),
                epic0.getId()
        );
        ArrayList<Subtask> subtasksFromEpic = epic0.getSubtasks();
        ArrayList<Subtask> subtasksFromRepo = subtaskService.getAll();

        // Assert
        assertEquals(3, subtasksFromEpic.size());
        assertEquals(3, subtasksFromRepo.size());
        assertEquals(subtasksFromEpic, subtasksFromRepo);
        assertEquals(subtask1, subtasksFromRepo.get(0));
        assertEquals(subtask2, subtasksFromRepo.get(1));
        assertEquals(subtask3, subtasksFromRepo.get(2));
        assertEquals(epic0, subtask1.getEpic());
        assertEquals(epic0, subtask2.getEpic());
        assertEquals(epic0, subtask3.getEpic());
        assertEquals(0, epic0.getId());
        assertEquals(1, subtask1.getId());
        assertEquals(2, subtask2.getId());
        assertEquals(3, subtask3.getId());
        assertEquals(TaskStatus.NEW, epic0.getStatus());
        assertEquals(TaskStatus.NEW, subtask1.getStatus());
        assertEquals(TaskStatus.NEW, subtask2.getStatus());
        assertEquals(TaskStatus.NEW, subtask3.getStatus());

    }

    @Test
    public void testUpdating() {
        // Arrange
        Epic epic0 = epicService.create(
                new TaskCreationData("epic 0", "learn programming")
        );
        Subtask subtask1 = subtaskService.create(
                new TaskCreationData("task0.1", "learn java syntax"),
                epic0.getId()
        );
        Subtask subtask2 = subtaskService.create(
                new TaskCreationData("task0.2", "learn git"),
                epic0.getId()
        );
        Subtask subtask3 = subtaskService.create(
                new TaskCreationData("task0.3", "learn unit testing"),
                epic0.getId()
        );

        // Assert
        assertEquals(TaskStatus.NEW, epic0.getStatus());
        assertEquals(TaskStatus.NEW, subtask1.getStatus());
        assertEquals(TaskStatus.NEW, subtask2.getStatus());
        assertEquals(TaskStatus.NEW, subtask3.getStatus());

        // Act
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        subtask2 = subtaskService.update(subtask2);

        // Assert
        assertEquals(TaskStatus.IN_PROGRESS, epic0.getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subtask2.getStatus());
        assertEquals(TaskStatus.NEW, subtask1.getStatus());
        assertEquals(TaskStatus.NEW, subtask3.getStatus());

        // Act
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        subtaskService.update(subtask1);
        subtaskService.update(subtask2);
        subtaskService.update(subtask3);

        // Assert
        assertEquals(TaskStatus.IN_PROGRESS, epic0.getStatus());
        assertEquals(TaskStatus.DONE, subtask1.getStatus());
        assertEquals(TaskStatus.DONE, subtask2.getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subtask3.getStatus());

        // Act
        subtask3.setStatus(TaskStatus.DONE);
        subtaskService.update(subtask3);

        // Assert
        assertEquals(TaskStatus.DONE, epic0.getStatus());
        assertEquals(TaskStatus.DONE, subtask1.getStatus());
        assertEquals(TaskStatus.DONE, subtask2.getStatus());
        assertEquals(TaskStatus.DONE, subtask3.getStatus());
    }

    @Test
    public void testRemoving() {
        // Arrange
        Epic epic0 = epicService.create(
                new TaskCreationData("epic 0", "learn programming")
        );
        Subtask subtask1 = subtaskService.create(
                new TaskCreationData("task0.1", "learn java syntax"),
                epic0.getId()
        );
        Subtask subtask2 = subtaskService.create(
                new TaskCreationData("task0.2", "learn git"),
                epic0.getId()
        );
        Subtask subtask3 = subtaskService.create(
                new TaskCreationData("task0.3", "learn unit testing"),
                epic0.getId()
        );

        // Assert
        assertEquals(TaskStatus.NEW, epic0.getStatus());
        assertEquals(TaskStatus.NEW, subtask1.getStatus());
        assertEquals(TaskStatus.NEW, subtask2.getStatus());
        assertEquals(TaskStatus.NEW, subtask3.getStatus());

        // Act
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        subtaskService.update(subtask1);
        subtaskService.update(subtask2);
        subtaskService.update(subtask3);

        // Assert
        assertEquals(TaskStatus.IN_PROGRESS, epic0.getStatus());
        assertEquals(TaskStatus.DONE, subtask1.getStatus());
        assertEquals(TaskStatus.DONE, subtask2.getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subtask3.getStatus());

        // Act
        subtaskService.remove(subtask3.getId());

        // Assert
        assertEquals(2, epic0.getSubtasks().size());
        assertEquals(2, subtaskService.getAll().size());
        assertEquals(TaskStatus.DONE, epic0.getStatus());
        assertEquals(TaskStatus.DONE, subtask1.getStatus());
        assertEquals(TaskStatus.DONE, subtask2.getStatus());

        // Act
        subtaskService.removeAll();

        // Assert
        assertEquals(TaskStatus.NEW, epic0.getStatus());
    }
}