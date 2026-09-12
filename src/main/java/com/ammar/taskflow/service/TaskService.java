package com.ammar.taskflow.service;

import com.ammar.taskflow.domain.Priority;
import com.ammar.taskflow.domain.Status;
import com.ammar.taskflow.domain.Task;
import com.ammar.taskflow.domain.User;
import com.ammar.taskflow.exception.InvalidTaskStateException;
import com.ammar.taskflow.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(User owner, String title, LocalDateTime dueDate, Priority priority) {
        Task task = new Task(owner, title, dueDate, priority);
        return taskRepository.save(task);
    }

    public Task startTask(Long id){
        Task task = getTaskById(id);
        if (task.getStatus() == Status.DONE){
            throw new InvalidTaskStateException("Cannot start a task that is already done");
        }
        task.setAsInProgress();
        return taskRepository.update(task);
    }

    public Task completeTask(Long id){
        Task task = getTaskById(id);
        if (task.getStatus() == Status.DONE){
            throw new InvalidTaskStateException("Cannot complete a task that is already done");
        }
        task.setAsDone();
        return taskRepository.update(task);
    }

    public Task updateTaskDueDate(Long id, LocalDateTime dueDate){
        Task task = getTaskById(id);
        task.updateDueDate(dueDate);
        return taskRepository.update(task);
    }

    public Task updateTaskPriority(Long id, Priority priority){
        Task task = getTaskById(id);
        task.updatePriority(priority);
        return taskRepository.update(task);
    }

    public Task updateTaskTitle(Long id, String title){
        Task task = getTaskById(id);
        task.updateTitle(title);
        return taskRepository.update(task);
    }

    public void deleteTaskById(Long id) {
        getTaskById(id);
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksBeforeDueDate(LocalDateTime dueDate) {
        return taskRepository.findDueBefore(dueDate);
    }

    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new InvalidTaskStateException("Task not found with this id");
        }
        return task;
    }
}
