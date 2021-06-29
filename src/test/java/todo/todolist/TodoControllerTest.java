package todo.todolist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;
import todo.todolist.controller.TodoController;
import todo.todolist.model.TodoItem;
import todo.todolist.repo.TodoRepo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.data.jpa.domain.Specification.where;

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
    public void findByIdTest() {
        TodoItem resultFromRepo = new TodoItem();
        resultFromRepo.setId(1L);
        Optional<TodoItem> resultOptional = Optional.of(resultFromRepo);

        when(todoRepoMock.findById(1L)).thenReturn(resultOptional);

        Optional<TodoItem> result = subject.findById(1L);

        assertThat(result, is(resultOptional));
    }

    @Test
    public void findByFinishedTest() {
        TodoItem resultFromRepo = new TodoItem();
        resultFromRepo.setFinished(true);
        Optional<TodoItem> resultOptional = Optional.of(resultFromRepo);

        List<TodoItem> itemList = new ArrayList<>();
        itemList.add(resultFromRepo);

        ArgumentCaptor<Specification> specificationCaptor = ArgumentCaptor.forClass(Specification.class);
        when(todoRepoMock.findAll(specificationCaptor.capture())).thenReturn(itemList);

        List<TodoItem> result = subject.findByFinished(true);

        assertThat(result, is(itemList));
    }

    @Test
    public void updateFinishedTest_success() {
        TodoItem resultFromRepo = new TodoItem();
        resultFromRepo.setFinished(false);
        resultFromRepo.setId(1L);

        when(todoRepoMock.findById(1L)).thenReturn(Optional.of(resultFromRepo));
        when(todoRepoMock.save(resultFromRepo)).thenReturn(resultFromRepo);

        TodoItem result = subject.updateFinished(1L, true);

        assertTrue(result.isFinished());
    }

    @Test
    public void updateFinishedTest_nonexistentId() {
        when(todoRepoMock.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            subject.updateFinished(1L, false);
        });
    }

    @Test
    public void updateTest_success() {
        TodoItem resultFromRepo = new TodoItem();
        resultFromRepo.setFinished(false);
        resultFromRepo.setId(1L);
        resultFromRepo.setDescription("Not Updated");

        TodoItem updateToRepo = new TodoItem();
        updateToRepo.setFinished(true);
        updateToRepo.setId(1L);
        updateToRepo.setDescription("Updated");

        when(todoRepoMock.findById(1L)).thenReturn(Optional.of(resultFromRepo));
        when(todoRepoMock.save(resultFromRepo)).thenReturn(resultFromRepo);

        TodoItem result = subject.update(updateToRepo);

        assertTrue(result.isFinished() && result.getDescription().equals("Updated"));
    }

    @Test
    public void updateTest_nonexistentId() {
        when(todoRepoMock.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            subject.update(new TodoItem());
        });
    }

    @Test
    public void deleteTest_success() {
        TodoItem resultFromRepo = new TodoItem();
        resultFromRepo.setId(1L);
        resultFromRepo.setFinished(false);

        when(todoRepoMock.findById(1L)).thenReturn(Optional.of(resultFromRepo));

        assertDoesNotThrow( () -> {
            subject.delete(1L);
        });
    }

    @Test
    public void deleteTest_badInput() {
        when(todoRepoMock.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            subject.delete(1L);
        });
    }
}
