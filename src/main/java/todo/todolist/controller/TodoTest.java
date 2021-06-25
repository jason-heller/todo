package todo.todolist.controller;

import org.aspectj.lang.annotation.Before;
import static org.mockito.Mockito.*;

import todo.todolist.model.TodoItem;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {
    public static void test(TodoController cont) {

        TodoItem item1 = mock(TodoItem.class);
        TodoItem item2 = mock(TodoItem.class);

        when(item1.getId()).thenReturn(1l);
        when(item1.getDescription()).thenReturn("item 1 - desc");
        when(item1.getTitle()).thenReturn("item 1 - title");

        when(item1.getId()).thenReturn(2l);
        when(item1.getDescription()).thenReturn("item 2 - desc");
        when(item1.getTitle()).thenReturn("item 2 - title");

        // CRUD
        cont.save(item1);
        cont.update(1l, true);
        cont.save(item2);
        cont.find(false);
        cont.findAll();
        cont.delete(2l);
        cont.findAll();
    }
}
