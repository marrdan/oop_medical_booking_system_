package repositories;

import java.util.List;

public interface Repository<T> {
    void save(T t);
    void delete(T t);
    T findById(String id);
    List<T> findAll();


}
