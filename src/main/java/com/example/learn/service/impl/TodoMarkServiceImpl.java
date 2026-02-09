package com.example.learn.service.impl;

import com.example.learn.entity.TodoMark;
import com.example.learn.repository.TodoMarkRepository;
import com.example.learn.service.TodoMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoMarkServiceImpl implements TodoMarkService {

    @Autowired
    private TodoMarkRepository todoMarkRepository;

    @Override
    public List<TodoMark> findAll() {
        return todoMarkRepository.findAll();
    }

    @Override
    public TodoMark create(TodoMark todoMark) {
        // 核心校验逻辑：仅当 userId 为 null 时手动检查（因为数据库的唯一索引对 null 不生效）
        // TODO: 后续启用完整用户登录流程，确定业务逻辑不需要允许 null 值 userId 重复或改用其他策略后，可移除此手动校验
        if (todoMark.getUserId() == null) {
            boolean exists = todoMarkRepository.existsByUserIdIsNullAndTitle(todoMark.getTitle());
            if (exists) {
                throw new RuntimeException("公共标签名称已存在，请勿重复创建");
            }
        }
        
        // 当 userId 不为 null 时，数据库的 @UniqueConstraint(columnNames = {"userId", "title"}) 会自动保证唯一性
        // 如果重复会抛出 DataIntegrityViolationException，建议在全局异常处理器中捕获
        return todoMarkRepository.save(todoMark);
    }
}
