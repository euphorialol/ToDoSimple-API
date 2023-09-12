package todosimple.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todosimple.models.Task;
import todosimple.services.TaskService;
import todosimple.services.UserService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        Task obj = this.taskService.findById(id);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj){
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id){

        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        this.taskService.delete(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>> findAllByUser(){

        List<Task> objs = this.taskService.findAllByUser();
        return ResponseEntity.ok().body(objs);

    }
}
