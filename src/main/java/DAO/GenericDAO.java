package DAO;

public interface GenericDAO<T> {
    void add(T entity);
    T getById(int id);
    void update(T entity);
    void delete(int id);

}