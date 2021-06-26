package todo.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem {
    private long id;
    @NotBlank
    private String title;
    private String description;
    private boolean finished;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }
}
