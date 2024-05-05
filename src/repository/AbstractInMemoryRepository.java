package repository;

import interfaces.repository.Repository;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractInMemoryRepository<E extends Task> implements Repository<E, Integer> {
    private HashMap<Integer, E> store;

    public AbstractInMemoryRepository() {
        store = new HashMap<>();
    }

    @Override
    public E create(E entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E get(Integer id) {
        return store.get(id);
    }

    @Override
    public ArrayList<E> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public E update(E entity) {
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
