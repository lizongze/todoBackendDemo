package com.example.learn.entity;

import com.example.learn.converter.StringListConverter;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Todo {
  @Id
  // 使用雪花ID时，移除自增策略
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonSerialize(using = ToStringSerializer.class) // 防止前端JS精度丢失
  private Long id;

  private String title;
  private Boolean completed;
  private String description;

  // 分库分表键 (Sharding Key)
  @Column(updatable = false)
  private Long userId;

  // 存储公司时区时间
  @Column(name = "plannedFinishTime", columnDefinition = "DATETIME(6)")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime plannedFinishTime;

  @Column(name = "reminders")
  @Convert(converter = StringListConverter.class)
  private List<String> reminders;

  @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<TodoMarkRelation> markRelations;

  // 虚拟 Setter：前端传 {"todoMarkIds": [1, 2]} 时，自动组装 Relation 对象
  @JsonProperty("todoMarkIds")
  public void setTodoMarkIds(List<Long> todoMarkIds) {
    if (todoMarkIds != null) {
      if (this.markRelations == null) {
        this.markRelations = new ArrayList<>();
      }
      // 清空现有关系 (orphanRemoval = true 会自动删除数据库记录)
      this.markRelations.clear();
      
      for (Long markId : todoMarkIds) {
        TodoMark mark = new TodoMark();
        mark.setId(markId);
        
        // 创建中间对象，并建立双向关联
        TodoMarkRelation relation = new TodoMarkRelation(this, mark);
        this.markRelations.add(relation);
      }
    }
  }

  // 虚拟 Getter：从 Relation 中提取 Mark ID
  @JsonProperty("todoMarkIds")
  public List<Long> getTodoMarkIds() {
    if (markRelations == null) {
      return null;
    }
    return markRelations.stream()
        .map(r -> r.getTodoMark().getId())
        .collect(Collectors.toList());
  }

  // 获取完整的标签对象列表 (方便前端展示)
  @JsonProperty("todoMarks")
  public List<TodoMark> getTodoMarks() {
    if (markRelations == null) {
      return null;
    }
    return markRelations.stream()
        .map(TodoMarkRelation::getTodoMark)
        .collect(Collectors.toList());
  }
  
  @JsonIgnore
  public List<TodoMarkRelation> getMarkRelations() {
    return markRelations;
  }

  public void setMarkRelations(List<TodoMarkRelation> markRelations) {
    this.markRelations = markRelations;
  }

  // 保存前，将 Todo 的 userId 同步给所有的 Relation，确保分片键一致
  @PrePersist
  @PreUpdate
  public void syncUserIdToRelations() {
    if (this.markRelations != null && this.userId != null) {
      for (TodoMarkRelation relation : this.markRelations) {
        relation.setUserId(this.userId);
      }
    }
  }

  public Todo() {}

  public Todo(String title, boolean completed) {
    this.title = title;
    this.completed = completed;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public LocalDateTime getPlannedFinishTime() {
    return plannedFinishTime;
  }

  public void setPlannedFinishTime(LocalDateTime plannedFinishTime) {
    this.plannedFinishTime = plannedFinishTime;
  }

  public List<String> getReminders() {
    return reminders;
  }

  public void setReminders(List<String> reminders) {
    this.reminders = reminders;
  }
}
