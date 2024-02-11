package repository;

import interfaces.repository.TaskRepository;
import model.Task;

public class InMemoryTaskRepository extends AbstractInMemoryRepository<Task> implements TaskRepository {

}
