package com.example.learn.service;

import com.example.learn.entity.Todo;
import java.util.List;

public interface TodoService {
    List<Todo> findAll();
    Todo create(Todo todo);
    Todo update(Long id, Todo todo);
    void delete(Long id);
}
