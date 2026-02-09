package com.example.learn.repository;

import com.example.learn.entity.TodoMarkRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoMarkRelationRepository extends JpaRepository<TodoMarkRelation, Long> {
    
    // 根据 todoId 列表查询关联关系
    // 注意：在分片环境下，如果没有 userId，这会触发全库路由。
    // 但鉴于我们现在没有 userId 上下文，这是唯一办法。
    List<TodoMarkRelation> findByTodoIdIn(List<Long> todoIds);

    void deleteByTodoId(Long todoId);
}
