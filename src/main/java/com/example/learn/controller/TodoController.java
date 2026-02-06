package com.example.learn.controller;

import com.example.learn.common.Result;
import com.example.learn.entity.Todo;
import com.example.learn.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public Result<List<Todo>> getAllTodos() {
        return Result.success(todoService.findAll());
    }

    @PostMapping
    public Result<Todo> createTodo(@RequestBody Todo todo) {
        return Result.success(todoService.create(todo));
    }

    @PutMapping("/{id}")
    public Result<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        return Result.success(todoService.update(id, todoDetails));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return Result.success();
    }
}