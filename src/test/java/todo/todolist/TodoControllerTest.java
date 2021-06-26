package todo.todolist;

import org.junit.Before;
import org.junit.Test;
import todo.todolist.controller.TodoController;
import todo.todolist.model.TodoItem;
import todo.todolist.repo.TodoRepo;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.CoreMatchers.is;

public class TodoControllerTest {
    private TodoController subject;
    private TodoRepo todoRepoMock;

    @Before
    public void setup() {
        todoRepoMock = mock(TodoRepo.class);
        subject = new TodoController(todoRepoMock);
    }

    @Test
    public void saveTest() {
        TodoItem in = new TodoItem();
        TodoItem out = new TodoItem();

        when(todoRepoMock.save(in)).thenReturn(out);

        TodoItem result = subject.save(in);

        assertThat(result, is(out));
    }

    @Test
    public void updateFinishedTest() {
        TodoItem resultFromRepo = new TodoItem();
        resultFromRepo.setFinished(false);
        resultFromRepo.setId(1L);

        when(todoRepoMock.findById(1L)).thenReturn(Optional.of(resultFromRepo));
        when(todoRepoMock.save(resultFromRepo)).thenReturn(resultFromRepo);

        TodoItem result = subject.updateFinished(1L, true);

        assertTrue(result.isFinished());
    }

    @Test
    public void updateFinishedSuccess() {
        TodoItem resultFromRepo = new TodoItem();
        resultFromRepo.setFinished(false);
        resultFromRepo.setId(1L);

        when(todoRepoMock.findById(1L)).thenReturn(Optional.of(resultFromRepo));
        when(todoRepoMock.save(resultFromRepo)).thenReturn(resultFromRepo);

        TodoItem result = subject.updateFinished(1L, true);

        assertTrue(result.isFinished());
    }
}
