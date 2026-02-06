# Todo App 后端工程

基于 **Spring Boot** 构建的 RESTful API 后端服务。

## 🛠 技术栈
- **语言**: Java 8
- **框架**: Spring Boot 2.7.18
- **ORM**: Spring Data JPA
- **数据库**: MySQL 8.0
- **构建工具**: Maven

## ⚙️ 环境要求
- JDK 8 或更高版本
- Maven 3.x
- MySQL Server

## 📝 配置说明

配置文件位于 `src/main/resources/application.yml`。

### 1. 数据库准备
请确保本地 MySQL 已创建名为 `todo_db` 的数据库：
```sql
CREATE DATABASE todo_db;
```

### 2. 修改连接信息
打开 `application.yml` 并更新你的数据库账号密码：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todo_db?useSSL=false&serverTimezone=UTC
    username: root          # 修改为你的用户名
    password: your_password # 修改为你的密码
```

## 🚀 启动运行

你可以使用项目自带的 Maven Wrapper 或本地 Maven 运行。

### 使用 Maven Wrapper (推荐)
```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
mvnw spring-boot:run
```

### 使用本地 Maven
```bash
mvn spring-boot:run
```

服务启动后将运行在端口 **8080**。

## 🔌 API 接口文档

基础路径: `http://localhost:8080`

| 请求方法 | 接口路径 | 描述 | 请求参数示例 (JSON) |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/todos` | 获取所有待办事项 | N/A |
| **POST** | `/api/todos` | 创建新的待办 | `{"title": "买牛奶", "completed": false}` |
| **PUT** | `/api/todos/{id}` | 更新待办状态 | `{"id": 1, "title": "买牛奶", "completed": true}` |
| **DELETE** | `/api/todos/{id}` | 删除待办 | N/A |

## 📂 项目结构
*   `controller`: **Web 层** - 处理 HTTP 请求，对外暴露 RESTful 接口。
*   `entity`: **领域模型层** - 定义与数据库表对应的 Java 对象 (POJO)。
*   `repository`: **数据访问层** - 基于 Spring Data JPA 接口，自动实现 CRUD 操作。