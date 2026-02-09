package com.example.learn.service.impl;

import cn.hutool.core.util.IdUtil;
import com.example.learn.entity.Todo;
import com.example.learn.repository.TodoRepository;
import com.example.learn.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.learn.entity.TodoMarkRelation;
import com.example.learn.repository.TodoMarkRelationRepository;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoServiceImpl implements TodoService {

  @Autowired private TodoRepository todoRepository;
  @Autowired private TodoMarkRelationRepository relationRepository;

  @Override
  public List<Todo> findAll() {
    // 1. 查询所有 Todo
    List<Todo> todos = todoRepository.findAll();
    if (todos.isEmpty()) {
        return todos;
    }

    // 2. 提取 ID 列表
    List<Long> todoIds = todos.stream().map(Todo::getId).collect(Collectors.toList());

    // 3. 查询关联表 (手动组装，避开 Hibernate + ShardingSphere 的 JOIN 坑)
    List<TodoMarkRelation> relations = relationRepository.findByTodoIdIn(todoIds);

    // 4. 内存分组
    Map<Long, List<TodoMarkRelation>> relationMap = relations.stream()
        .collect(Collectors.groupingBy(r -> r.getTodo().getId()));

    // 5. 填回 Todo 对象
    for (Todo todo : todos) {
        List<TodoMarkRelation> todoRelations = relationMap.get(todo.getId());
        if (todoRelations != null) {
            todo.setMarkRelations(todoRelations);
        } else {
            todo.setMarkRelations(new ArrayList<>());
        }
    }

    return todos;
  }

  @Override
  public Todo create(Todo todo) {
    // 手动生成雪花 ID
    if (todo.getId() == null) {
      todo.setId(IdUtil.getSnowflakeNextId());
    }
    // 确保 userId 同步给关联对象 (因为 Controller 可能是在 JSON 解析后才设置的 userId)
    todo.syncUserIdToRelations();
    return todoRepository.save(todo);
  }

  @Override
  @Transactional
  public Todo update(Long id, Todo todoDetails) {
    Todo todo =
        todoRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    
    if (todoDetails.getTitle() != null) {
        todo.setTitle(todoDetails.getTitle());
    }
    if (todoDetails.getCompleted() != null) {
        todo.setCompleted(todoDetails.getCompleted());
    }
    if (todoDetails.getDescription() != null) {
        todo.setDescription(todoDetails.getDescription());
    }
    if (todoDetails.getPlannedFinishTime() != null) {
        todo.setPlannedFinishTime(todoDetails.getPlannedFinishTime());
    }
    if (todoDetails.getReminders() != null) {
        todo.setReminders(todoDetails.getReminders());
    }
    
    // 更新关联的标签 (仅当 todoMarkIds 不为 null 时才更新)
    if (todoDetails.getTodoMarkIds() != null) {
        // 1. 手动删除旧的关联 (避开 Hibernate orphanRemoval 在分片下的潜在问题)
        relationRepository.deleteByTodoId(id);
        
        // 2. 强制刷新，确保删除 SQL 先执行
        relationRepository.flush();

        // 3. 此时再设置新标签
        todo.setTodoMarkIds(todoDetails.getTodoMarkIds());
        
        // 4. 同步 userId
        todo.syncUserIdToRelations();
    }
    
    return todoRepository.save(todo);
  }

  @Override
  public void delete(Long id) {
    todoRepository.deleteById(id);
  }
}
