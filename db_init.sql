RENAME TABLE todo TO todo_backup_2026;

CREATE TABLE todo_0 (
    id BIGINT NOT NULL,
    title VARCHAR(255),
    completed BOOLEAN,
    description VARCHAR(255),
    planned_finish_time DATETIME(6),
    reminders TEXT,
    user_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE todo_1 LIKE todo_0;

CREATE TABLE todo_mark (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    user_id BIGINT,
    CONSTRAINT uk_user_title UNIQUE (user_id, title)
);

CREATE TABLE todo_mark_relation_0 (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    todo_id BIGINT NOT NULL,
    mark_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME(6),
    UNIQUE KEY uk_todo_mark (todo_id, mark_id)
);

CREATE TABLE todo_mark_relation_1 LIKE todo_mark_relation_0;

INSERT INTO todo_mark (title, user_id) VALUES ('Work', NULL), ('Life', NULL), ('Urgent', NULL);
