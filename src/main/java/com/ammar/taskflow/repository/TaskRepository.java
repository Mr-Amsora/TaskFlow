package com.ammar.taskflow.repository;

import com.ammar.taskflow.domain.Task;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public class TaskRepository extends Repository<Task, Long> {
    public TaskRepository(EntityManager entityManager) {
        super(entityManager, Task.class);
    }

    public List<Task> findDueBefore(LocalDateTime dueTime) {
        String query = "SELECT t FROM Task t WHERE t.dueDate <= :dueTime AND t.status != 'DONE'";
        return entityManager.createQuery(query, Task.class)
                .setParameter("dueTime", dueTime)
                .getResultList();
    }
}
