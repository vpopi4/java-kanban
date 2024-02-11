package service.taskService;

import util.TaskManagerConfig;

public class InMemoryTaskService extends AbstractTaskService {
    public InMemoryTaskService(TaskManagerConfig config) {
        super(config);
    }
}
