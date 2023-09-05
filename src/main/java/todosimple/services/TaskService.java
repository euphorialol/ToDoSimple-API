package todosimple.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todosimple.models.Task;
import todosimple.models.User;
import todosimple.repositories.TaskRepository;
import todosimple.services.exceptions.DataBindingViolationException;
import todosimple.services.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private  UserService userService;

    public Task findById(Long id){
        Optional<Task> task =  this.taskRepository.findById(id);
        return  task.orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id  + ", Tipo: " + Task.class.getName()));
    }

    public List<Task> findAllByUserId(Long userId){

        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;

    }

    @Transactional
    public Task create(Task obj){
        User user = this.userService.findByID(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj){
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e){
            throw new DataBindingViolationException("não foi possível excluir pois há entidades relacionadas!" + e);
        }
    }

}
