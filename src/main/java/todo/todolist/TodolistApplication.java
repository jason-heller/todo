package todo.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import todo.todolist.controller.TodoTest;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {

		// SpringApplication.run(TodolistApplication.class, args);

		new TodoTest();
	}

}
