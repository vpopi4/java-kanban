package interfaces.repository;

import java.util.ArrayList;

public interface Repository<E, I> {
    E create(E entity);

    E get(I id);

    ArrayList<E> getAll();

    E update(E entity);

    void remove(I id);

    void removeAll();
}
