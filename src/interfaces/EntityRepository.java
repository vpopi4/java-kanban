package interfaces;

import java.util.ArrayList;

public interface EntityRepository<Entity, IDType> {
    Entity create(Entity entity);

    Entity get(IDType id);
    ArrayList<Entity> getAll();

    Entity update(Entity entity);

    void remove(IDType id);
    void removeAll();
}
