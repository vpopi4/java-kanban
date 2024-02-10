package service;

import repository.InMemoryTaskRepository;

public class InMemoryTaskService extends AbstractTaskService {
    public InMemoryTaskService(IdGenerator idGenerator) {
        super(new InMemoryTaskRepository(), idGenerator);
    }
}
