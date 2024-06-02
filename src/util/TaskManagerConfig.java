package util;

import interfaces.HistoryManager;
import interfaces.repository.Repository;

public record TaskManagerConfig(
        Repository repository,
        IdGenerator idGenerator,
        HistoryManager historyManager
) {

}
