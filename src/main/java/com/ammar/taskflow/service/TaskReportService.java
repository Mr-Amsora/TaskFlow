package com.ammar.taskflow.service;

import com.ammar.taskflow.algorithm.InsertionTaskSorter;
import com.ammar.taskflow.domain.Priority;
import com.ammar.taskflow.domain.Task;
import com.ammar.taskflow.domain.User;
import com.ammar.taskflow.repository.TaskRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ammar.taskflow.domain.Status.DONE;
import static com.ammar.taskflow.domain.Status.OVERDUE;

public class TaskReportService {

    private final TaskRepository taskRepository;

    public TaskReportService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getDueSoonReport(LocalDateTime deadLine){
        List<Task> dueSoonTasks = new ArrayList<>(taskRepository.findDueBefore(deadLine));
        InsertionTaskSorter.sort(dueSoonTasks);
        return dueSoonTasks;
    }

    public long userTaskCompletedThisWeek(User user){
        LocalDateTime beforeWeek = LocalDateTime.now().minusWeeks(1);
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .filter(task -> task.getOwner().equals(user))
                .filter(task -> task.getStatus() == DONE)
                .filter(task -> task.getCompletedAt().isAfter(beforeWeek))
                .count();
    }

    public Map<Priority, Long> overdueTaskCountByPriority(){
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .filter(task -> task.getStatus() == OVERDUE)
                .collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
    }

    public double averageCompletionHours(){
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .filter(task -> task.getStatus() == DONE)
                .mapToLong(task -> Duration.between(task.getCreatedAt(),task.getCompletedAt()).toHours())
                .average()
                .orElse(0);
    }
}
