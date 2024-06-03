package repository;

import interfaces.model.Taskable;
import util.ManagerSaveException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class FileBackedRepository extends InMemoryRepository {
    private final Path pathToBackup;

    public FileBackedRepository(Path pathToBackup) {
        super();
        this.pathToBackup = pathToBackup;
    }

    public void save() throws ManagerSaveException {
        try {
            String header = "id,type,name,status,description,epic\n";
            StringBuilder sb = new StringBuilder(header);

            for (Map.Entry<Integer, Taskable> entry : store.entrySet()) {
                sb.append(entry.getValue().toString());
            }

            Files.writeString(
                    pathToBackup,
                    sb.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }
}
