package repository;

import interfaces.repository.Repository;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractInMemoryRepository<Entity extends Task> implements Repository<Entity, Integer> {
    private HashMap<Integer, Entity> store;

    public AbstractInMemoryRepository() {
        store = new HashMap<>();
    }

    @Override
    public Entity create(Entity entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Entity get(Integer id) {
        return store.get(id);
    }

    @Override
    public ArrayList<Entity> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Entity update(Entity entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void remove(Integer id) {
        store.remove(id);
    }

    @Override
    public void removeAll() {
        store.clear();
    }
}
