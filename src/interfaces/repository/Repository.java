package interfaces.repository;

import java.util.ArrayList;

public interface Repository<Entity, IDType> {
    Entity create(Entity entity);

    Entity get(IDType id);

    ArrayList<Entity> getAll();

    Entity update(Entity entity);

    void remove(IDType id);

    void removeAll();
}
