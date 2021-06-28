package todo.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import todo.todolist.model.TodoItem;
import todo.todolist.repo.TodoRepo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@RestController
@RequestMapping(value = "/todo")
public class TodoController {

    private TodoRepo todoRepo;

    @Autowired
    public TodoController(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    @PostMapping
    public TodoItem save(@Valid @RequestBody TodoItem todoItem) {
        return todoRepo.save(todoItem);
    }

    @GetMapping
    public List<TodoItem> findAll() {
        return todoRepo.findAll();
    }

    @GetMapping
    @RequestMapping(value = "/{id}")
    public Optional<TodoItem> findById(@PathVariable("id") Long id) {
        return todoRepo.findById(id);
    }

    @GetMapping
    @RequestMapping(value = "/finished/{finished}")
    public List<TodoItem> findByFinished(@PathVariable("finished") boolean finished) {
        List<TodoItem> todoItemList = todoRepo.findAll(where(isFinished(finished)));

        return todoItemList;
    }

    @PutMapping
    @RequestMapping(value = "/update/{id}/{finished}")
    public TodoItem updateFinished(@PathVariable("id") Long id, @PathVariable("finished") boolean finished) {
        Optional<TodoItem> todoItem = todoRepo.findById(id);
        if (todoItem.isPresent()) {
            todoItem.get().setFinished(finished);
            return todoRepo.save(todoItem.get());
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "item not found"
        );
    }

    @PutMapping(path="/update")
    public TodoItem update(@Valid @RequestBody TodoItem newTodoItem) {
        Optional<TodoItem> todoItem = todoRepo.findById(newTodoItem.getId());
        if (todoItem.isPresent()) {
            todoItem.get().setDescription(newTodoItem.getDescription());
            todoItem.get().setTitle(newTodoItem.getTitle());
            todoItem.get().setFinished(newTodoItem.isFinished());
            todoRepo.save(todoItem.get());
            return todoItem.get();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "item not found"
        );
    }

    @DeleteMapping
    @RequestMapping(value = "/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        Optional<TodoItem> todoItem = todoRepo.findById(id);
        if (todoItem.isPresent()) {
            todoRepo.delete(todoItem.get());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "item not found"
            );
        }
    }

    // TODO: Refactor (implement elsewhere)
    private <TodoItem> Specification<TodoItem> isFinished(boolean finished) {
        return new Specification<TodoItem>() {
            @Override
            public Predicate toPredicate(Root<TodoItem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("finished"), finished);
            }
        };
    }
}
