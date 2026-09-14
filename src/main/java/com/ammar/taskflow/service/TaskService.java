package com.ammar.taskflow.service;

import com.ammar.taskflow.domain.*;
import com.ammar.taskflow.event.TaskCompletedEvent;
import com.ammar.taskflow.event.TaskCreatedEvent;
import com.ammar.taskflow.event.TaskEventPublisher;
import com.ammar.taskflow.exception.InvalidTaskStateException;
import com.ammar.taskflow.repository.ReminderRepository;
import com.ammar.taskflow.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;
    private final ReminderRepository reminderRepository;
    private final TaskEventPublisher eventPublisher;

    public TaskService(TaskRepository taskRepository, TaskEventPublisher eventPublisher , ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
    }

    public Task createTask(User owner, String title, LocalDateTime dueDate, Priority priority) {
        Task task = new Task(owner, title, dueDate, priority);
        Task saved = taskRepository.save(task);
        eventPublisher.publish(new TaskCreatedEvent(saved));
        return saved;
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
        Task updated = taskRepository.update(task);
        eventPublisher.publish(new TaskCompletedEvent(updated));
        return updated;
    }

    public Task updateTaskDueDate(Long id, LocalDateTime dueDate){
        Task task = getTaskById(id);
        task.updateDueDate(dueDate);
        Task updated = taskRepository.update(task);

        if (task.getReminder() != null && dueDate.isBefore(task.getReminder().getTriggerTime())) {
            reminderRepository.deleteById(task.getId());
            System.out.println("[TaskFlow] Reminder deleted — new due date is before the reminder trigger time. Please recreate it.");
        }

        return updated;
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

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
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
