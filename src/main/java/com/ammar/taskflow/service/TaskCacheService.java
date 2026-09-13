package com.ammar.taskflow.service;

import com.ammar.taskflow.domain.Task;
import com.ammar.taskflow.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskCacheService {

    private final TaskRepository taskRepository;
    private final List<Task> tasks = new ArrayList<>();

    public TaskCacheService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void refreshCache() {
        tasks.clear();
        tasks.addAll(taskRepository.findAll());
    }

    public Task getTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}
