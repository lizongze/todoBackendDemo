package com.example.learn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "todo_mark_relation", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"todo_id", "mark_id"}) // 确保同一个 Todo 不会重复打同一个标签
})
@EntityListeners(AuditingEntityListener.class) // 启用自动审计功能（填充 createdAt）
public class TodoMarkRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    @JsonIgnore // 防止 JSON 死循环：Todo -> Relation -> Todo
    private Todo todo;

    @ManyToOne(fetch = FetchType.EAGER) // 查 Relation 时顺便把 Tag 查出来，方便
    @JoinColumn(name = "mark_id", nullable = false)
    private TodoMark todoMark;

    // 冗余 userId，用于分库分表绑定 (Binding Table)
    @Column(nullable = false)
    private Long userId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // 也可以加 createdBy, weight 等其他字段

    public TodoMarkRelation() {}

    public TodoMarkRelation(Todo todo, TodoMark todoMark) {
        this.todo = todo;
        this.todoMark = todoMark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    public TodoMark getTodoMark() {
        return todoMark;
    }

    public void setTodoMark(TodoMark todoMark) {
        this.todoMark = todoMark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
