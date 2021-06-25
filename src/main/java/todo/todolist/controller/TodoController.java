package todo.todolist.controller;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import todo.todolist.model.TodoItem;
import todo.todolist.repo.TodoRepo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/todo")
public class TodoController {

    @Autowired
    private TodoRepo todoRepo;

    @Before
    public void test() {
        TodoTest.test(this);
    }

    @PostMapping
    public TodoItem save(@Valid @NotBlank @RequestBody TodoItem todoItem) {
        return todoRepo.save(todoItem);
    }

    @GetMapping
    public List<TodoItem> findAll() {
        return todoRepo.findAll();
    }

    @GetMapping
    @RequestMapping(value = "/{id}")
    public Optional<TodoItem> find(@PathVariable("id") Long id) {
        return todoRepo.findById(id);
    }

    @GetMapping
    @RequestMapping(value = "/finished={finished}")
    public List<TodoItem> find(@PathVariable("finished") boolean finished) {
        List<TodoItem> todoItemList = todoRepo.findAll();
        List<TodoItem> todoItemFinished = new ArrayList<>();

        todoItemList.forEach(todoItem -> {
            if (todoItem.isFinished() == finished) {
                todoItemFinished.add(todoItem);
            }
        });

        return todoItemFinished;
    }

    @PutMapping
    @RequestMapping(value = "/update/{id}/{finished}")
    public TodoItem update(@PathVariable("id") Long id, @PathVariable("finished") boolean finished) {
        Optional<TodoItem> todoItem = todoRepo.findById(id);
        if (todoItem.isPresent()) {
            todoItem.get().setFinished(finished);
            todoRepo.save(todoItem.get());
            return todoItem.get();
        }

        return null;
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

        return null;
    }

    @DeleteMapping
    @RequestMapping(value = "/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        Optional<TodoItem> todoItem = todoRepo.findById(id);
        if (todoItem.isPresent()) {
            todoRepo.delete(todoItem.get());
        }
    }
}
