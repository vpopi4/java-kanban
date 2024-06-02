package service.subtaskService;

import interfaces.service.EpicService;
import interfaces.service.SubtaskService;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepository;
import service.epicService.InMemoryEpicService;
import util.IdGenerator;
import util.InMemoryHistoryManager;
import util.TaskManagerConfig;
import util.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemorySubtaskServiceTest {
    EpicService epicService;
    SubtaskService subtaskService;

    @BeforeEach
    public void beforeEach() {
        TaskManagerConfig config = new TaskManagerConfig(
                new InMemoryRepository(),
                new IdGenerator(),
                new InMemoryHistoryManager()
        );

        epicService = new InMemoryEpicService(config);
        subtaskService = new InMemorySubtaskService(config);
    }

    @Test
    public void testCreation() {
        // Arrange
        Epic epic0 = epicService.create("epic 0", "learn programming");

        // Act
        Subtask subtask1 = subtaskService.create(
                epic0.getId(), "task0.1", "learn java syntax"
        );
        Subtask subtask2 = subtaskService.create(
                epic0.getId(), "task0.2", "learn git"
        );
        Subtask subtask3 = subtaskService.create(
                epic0.getId(), "task0.3", "learn unit testing"
        );
        List<Integer> subtasksFromEpic = epic0.getSubtaskIds();
        List<Integer> subtasksFromRepo = subtaskService.getAll().stream().map(Task::getId).toList();

        // Assert
        assertEquals(3, subtasksFromEpic.size());
        assertEquals(3, subtasksFromRepo.size());
        assertEquals(subtasksFromEpic, subtasksFromRepo);
        assertEquals(subtask1.getId(), subtasksFromRepo.get(0));
        assertEquals(subtask2.getId(), subtasksFromRepo.get(1));
        assertEquals(subtask3.getId(), subtasksFromRepo.get(2));
        assertEquals(epic0.getId(), subtask1.getEpicId());
        assertEquals(epic0.getId(), subtask2.getEpicId());
        assertEquals(epic0.getId(), subtask3.getEpicId());
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
        Epic epic0 = epicService.create("epic 0", "learn programming");
        Subtask subtask1 = subtaskService.create(
                epic0.getId(), "task0.1", "learn java syntax"
        );
        Subtask subtask2 = subtaskService.create(
                epic0.getId(), "task0.2", "learn git"
        );
        Subtask subtask3 = subtaskService.create(
                epic0.getId(), "task0.3", "learn unit testing"
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
        Epic epic0 = epicService.create("epic 0", "learn programming");
        Subtask subtask1 = subtaskService.create(
                epic0.getId(), "task0.1", "learn java syntax"
        );
        Subtask subtask2 = subtaskService.create(
                epic0.getId(), "task0.2", "learn git"
        );
        Subtask subtask3 = subtaskService.create(
                epic0.getId(), "task0.3", "learn unit testing"
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
        assertEquals(2, epic0.getSubtaskIds().size());
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