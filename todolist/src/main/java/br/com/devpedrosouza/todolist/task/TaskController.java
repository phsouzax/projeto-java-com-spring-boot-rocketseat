package br.com.devpedrosouza.todolist.task;

import org.springframework.web.bind.annotation.RestController;

import br.com.devpedrosouza.utils.utils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskModel.getStartAt()) || (currentDate.isAfter(taskModel.getEndAt()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("A data de início / data de término não pode ser anterior a data atual");

        }
         if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("A data de início menor que a data de término");
         }


        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);
        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }
    @PutMapping("/{id}")
    public ResponseEntity update (@RequestBody TaskModel taskModel, HttpServletRequest 
        request, @PathVariable UUID id){
        var task = this.taskRepository.findById(id).orElse(null);
            if (task == null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Tarefa não encontrada");
            }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Usuário sem autorização para atualizar esta tarefa");
        }
        utils.copyNonNullProperties(taskModel, task);
        var taskUpdate = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdate);
     }

    }
