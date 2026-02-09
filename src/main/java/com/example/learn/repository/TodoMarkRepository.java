package com.example.learn.repository;

import com.example.learn.entity.TodoMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoMarkRepository extends JpaRepository<TodoMark, Long> {
    
    // 检查指定用户的标签是否存在
    boolean existsByUserIdAndTitle(Long userId, String title);

    // 检查公共标签（UserId为null）是否存在
    boolean existsByUserIdIsNullAndTitle(String title);
}
