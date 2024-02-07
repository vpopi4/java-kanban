import dtos.TaskDTO;
import enums.TaskStatus;
import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        Epic epic = tm.createEpic(
                new TaskDTO("epic", "All the leaves are brown")
        );
        Subtask firstSubtask = tm.createSubtask(
                new TaskDTO("first subtask", "And the sky is gray"),
                epic.getId()
        );
        Subtask secondSubtask = tm.createSubtask(
                new TaskDTO("second subtask", "I've been for a walk"),
                epic.getId()
        );
        Subtask thirdSubtask = tm.createSubtask(
                new TaskDTO("third subtask", "On a winter's day"),
                epic.getId()
        );

        System.out.println("Creating entities...");
        System.out.println("Epics:\n" + tm.getAllEpics());
        System.out.println("Subtasks:\n" + tm.getAllSubtasks() + "\n");

        firstSubtask.setStatus(TaskStatus.DONE);
        firstSubtask = tm.updateSubtask(firstSubtask);
        secondSubtask.setStatus(TaskStatus.DONE);
        secondSubtask = tm.updateSubtask(secondSubtask);
        thirdSubtask.setStatus(TaskStatus.IN_PROGRESS);
        thirdSubtask = tm.updateSubtask(thirdSubtask);

        System.out.println("Changing subtasks of firstEpic...");
        System.out.println("Epics:\n" + tm.getAllEpics());
        System.out.println("Subtasks:\n" + tm.getAllSubtasks() + "\n");

        tm.removeSubtask(thirdSubtask.getId());

        System.out.println("Deleting third subtask...");
        System.out.println("Epics:\n" + tm.getAllEpics());
        System.out.println("Subtasks:\n" + tm.getAllSubtasks() + "\n");

        tm.removeAllSubtasks();

        System.out.println("Deleting all subtask...");
        System.out.println("Epics:\n" + tm.getAllEpics());
        System.out.println("Subtasks:\n" + tm.getAllSubtasks() + "\n");
    }
}
