package com.alaasmagi.restaurant_booking_api.application.contracts;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    void delete(ID id);
}