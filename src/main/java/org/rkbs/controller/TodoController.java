package org.rkbs.controller;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.rkbs.model.TodoDTO;
import org.rkbs.model.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {
	
	@Autowired
	private TodoRepository todoRepository;
	
	@GetMapping("/todos")
	public ResponseEntity<?> getAllTodos(){
		List<TodoDTO> todos = todoRepository.findAll();
		
		if(todos.size() != 0) {
			return new ResponseEntity<List<TodoDTO>>(todos, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Noto todos available", HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PostMapping("/todos")
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDto){
		
		try {
			todoDto.setCreatedAt(new Date(System.currentTimeMillis() ));
			todoRepository.save(todoDto);
			return new ResponseEntity<TodoDTO>(todoDto, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@PutMapping("/todos/{id}")
	public ResponseEntity<?> updateTodoById(@PathVariable("id") String id, @RequestBody TodoDTO todo){
		Optional<TodoDTO> todoOptional = todoRepository.findById(id);
		if(todoOptional.isPresent() ) {
			TodoDTO todoToUpdate = todoOptional.get();
			todoToUpdate.setCompleted(todo.getCompleted() != null ? todo.getCompleted() : todoToUpdate.getCompleted());
			todoToUpdate.setTodo(todo.getTodo() !=null ? todo.getTodo() : todoToUpdate.getTodo() );
			todoToUpdate.setDescription(todo.getDescription() != null ? todo.getDescription() : todoToUpdate.getDescription() );
			todoToUpdate.setUpdatedAt( new Date(System.currentTimeMillis() ));
			
			todoRepository.save(todoToUpdate);
			
			return new ResponseEntity<>(todoToUpdate, HttpStatus.OK);
		
		}else {
			return new ResponseEntity<>("Todo not found with id " + id, HttpStatus.NOT_FOUND);
		}
		
	}
	

	@GetMapping("/todos/{id}")
	public ResponseEntity<?> getSingleTodo(@PathVariable("id") String id){
		Optional<TodoDTO> todoOptional = todoRepository.findById(id);
		if(todoOptional.isPresent() ) {
			return new ResponseEntity<>(todoOptional.get() , HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Todo not found with id ", HttpStatus.NOT_FOUND);
		}
		
	}
	
	@DeleteMapping("/todos/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
		
		try {
			todoRepository.deleteById(id);
			return new ResponseEntity<> ("Sucessfully delete with id " + id, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	
	
	
	
	
	
	
	

}
