package com.example.learn.repository;

import com.example.learn.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    // 使用 JOIN FETCH 一次性查出关联数据，利用 ShardingSphere 的 Binding Table 特性
    // distinct 是为了去重 (因为 OneToMany Join 会导致主表记录重复)
    @Query("SELECT distinct t FROM Todo t LEFT JOIN FETCH t.markRelations r LEFT JOIN FETCH r.todoMark")
    List<Todo> findAllWithMarks();
}