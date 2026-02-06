package com.example.learn.service.impl;

import com.example.learn.entity.Todo;
import com.example.learn.repository.TodoRepository;
import com.example.learn.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

  @Autowired private TodoRepository todoRepository;

  @Override
  public List<Todo> findAll() {
    return todoRepository.findAll();
  }

  @Override
  public Todo create(Todo todo) {
    return todoRepository.save(todo);
  }

  @Override
  public Todo update(Long id, Todo todoDetails) {
    Todo todo =
        todoRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    todo.setTitle(todoDetails.getTitle());
    todo.setCompleted(todoDetails.isCompleted());
    todo.setDescription(todoDetails.getDescription());
    todo.setPlannedFinishTime(todoDetails.getPlannedFinishTime());
    todo.setReminders(todoDetails.getReminders());
    return todoRepository.save(todo);
  }

  @Override
  public void delete(Long id) {
    todoRepository.deleteById(id);
  }
}
