package com.example.learn.entity;

import com.example.learn.converter.StringListConverter;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.*;
import java.util.List;

@Entity
public class Todo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private boolean completed;
  private String description;

  // 存储公司时区时间
  @Column(name = "plannedFinishTime", columnDefinition = "DATETIME(6)")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime plannedFinishTime;

  @Column(name = "reminders")
  @Convert(converter = StringListConverter.class)
  private List<String> reminders;

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

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
