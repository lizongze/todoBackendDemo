package com.example.learn.service;

import com.example.learn.entity.TodoMark;
import java.util.List;

public interface TodoMarkService {
    List<TodoMark> findAll();
    TodoMark create(TodoMark todoMark);
}
