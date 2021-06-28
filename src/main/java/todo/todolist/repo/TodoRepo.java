package todo.todolist.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import todo.todolist.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepo extends JpaRepository<TodoItem, Long>, JpaSpecificationExecutor<TodoItem> {
}
